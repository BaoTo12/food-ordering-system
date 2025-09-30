package com.chibao.edu.kafka.producer.service.impl;

import com.chibao.edu.kafka.producer.exception.KafkaProducerException;
import com.chibao.edu.kafka.producer.service.KafkaProducer;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

@Slf4j
@Component
public class KafkaProducerImpl<K extends Serializable, V extends SpecificRecordBase> implements KafkaProducer<K, V> {

    private final KafkaTemplate<K, V> kafkaTemplate;

    public KafkaProducerImpl(KafkaTemplate<K, V> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(String topicName, K key, V message, BiConsumer<SendResult<K, V>, Throwable> callback) {
        log.info("Sending message={} to topic={}", message, topicName);
        try {
            // ? CompletableFuture --> promise/async primitive
            /*
             * * It represents “a value that will be available later” and provides many ways to attach code that
             * * runs when the value arrives or when failure happens.
             * * whenComplete((result, ex) -> {...}) — run code for success or failure (ex tells you if it failed).
             * * thenApply(...) / thenAccept(...) — chain transformations on success.
             * * exceptionally(ex -> fallback) or handle(...) — recover from errors.
             * * get() or join() — block and wait (avoid in reactive/non-blocking code).
             */
            CompletableFuture<SendResult<K, V>> future = kafkaTemplate.send(topicName, key, message);
            // Attach callback
            future.whenComplete((result, ex) -> {
                if (callback != null) {
                    callback.accept(result, ex);
                }
                if (ex == null) {
                    RecordMetadata metadata = result.getRecordMetadata();
                    log.info("Message sent successfully to topic={}, partition={}, offset={}",
                            metadata.topic(), metadata.partition(), metadata.offset());
                } else {
                    log.error("Error on kafka producer with key={}, message={}, exception={}",
                            key, message, ex.getMessage(), ex);
                }
            });
        } catch (KafkaException e) {
            log.error("Error on kafka producer with key={}, message={}, exception={}",
                    key, message, e.getMessage(), e);
            throw new KafkaProducerException("Error on kafka producer with key: " + key
                    + " and message: " + message, e);
        }
    }
    @PreDestroy
    public void close() {
        if (kafkaTemplate != null) {
            log.info("Closing kafka producer!");
            kafkaTemplate.destroy();
        }
    }
}
