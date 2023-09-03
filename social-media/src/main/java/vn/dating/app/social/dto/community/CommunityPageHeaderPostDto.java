package vn.dating.app.social.dto.community;

import lombok.*;
import org.springframework.data.domain.Page;
import vn.dating.app.social.models.Post;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommunityPageHeaderPostDto {
    private int page;
    private int size;
    private int totalPages;
    private long totalElements;
    private boolean last;
    private List<CommunityHeaderResultDto> data = new ArrayList<>();

    public CommunityPageHeaderPostDto(Page<Post> postPage){

        this.page = postPage.getNumber();
        this.totalPages=postPage.getTotalPages();
        this.size=postPage.getSize();
        this.last=postPage.isLast();
        this.totalElements = postPage.getTotalElements();
        this.data  = CommunityHeaderResultDto.fromEntities(postPage.stream().toList());

    }
}
