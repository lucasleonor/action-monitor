package com.betvictor.test.messaging.actionmonitor.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import static com.betvictor.test.messaging.actionmonitor.controller.MessageController.SPECIFIC_USER;
import static com.betvictor.test.messaging.actionmonitor.controller.UserController.USERS;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    public static final String SOCKETS = "/private/sockets";

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker(USERS, SPECIFIC_USER);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(SOCKETS)
                .withSockJS();
    }
}
