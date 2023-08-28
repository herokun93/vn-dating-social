package vn.dating.app.social.config.socket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import vn.dating.app.social.models.User;
import vn.dating.app.social.repositories.UserRepository;

import java.time.Instant;
import java.util.Optional;


//@Component
//@Slf4j
//public class WebSocketDisconnectEventListener implements ApplicationListener<SessionDisconnectEvent> {
//
//    @Autowired
//    private WebSocketSessionRegistry webSocketSessionRegistry;
//
//
//    @Autowired
//    private UserRepository userRepository;
//
//
//    @Override
//    public void onApplicationEvent(SessionDisconnectEvent event) {
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//
//        if(headerAccessor !=null){
//            if(headerAccessor.getUser() !=null){
//                String username = headerAccessor.getUser().getName();
//                String sessionId = headerAccessor.getSessionId();
//
//                webSocketSessionRegistry.unregisterSessionId(username, sessionId);
//
//                Optional<User> currentUser = userRepository.findById(username);
//                if (currentUser.isPresent()) {
//                    boolean isOnline = webSocketSessionRegistry.isUserOnline(username);
//                    boolean isOnlineDb = currentUser.get().isOnline();
//                    log.info(isOnline+"");
//                    if(isOnline != isOnlineDb){
//                        currentUser.get().setOnline(isOnline);
//                        currentUser.get().setLastOnline(Instant.now());
//                        userRepository.save(currentUser.get());
//                    }
//                }
//            }
//        }
//
//
//
//
//        // Handle disconnect event
////        System.out.println("Session " + sessionId + " disconnected");
////        System.out.println("User  " + headerAccessor.getUser().getName() + " disconnected");
//    }
//}
