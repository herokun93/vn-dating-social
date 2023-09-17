package vn.dating.app.social.dto.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import vn.dating.app.social.dto.community.CreateByDto;
import vn.dating.app.social.models.Comment;
import vn.dating.app.social.models.User;
import vn.dating.app.social.utils.UtilsAvatar;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CommentDetails {
    private CreateByDto createBy;
    boolean anonymous;
    private int cReplies;
    private int cLikes;

    private String content;
    private Instant createdAt;
    private Instant updatedAt;

    public static  CommentDetails fromEntity(Comment comment){
        CommentDetails commentDetails = new CommentDetails();
        boolean anonymous = comment.isAnonymous();

        User creator = comment.getUser();
        CreateByDto createByDto = new CreateByDto();

        if(anonymous){

        }else{
            createByDto.setId(creator.getId());
            createByDto.setUsername(creator.getUsername());
            createByDto.setAvatar(UtilsAvatar.toPath(creator.getAvatar()));
        }
        commentDetails.setAnonymous(comment.isAnonymous());
        commentDetails.setCreatedAt(comment.getCreatedAt());
        commentDetails.setUpdatedAt(comment.getUpdatedAt());
        commentDetails.setContent(comment.getContent());
        commentDetails.setCLikes(comment.getLikes().size());
        commentDetails.setCReplies(comment.getReplies().size());

        commentDetails.setCreateBy(createByDto);

        return commentDetails;
    }

    public static List<CommentDetails> fromEntities(List<Comment> comments) {
        return comments.stream()
                .map(CommentDetails::fromEntity)
                .collect(Collectors.toList());
    }
}
