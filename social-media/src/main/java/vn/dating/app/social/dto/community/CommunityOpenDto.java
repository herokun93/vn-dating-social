package vn.dating.app.social.dto.community;

import lombok.*;
import org.springframework.data.domain.Page;
import vn.dating.app.social.models.Community;
import vn.dating.app.social.models.Post;
import vn.dating.app.social.models.eenum.UserCommunityRoleType;
import vn.dating.app.social.models.eenum.UserCommunityType;
import vn.dating.app.social.utils.UtilsAvatar;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommunityOpenDto {

    private CommunityBaseDto community;
    private UserCommunityRoleType role;
    private UserCommunityType status;
    private int page;
    private int size;
    private int totalPages;
    private long totalElements;
    private boolean last;
    private List<CommunityPostHeaderResultDto> data = new ArrayList<>();

    public CommunityOpenDto(Community community,Page<Post> postPage){

        CommunityBaseDto communityBaseDto = new CommunityBaseDto();

        String communityName = community.getName();

        if(communityName.contains("news") || communityName.contains("relax") ||
                communityName.contains("ask") || communityName.contains("drama") ) {
        }


        communityBaseDto.setType(community.getType());
        communityBaseDto.setName(communityName);
        communityBaseDto.setAvatar(UtilsAvatar.toPath(community.getAvatar()));
        communityBaseDto.setDescription(community.getDescription());


        this.page = postPage.getNumber();
        this.totalPages=postPage.getTotalPages();
        this.size=postPage.getSize();
        this.last=postPage.isLast();
        this.totalElements = postPage.getTotalElements();
        this.data  = CommunityPostHeaderResultDto.fromEntities(postPage.stream().toList());
        this.community = communityBaseDto;

    }
}
