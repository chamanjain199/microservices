package com.example.movie.rate.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import com.example.movie.rate.constant.Constant;

@Configuration
public class KafkaTopicConfig {

	@Bean
	NewTopic topic() {
		return TopicBuilder.name(Constant.KAFKA_TOPIC_RATE_MOVIE).partitions(10).replicas(1).build();
	}

}