package vn.dating.app.social.dto.post;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;
import vn.dating.app.social.dto.comment.CommentPageDetails;
import vn.dating.app.social.dto.community.ComunityPostHeaderResultDto;
import vn.dating.app.social.dto.community.CreateByDto;
import vn.dating.app.social.models.Comment;
import vn.dating.app.social.models.Community;
import vn.dating.app.social.models.Post;
import vn.dating.app.social.models.User;
import vn.dating.app.social.utils.UtilsAvatar;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PostDetailsDto {
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
    private CommentPageDetails comments = new CommentPageDetails();

    public static PostDetailsDto fromEntity(Post post, Page<Comment> commentPage){

        PostDetailsDto postDetailsDto = new PostDetailsDto();
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


        postDetailsDto.setCommunity(getCommunity.getName());

        postDetailsDto.setTitle(post.getTitle());
        postDetailsDto.setUrl(post.getUrl());
        postDetailsDto.setAnonymous(post.isAnonymous());
        postDetailsDto.setCreatedAt(post.getCreatedAt());
        postDetailsDto.setUpdatedAt(post.getUpdatedAt());
        postDetailsDto.setContent(post.getContent());

        postDetailsDto.setCreateBy(createByDto);
        postDetailsDto.setComments(new CommentPageDetails(commentPage));

        return postDetailsDto;

    }
}
