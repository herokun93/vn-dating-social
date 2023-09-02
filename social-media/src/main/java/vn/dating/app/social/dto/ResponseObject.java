package vn.dating.app.social.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ResponseObject {
    private String status;

    private ResponseMessage message;
    private Object data;

    public ResponseObject() {
    }

    public ResponseObject(String status, ResponseMessage message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

}
