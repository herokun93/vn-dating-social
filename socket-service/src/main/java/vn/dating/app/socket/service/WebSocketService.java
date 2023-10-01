package vn.dating.app.socket.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import vn.dating.app.socket.dto.Message;

@Service
@Slf4j
public class WebSocketService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

     public void sendNotification(String toUser,Message message){
         messagingTemplate.convertAndSendToUser(toUser,"/topic/notifications", message);
     }

}
