package vn.dating.app.social.dto.auth;

import lombok.*;

@Getter
@Setter
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBaseResult {
    protected String id;
    protected String username;
    protected String firstName;
    protected String lastName;
    protected String email;
}
