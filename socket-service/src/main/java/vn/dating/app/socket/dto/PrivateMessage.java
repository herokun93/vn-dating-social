package vn.dating.app.socket.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class PrivateMessage {
    private String content;
    private String senderId;
    private String recipientId;
}

// You can access the sender's ID using message.getSenderId()
// You can access the recipient's ID using message.getRecipientId()