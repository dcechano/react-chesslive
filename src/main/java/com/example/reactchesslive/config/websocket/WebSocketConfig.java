package com.example.reactchesslive.config.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeHandler;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic", "/queue");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        HandshakeHandler handshakeHandler = (request, response, wsHandler, attributes) -> {
            System.out.println("Inside the websocket Handshake handler");
            return true;
        };

        registry
                .addEndpoint("/chess-lite")
                .setHandshakeHandler(handshakeHandler)
                        .setAllowedOrigins("http://localhost:3000", "https://localhost:3000", "ws://localhost:3000");

        registry
                .addEndpoint("/chess-lite")
                .setHandshakeHandler(handshakeHandler)
                .setAllowedOrigins("http://localhost:3000", "https://localhost:3000", "ws://localhost:3000")
                .withSockJS();
    }
}
