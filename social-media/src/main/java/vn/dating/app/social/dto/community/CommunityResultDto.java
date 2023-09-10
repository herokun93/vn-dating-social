package vn.dating.app.social.dto.community;

import lombok.*;
import vn.dating.app.social.models.Community;
import vn.dating.app.social.models.eenum.CommunityType;
import vn.dating.app.social.utils.UtilsAvatar;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CommunityResultDto {
    private Long id;
    private String name;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
    private String avatar;
    private CommunityType type;
    private boolean approval;

    public static CommunityResultDto fromEntity(Community community) {
        CommunityResultDto communityResultDto = new CommunityResultDto();
        communityResultDto.setId(community.getId());
        communityResultDto.setName(community.getName());
        communityResultDto.setDescription(community.getDescription());
        communityResultDto.setCreatedAt(community.getCreatedAt());
        communityResultDto.setUpdatedAt(community.getUpdatedAt());
        communityResultDto.setType(community.getType());
        communityResultDto.setApproval(community.isApproval());
        communityResultDto.setAvatar(UtilsAvatar.toPath(community.getAvatar()));
        return communityResultDto;
    }
    public static List<CommunityResultDto> fromEntities(List<Community> communities) {
        return communities.stream()
                .map(CommunityResultDto::fromEntity)
                .collect(Collectors.toList());
    }
}
