package vn.dating.app.social.dto.community;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateByDto {
    private String id;
    private String username;
    private String avatar;

    public CreateByDto() {
        this.id = "anonymous";
        this.username = "anonymous";
        this.avatar = "anonymous";
    }
}
