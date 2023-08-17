package vn.dating.app.social.config.socket;

import com.sun.security.auth.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import vn.dating.app.social.services.UserService;

import java.security.Principal;
import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class WebSocketBrokerConfig implements WebSocketMessageBrokerConfigurer {


    @Autowired
    private UserService userService;


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
//                log.info("new connect socket");
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);


                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    List<String> authorization = accessor.getNativeHeader("Authorization");

                    if(authorization !=null){
                        for(int i=0;i<authorization.size();i++){
                            if(authorization.get(i).indexOf("Bearer ")>-1) {
                                log.info("read token from header");

                                String token = authorization.get(i);
                                token = token.substring(7, token.length());
                                String userId = userService.getCurrentUserByToken(token);

                                if(userId !=null){
                                    Principal principal = new Principal() {
                                        @Override
                                        public String getName() {
                                            return userId;
                                        }
                                    };
                                    accessor.setUser(principal);

                                }


//                                log.info("New connect {}", principal.getName());
//                                accessor.setSessionId(gmail);
                            }
                        }
                    }
                }
                return message;
            }
        });
    }


}
