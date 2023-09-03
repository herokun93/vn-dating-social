package vn.dating.app.social.dto.community;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import vn.dating.app.social.models.User;
import vn.dating.app.social.models.eenum.UserCommunityRoleType;
import vn.dating.app.social.models.eenum.UserCommunityType;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class CommunityUserResultDto {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private UserCommunityType type;
    private UserCommunityRoleType role;

    public CommunityUserResultDto(String id, String username, String firstName, String lastName, String email, UserCommunityType type, UserCommunityRoleType role) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.type = type;
        this.role = role;
    }

    public CommunityUserResultDto() {
    }

//    public static CommunityUserResultDto fromEntity(User user) {
//        CommunityUserResultDto userResultDto = new CommunityUserResultDto();
//
//        userResultDto.setId(user.getId());
//        userResultDto.setUsername(user.getUsername());
//        userResultDto.setEmail(user.getEmail());
//        userResultDto.setFirstName(user.getFirstName());
//        userResultDto.setLastName(user.getLastName());
//
//        return userResultDto;
//    }
//    public static List<CommunityUserResultDto> fromEntities(List<User> users) {
//        return users.stream()
//                .map(CommunityUserResultDto::fromEntity)
//                .collect(Collectors.toList());
//    }
}
