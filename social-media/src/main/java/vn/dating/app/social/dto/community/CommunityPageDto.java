package vn.dating.app.social.dto.community;

import lombok.*;
import org.springframework.data.domain.Page;
import vn.dating.app.social.models.Community;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommunityPageDto implements Serializable {
    private int page;
    private int size;
    private int totalPages;
    private long totalElements;
    private boolean last;
    private List<CommunityResultDto> data = new ArrayList<>();

    public CommunityPageDto(Page<Community> communityPage){

        this.page = communityPage.getNumber();
        this.totalPages=communityPage.getTotalPages();
        this.size=communityPage.getSize();
        this.last=communityPage.isLast();
        this.totalElements = communityPage.getTotalElements();

        List<CommunityResultDto> communityResultDtoList = CommunityResultDto.fromEntities(communityPage.stream().toList());

        this.data = communityResultDtoList;

    }
}
