package vn.dating.app.social.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import vn.dating.app.social.services.NotificationService;

@Controller
@Slf4j
public class NotificationSocket {
    @Autowired
    private NotificationService notificationService;

//    @MessageMapping("/notifications")
//    @SendToUser("topic/notifications")
//    public void sendMessageToGroup(NotificationDto notificationDto) {
//        log.info("notification");
//        String toUser ="";
//        notificationService.sendNotification(toUser,notificationDto);
//    }
}
