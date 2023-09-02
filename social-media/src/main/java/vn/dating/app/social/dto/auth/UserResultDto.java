package vn.dating.app.social.dto.auth;

import lombok.*;
import vn.dating.app.social.dto.community.CommunityResultDto;
import vn.dating.app.social.models.Community;
import vn.dating.app.social.models.User;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserResultDto {
    protected String id;
    protected String username;
    protected String firstName;
    protected String lastName;
    protected String email;

    public static UserResultDto fromEntity(User user) {
        UserResultDto userResultDto = new UserResultDto();

        userResultDto.setId(user.getId());
        userResultDto.setUsername(user.getUsername());
        userResultDto.setEmail(user.getEmail());
        userResultDto.setFirstName(user.getFirstName());
        userResultDto.setLastName(user.getLastName());

        return userResultDto;
    }
    public static List<UserResultDto> fromEntities(List<User> users) {
        return users.stream()
                .map(UserResultDto::fromEntity)
                .collect(Collectors.toList());
    }
}
