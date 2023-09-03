package vn.dating.app.social.dto.community;

import lombok.*;
import org.springframework.data.domain.Page;
import vn.dating.app.social.dto.auth.UserResultDto;
import vn.dating.app.social.models.Community;
import vn.dating.app.social.models.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommunityPageMemberDto implements Serializable {
    private int page;
    private int size;
    private int totalPages;
    private long totalElements;
    private boolean last;
    private List<CommunityUserResultDto> data = new ArrayList<>();

//    public CommunityPageMemberDto(Page<User> users){
//
//        this.page = users.getNumber();
//        this.totalPages=users.getTotalPages();
//        this.size=users.getSize();
//        this.last=users.isLast();
//        this.totalElements = users.getTotalElements();
//
//        List<UserResultDto> userResultDtos = UserResultDto.fromEntities(users.stream().toList());
//
//        this.data = userResultDtos;
//
//    }
    public CommunityPageMemberDto(Page<CommunityUserResultDto> userPage){

        this.page = userPage.getNumber();
        this.totalPages=userPage.getTotalPages();
        this.size=userPage.getSize();
        this.last=userPage.isLast();
        this.totalElements = userPage.getTotalElements();

        List<CommunityUserResultDto> userResultDtos = userPage.stream().toList();

        this.data = userResultDtos;

    }
}
