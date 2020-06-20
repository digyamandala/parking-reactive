package com.parking.reactive.command.event.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

  private static final String LOCALHOST_9092 = "localhost:9092";

  @Bean
  public KafkaTemplate kafkaTemplateLocal() {
    return new KafkaTemplate<>(kafkaProducerLocal());
  }

  @Bean
  public DefaultKafkaProducerFactory<String, Object> kafkaProducerLocal() {
    Map<String, Object> configuration = new HashMap<>();
    configuration.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, LOCALHOST_9092);
    configuration.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configuration.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

    return new DefaultKafkaProducerFactory<>(configuration);
  }
}
