package vn.dating.app.social.dto.open;

import lombok.*;
import vn.dating.app.social.dto.community.CommunityPageDto;
import vn.dating.app.social.dto.community.other.CommunitiesOtherDto;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OpenHomeDto {
    private AuthDto auth;
    private CommunityPageDto communities;
    private CommunitiesOtherDto communitiesOther;
}
