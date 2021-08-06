package com.example.reactchesslive.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

//    TODO remove comments or uncomment
    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages.anyMessage().permitAll();

        messages.simpTypeMatchers(SimpMessageType.CONNECT, SimpMessageType.SUBSCRIBE,
                SimpMessageType.UNSUBSCRIBE, SimpMessageType.DISCONNECT,
                SimpMessageType.MESSAGE).permitAll();
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}
