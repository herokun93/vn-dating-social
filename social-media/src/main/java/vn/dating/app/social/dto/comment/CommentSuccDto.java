package vn.dating.app.social.dto.comment;

import lombok.*;
import vn.dating.app.social.models.Comment;

import java.time.Instant;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommentSuccDto {
    private Long id;
    private String content;

    boolean anonymous;
    private Instant createdAt;
    private Instant updatedAt;
    private String media="";

    public static CommentSuccDto fromEntity(Comment comment){
        CommentSuccDto commentSuccDto = new CommentSuccDto();

        commentSuccDto.setId(comment.getId());
        commentSuccDto.setContent(comment.getContent());
        commentSuccDto.setAnonymous(comment.isAnonymous());
        commentSuccDto.setCreatedAt(comment.getCreatedAt());
        commentSuccDto.setUpdatedAt(comment.getUpdatedAt());

        if(comment.getMedia()==null ){

        }else{
            if(comment.getMedia().getPath()!="" ||comment.getMedia().getPath()==null){

            }else{
                commentSuccDto.setMedia(comment.getMedia().getPath());
            }

        }


        return commentSuccDto;
    }
}
