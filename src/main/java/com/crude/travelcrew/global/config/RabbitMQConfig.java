package com.crude.travelcrew.global.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

	@Value("${spring.rabbitmq.host}")
	private String host;

	@Value("${spring.rabbitmq.username}")
	private String username;

	@Value("${spring.rabbitmq.password}")
	private String password;

	private static final String CHAT_EXCHANGE_NAME = "chat.exchange";
	private static final String CHAT_QUEUE_NAME = "chat.queue";
	private static final String CHAT_ROUTING_KEY = "chat.room.*";

	private static final String NOTIFICATION_EXCHANGE_NAME = "notification.exchange";
	private static final String NOTIFICATION_QUEUE_NAME = "notification.queue";
	private static final String NOTIFICATION_ROUTING_KEY = "notification.key";

	@Bean
	public TopicExchange topicExchange() {
		return new TopicExchange(CHAT_EXCHANGE_NAME);
	}

	@Bean
	public DirectExchange directExchange() {
		return new DirectExchange(NOTIFICATION_EXCHANGE_NAME);
	}

	@Bean
	public Queue chatQueue() {
		return new Queue(CHAT_QUEUE_NAME);
	}

	@Bean
	Queue notificationQueue() {
		return new Queue(NOTIFICATION_QUEUE_NAME);
	}

	@Bean
	public Binding notificationBinding(DirectExchange directExchange) {
		return BindingBuilder.bind(notificationQueue()).to(directExchange).with(NOTIFICATION_ROUTING_KEY);
	}

	@Bean
	public Binding chatBinding(TopicExchange topicExchange) {
		return BindingBuilder.bind(chatQueue()).to(topicExchange).with(CHAT_ROUTING_KEY);
	}

	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory factory = new CachingConnectionFactory();
		factory.setHost(host);
		factory.setUsername(username);
		factory.setPassword(password);
		return factory;
	}

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(messageConverter);
		return rabbitTemplate;
	}

	@Bean
	public MessageConverter messageConverter() {
		ObjectMapper objectMapper = new ObjectMapper();
		// LocalDateTime serializable 위해
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
		objectMapper.registerModule(dateTimeModule());
		return new Jackson2JsonMessageConverter(objectMapper);
	}

	@Bean
	public Module dateTimeModule() {
		return new JavaTimeModule();
	}
}