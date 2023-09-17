package vn.dating.app.social.dto.community;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import vn.dating.app.social.models.Community;
import vn.dating.app.social.models.Post;
import vn.dating.app.social.models.User;
import vn.dating.app.social.utils.UtilsAvatar;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CommunityPostHeaderResultDto {
    private CreateByDto createBy;
    boolean anonymous;
    private String community;

    private String title;
    private String url;
    private int cComments;
    private int cLikes;

    private String content;
    private Instant createdAt;
    private Instant updatedAt;

    public static CommunityPostHeaderResultDto fromEntity(Post post){

        CommunityPostHeaderResultDto postheaderResultDtoComunity = new CommunityPostHeaderResultDto();
        Community getCommunity = post.getCommunity();

        boolean anonymous = post.isAnonymous();

        User creator = post.getAuthor();
        CreateByDto createByDto = new CreateByDto();

        if(anonymous){

        }else{
            createByDto.setId(creator.getId());
            createByDto.setUsername(creator.getUsername());
            createByDto.setAvatar(UtilsAvatar.toPath(creator.getAvatar()));
        }






        postheaderResultDtoComunity.setCommunity(getCommunity.getName());

        postheaderResultDtoComunity.setTitle(post.getTitle());
        postheaderResultDtoComunity.setUrl(post.getUrl());
        postheaderResultDtoComunity.setAnonymous(post.isAnonymous());
        postheaderResultDtoComunity.setCreatedAt(post.getCreatedAt());
        postheaderResultDtoComunity.setUpdatedAt(post.getUpdatedAt());
        postheaderResultDtoComunity.setContent(post.getContent());

        postheaderResultDtoComunity.setCreateBy(createByDto);

        return postheaderResultDtoComunity;

    }

    public static List<CommunityPostHeaderResultDto> fromEntities(List<Post> posts) {
        return posts.stream()
                .map(CommunityPostHeaderResultDto::fromEntity)
                .collect(Collectors.toList());
    }

}
