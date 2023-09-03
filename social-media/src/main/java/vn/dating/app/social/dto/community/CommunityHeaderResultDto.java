package vn.dating.app.social.dto.community;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import vn.dating.app.social.models.Community;
import vn.dating.app.social.models.Post;
import vn.dating.app.social.models.User;
import vn.dating.app.social.models.eenum.UserCommunityType;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CommunityHeaderResultDto {
    private String username;
    private String userId;

    private String community;

    private String title;
    private String url;
    private int cComments;
    private int cLikes;
    boolean anonymous;
    private Instant createdAt;
    private Instant updatedAt;

    public static CommunityHeaderResultDto fromEntity(Post post){
        CommunityHeaderResultDto communityHeaderResultDto = new CommunityHeaderResultDto();
        Community getCommunity = post.getCommunity();
        User creator = post.getAuthor();

        communityHeaderResultDto.setUsername(creator.getUsername());
        communityHeaderResultDto.setUserId(creator.getId());
        communityHeaderResultDto.setCommunity(getCommunity.getName());
        communityHeaderResultDto.setTitle(post.getTitle());
        communityHeaderResultDto.setUrl(post.getUrl());
        communityHeaderResultDto.setAnonymous(post.isAnonymous());
        communityHeaderResultDto.setCreatedAt(post.getCreatedAt());
        communityHeaderResultDto.setUpdatedAt(post.getUpdatedAt());

        return communityHeaderResultDto;

    }

    public static List<CommunityHeaderResultDto> fromEntities(List<Post> posts) {
        return posts.stream()
                .map(CommunityHeaderResultDto::fromEntity)
                .collect(Collectors.toList());
    }

}
