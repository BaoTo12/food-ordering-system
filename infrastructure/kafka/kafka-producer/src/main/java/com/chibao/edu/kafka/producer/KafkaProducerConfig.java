package com.chibao.edu.kafka.producer;

import com.chibao.edu.kafka.config.data.KafkaConfigData;
import com.chibao.edu.kafka.config.data.KafkaProducerConfigData;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class KafkaProducerConfig<K extends Serializable, V extends SpecificRecordBase> {
    KafkaConfigData kafkaConfigData;
    KafkaProducerConfigData kafkaProducerConfigData;

    @Bean
    public Map<String, Object> producerConfig() {
        Map<String, Object> props = new HashMap<>();

        // * Comma-separated list of Kafka broker addresses (host:port) your client will contact to discover the cluster.
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigData.getBootstrapServers());
        // * URL of your Schema Registry used with Avro serializers.
        // * Avro serializers register/fetch Schemas from this service. Messages carry a small schema id referring to the registry.
        props.put("schema.registry.url", kafkaConfigData.getSchemaRegistryUrl());
        // * Class that converts your message key (K) to bytes.
        // * Common choices: StringSerializer, UUIDSerializer, or ByteArraySerializer.
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProducerConfigData.getKeySerializerClass());
        // * Class that serializes the message value (V) to bytes. For Avro with Schema Registry you usually use KafkaAvroSerializer (Confluent).
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProducerConfigData.getValueSerializerClass());
        // * Maximum size (in bytes) of a batch of records the producer will attempt to build before sending to broker.
        // * Kafka sends messages in batches for efficiency — larger batches = better throughput, fewer requests.
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaProducerConfigData.getBatchSize() * kafkaProducerConfigData.getBatchSizeBoostFactor());
        // * How long the producer will wait (ms) for additional messages before sending a non-full batch.
        props.put(ProducerConfig.LINGER_MS_CONFIG, kafkaProducerConfigData.getLingerMs());
        // * Compression algorithm for batches. Values: none, gzip, snappy, lz4, zstd.
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, kafkaProducerConfigData.getCompressionType());
        // * How many broker replicas must acknowledge a write before it’s considered successful. Values: 0, 1, all (or -1).
        /*
        **  0 — fastest, no guarantee (fire-and-forget).
        **  1 — leader ack only (faster but if leader dies immediately you can lose data).
        **  all — wait for all in-sync replicas: slowest but safest. Usually recommended for production.
        * **/
        props.put(ProducerConfig.ACKS_CONFIG, kafkaProducerConfigData.getAcks());
        // * Maximum time the producer waits for a response for a request. If exceeded, it’s considered failed/retriable.
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, kafkaProducerConfigData.getRequestTimeoutMs());
        // * Number of retry attempts for sending a record if transient errors occur.
        props.put(ProducerConfig.RETRIES_CONFIG, kafkaProducerConfigData.getRetryCount());

        return props;
    }

    @Bean
    public ProducerFactory<K, V> producerFactory(){
        // ? ProducerFactory: Factory that creates KafkaProducer instances
        // ? Spring manages pooling/lifecycle through this factory; it centralizes config.
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean
    public KafkaTemplate<K, V> kafkaTemplate(){
        // ? KafkaTemplate: High-level Spring helper that wraps producer operations (send, flush) and integrates with Spring features.
        // ? Use KafkaTemplate to send messages easily
        return new KafkaTemplate<>(producerFactory());
    }
}
