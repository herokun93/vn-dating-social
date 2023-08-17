package vn.dating.app.social.config.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import vn.dating.app.social.models.User;
import vn.dating.app.social.repositories.UserRepository;

import java.time.Instant;
import java.util.Optional;

@Component
public class WebSocketConnectedEventListener implements ApplicationListener<SessionConnectedEvent> {

    @Autowired
    private WebSocketSessionRegistry webSocketSessionRegistry;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onApplicationEvent(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = headerAccessor.getUser().getName();
        String sessionId = headerAccessor.getSessionId();

        webSocketSessionRegistry.registerSessionId(username, sessionId);

        Optional<User> currentUser = userRepository.findById(username);


        if (currentUser.isPresent()) {
            boolean isOnline = webSocketSessionRegistry.isUserOnline(username);
            boolean isOnlineDb = currentUser.get().isOnline();
            if(isOnline != isOnlineDb){
                currentUser.get().setOnline(isOnline);
                currentUser.get().setLastOnline(Instant.now());
                userRepository.save(currentUser.get());
            }
        } else {
            // handle new user registration if needed
        }

//        System.out.println("Session " + sessionId + " connected");
//        System.out.println("User  " + headerAccessor.getUser().getName() + " connected");
    }
}


