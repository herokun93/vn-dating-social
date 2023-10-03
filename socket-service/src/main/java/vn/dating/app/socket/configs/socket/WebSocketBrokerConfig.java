package vn.dating.app.socket.configs.socket;



import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import vn.dating.app.socket.dto.UserInfo;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class WebSocketBrokerConfig implements WebSocketMessageBrokerConfigurer {


    private final RestTemplate restTemplate;

    public WebSocketBrokerConfig(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/ws");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*").withSockJS().setWebSocketEnabled(true);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {


                    String apiUrl = "http://localhost:1994/api/gateway/auth/current";
                    Optional.ofNullable(accessor.getNativeHeader("Authorization")).ifPresent(ah ->
                    {
                        String bearerToken = ah.get(0).replace("Bearer ", "");
                        log.info(bearerToken);
                        HttpHeaders headers = new HttpHeaders();
                        headers.setBearerAuth(bearerToken);
                        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
                        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, requestEntity, String.class);
                        log.info(responseEntity.getBody());
                        String responseBody = responseEntity.getBody();
                        Principal principal = new Principal() {@Override
                        public String getName() {return responseBody;}};

                        accessor.setUser(principal);
                    });
                }
                return message;
            }
        });
    }
}

