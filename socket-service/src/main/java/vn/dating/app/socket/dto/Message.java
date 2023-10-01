package vn.dating.app.socket.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class Message {
    private String name;
    private String message;
}
