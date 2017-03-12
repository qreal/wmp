package com.qreal.wmp.longpoll.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * Queue will look like endpoint/broker/*.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocket extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Messages for broker (service messages)
        config.enableSimpleBroker("/push");
        // Messages for annotated methods
        config.setApplicationDestinationPrefixes("/messages");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //Endpoint
        registry.addEndpoint("/diagrams").setAllowedOrigins("*").withSockJS();
    }
}
