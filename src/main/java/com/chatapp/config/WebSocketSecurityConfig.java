package com.chatapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;

@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
            .nullDestMatcher().hasRole("ANONYMOUS")
            .simpDestMatchers("/app/**", "/user/**", "/topic/**").hasRole("ANONYMOUS")
            .anyMessage().hasRole("ANONYMOUS");
    }

    @Override
    protected boolean sameOriginDisabled() {
        // Disable CSRF for WebSocket for now (enable in production)
        return true;
    }
}