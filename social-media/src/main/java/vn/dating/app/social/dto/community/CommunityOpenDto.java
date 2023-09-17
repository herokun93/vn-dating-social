package vn.dating.app.social.dto.community;

import lombok.*;
import org.springframework.data.domain.Page;
import vn.dating.app.social.models.Community;
import vn.dating.app.social.models.Post;
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
    private int page;
    private int size;
    private int totalPages;
    private long totalElements;
    private boolean last;
    private List<CommunityPostHeaderResultDto> data = new ArrayList<>();

    public CommunityOpenDto(Community comm,Page<Post> postPage){

        CommunityBaseDto community = new CommunityBaseDto();

        String communityName = comm.getName();

        if(communityName.contains("news") || communityName.contains("relax") ||
                communityName.contains("ask") || communityName.contains("drama") ) {
        }


        community.setType(comm.getType());
        community.setName(communityName);
        community.setAvatar(UtilsAvatar.toPath(comm.getAvatar()));
        community.setDescription(comm.getDescription());


        this.page = postPage.getNumber();
        this.totalPages=postPage.getTotalPages();
        this.size=postPage.getSize();
        this.last=postPage.isLast();
        this.totalElements = postPage.getTotalElements();
        this.data  = CommunityPostHeaderResultDto.fromEntities(postPage.stream().toList());
        this.community = community;

    }
}
