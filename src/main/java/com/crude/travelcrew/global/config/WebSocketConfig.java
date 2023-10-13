package com.crude.travelcrew.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.crude.travelcrew.domain.crew.handler.ChatPreHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Value("${spring.rabbitmq.host}")
	private String host;

	@Value("${spring.rabbitmq.username}")
	private String username;

	@Value("${spring.rabbitmq.password}")
	private String password;

	private final ChatPreHandler chatPreHandler;

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws") // 소켓 연결 uri
			.setAllowedOriginPatterns("*") // 소켓도 CORS 설정 필요
			.withSockJS(); // 소켓을 지원하지 않는 브라우저인 경우 sockJS 사용
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.setPathMatcher(new AntPathMatcher("."));
		registry.setApplicationDestinationPrefixes("/pub");
		// SimpleBroker 기능과 외부 message broker(RabbitMQ, ActiveMQ 등)에 메시지를 전달하는 기능을 가짐
		registry.enableStompBrokerRelay("/queue", "/topic", "/exchange", "/amq/queue")
			.setAutoStartup(true)
			.setClientLogin(username)
			.setClientPasscode(password)
			.setSystemLogin(username)
			.setSystemPasscode(password)
			.setRelayHost(host)
			.setRelayPort(61613);
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(chatPreHandler);
	}
}
