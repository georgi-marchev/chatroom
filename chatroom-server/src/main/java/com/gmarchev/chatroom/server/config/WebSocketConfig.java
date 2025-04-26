package com.gmarchev.chatroom.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {

		// TODO: Configurable
		registry.addEndpoint("/gmarchev-chatroom")
				.setAllowedOriginPatterns("*")
				.withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {

		// Enable a simple memory-based message broker to carry the greeting messages back to the client on
		// destinations prefixed with /topic and /queue
		registry.enableSimpleBroker("/topic", "/queue");

		// Designate the /app prefix for messages that are bound for methods annotated with @MessageMapping
		registry.setApplicationDestinationPrefixes("/app");

		// Enable a user destination where data can be broadcasted for each user via session ID
		registry.setUserDestinationPrefix("/user");
	}
}
