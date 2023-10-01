package vn.dating.app.socket.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import vn.dating.app.socket.dto.Message;
import vn.dating.app.socket.dto.PrivateMessage;
import vn.dating.app.socket.dto.PublicMessage;

@Controller
@Slf4j
public class WebSocketController {


    @MessageMapping("/public-messages") // This is the endpoint the client sends messages to
    @SendTo("/topic/public-messages")  // This is where the message will be sent to subscribers
    public PublicMessage handlePublicMessage(@Payload PublicMessage message, SimpMessageHeaderAccessor headerAccessor) {
        log.info("handlePublicMessage");
        log.info(headerAccessor.getSessionId());
        log.info(message.toString());
        return message;
    }

    @MessageMapping("/private-messages") // This is the endpoint for private messages
    @SendTo("/user/{recipientId}/private-messages") // This is where the message will be sent to the specific user
    public PrivateMessage handlePrivateMessage(@Payload PrivateMessage privateMessage, SimpMessageHeaderAccessor headerAccessor) {
        // You can access the message content using message.getContent()
        // You can access the sender's ID using message.getSenderId()
        // You can access the recipient's ID using message.getRecipientId()

        // You can also access session information using headerAccessor.getSessionId()

        // You can do any processing you need to do with the message here
        log.info("private-messages");
        log.info(privateMessage.toString());

        return privateMessage;
    }
}