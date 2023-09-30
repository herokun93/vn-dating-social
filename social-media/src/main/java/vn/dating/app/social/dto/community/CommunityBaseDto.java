package vn.dating.app.social.dto.community;

import lombok.*;
import vn.dating.app.social.models.eenum.CommunityType;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommunityBaseDto {
    private String name;
    private String avatar;
    private String description;
    private CommunityType type;
}
