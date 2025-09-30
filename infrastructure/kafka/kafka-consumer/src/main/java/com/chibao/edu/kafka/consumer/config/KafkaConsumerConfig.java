package com.chibao.edu.kafka.consumer.config;

import com.chibao.edu.kafka.config.data.KafkaConfigData;
import com.chibao.edu.kafka.config.data.KafkaConsumerConfigData;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class KafkaConsumerConfig<K extends Serializable, V extends SpecificRecordBase> {
    KafkaConfigData kafkaConfigData;
    KafkaConsumerConfigData kafkaConsumerConfigData;

    public Map<String, Object> consumerConfig(){
        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigData.getBootstrapServers());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaConsumerConfigData.getKeyDeserializer());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaConsumerConfigData.getValueDeserializer());
        // * Where to start reading when there is no committed offset for this consumer group.
        // * earliest (start from beginning)
        // * latest (start from new messages)
        // * none (throw error if no offset)
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaConsumerConfigData.getAutoOffsetReset());
        // * schema.registry.url: points Avro deserializer to the Schema Registry so it can fetch schemas.
        props.put(kafkaConfigData.getSchemaRegistryUrlKey(), kafkaConfigData.getSchemaRegistryUrl());
        // * specific.avro.reader (boolean): when true, the Avro deserializer returns generated
        // * SpecificRecord classes (e.g. User), not generic GenericRecord.
        props.put(kafkaConsumerConfigData.getSpecificAvroReaderKey(), kafkaConsumerConfigData.getSpecificAvroReader());
        // * max time broker waits for heartbeats before deeming consumer dead and triggering rebalance.
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, kafkaConsumerConfigData.getSessionTimeoutMs());
        // * how frequently consumer sends heartbeats. Must be significantly smaller than session.timeout.ms.
        // * Practical: Keep heartbeat.interval.ms ≈ sessionTimeout/3
        props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, kafkaConsumerConfigData.getHeartbeatIntervalMs());
        // * maximum time between calls to poll() before consumer is considered unresponsive and leaves the group (rebalance).
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, kafkaConsumerConfigData.getMaxPollIntervalMs());
        // * maximum bytes fetched per partition in one request.
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG
                , kafkaConsumerConfigData.getMaxPartitionFetchBytesDefault()
                        * kafkaConsumerConfigData.getMaxPartitionFetchBytesBoostFactor());
        // * maximum number of records returned in a single poll() call.
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, kafkaConsumerConfigData.getMaxPollRecords());

        return props;
    }

    @Bean
    public ConsumerFactory<K, V> consumerFactory(){
        // ? ConsumerFactory --> a factory that creates Kafka Consumer instances
        // ? DefaultKafkaConsumerFactory --> Spring-provided implementation of ConsumerFactory that creates consumers
        return new DefaultKafkaConsumerFactory<>(consumerConfig());
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<K, V>> kafkaListenerContainerFactory(){
        // ? KafkaListenerContainerFactory --> Factory for listener containers. Spring’s @KafkaListener uses this to create containers that
        // * poll Kafka and invoke your listener methods. It wires consumer factory, concurrency, batch mode, poll timeout and other
        // * runtime behaviors into the container that runs the consuming loop.
        // ? ConcurrentMessageListenerContainer --> The actual container that runs one or more Kafka consumer threads.
        // * Each consumer thread created by this container is a separate Kafka client instance in the same group; the broker will assign partitions across them.
        ConcurrentKafkaListenerContainerFactory<K, V> factory = new ConcurrentKafkaListenerContainerFactory<>();
        // * Tells the listener container which ConsumerFactory to use to create Kafka consumers.
        factory.setConsumerFactory(consumerFactory());
        // * Enables batch delivery to your @KafkaListener method (true means listener receives List<ConsumerRecord> or List<V>, false means single record).
        factory.setBatchListener(kafkaConsumerConfigData.getBatchListener());
        // * Number of concurrent consumer threads the container will start.
        factory.setConcurrency(kafkaConsumerConfigData.getConcurrencyLevel());
        // * Whether the container should start automatically when Spring context starts.
        factory.setAutoStartup(kafkaConsumerConfigData.getAutoStartup());
        // * Timeout (ms) that the container’s poll call will wait when no records are available.
        factory.getContainerProperties().setPollTimeout(kafkaConsumerConfigData.getPollTimeoutMs());
        return factory;
    }
}
