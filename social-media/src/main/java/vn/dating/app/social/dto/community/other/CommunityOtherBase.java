package vn.dating.app.social.dto.community.other;

import lombok.*;
import vn.dating.app.social.models.Community;
import vn.dating.app.social.utils.UtilsAvatar;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommunityOtherBase{
    private String name;
    private String avatar;
    private String description;

    public CommunityOtherBase(Community community) {
        this.name =  community.getName();
        this.avatar = UtilsAvatar.toPath(community.getAvatar()) ;
        this.description =community.getDescription();
    }
}
