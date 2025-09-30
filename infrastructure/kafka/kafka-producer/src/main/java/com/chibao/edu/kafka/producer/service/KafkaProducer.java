package com.chibao.edu.kafka.producer.service;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.support.SendResult;

import java.io.Serializable;
import java.util.function.BiConsumer;

public interface KafkaProducer<K extends Serializable, V extends SpecificRecordBase> {
    void send(String topicName, K key, V message, BiConsumer<SendResult<K, V>, Throwable> callback);
}

// ? BiConsumer<T, U> --> functional interface --> method void accept(T t, U u)
// ? SendResult<K, V> --> Spring Kafka’s small wrapper returned when a send completes
// ? SpecificRecordBase --> Avro code generation. When you compile an Avro schema into Java classes,
// * generated classes typically extend SpecificRecordBase and implement SpecificRecord

