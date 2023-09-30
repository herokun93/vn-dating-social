package vn.dating.app.social.dto.auth;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserBaseResult {
    private String id;
    private String username;
//    private String firstName;
//    private String lastName;
//    private String email;
    private String avatar;
}
