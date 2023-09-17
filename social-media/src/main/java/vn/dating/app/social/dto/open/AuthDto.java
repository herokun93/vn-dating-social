package vn.dating.app.social.dto.open;

import lombok.*;
import vn.dating.app.social.models.User;
import vn.dating.app.social.utils.UtilsAvatar;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AuthDto {
    private String id;
    private String username;
    private String avatar;

    public AuthDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.avatar = UtilsAvatar.toPath(user.getAvatar()) ;
    }
}
