package vn.dating.app.social.dto.post;

import lombok.*;

import vn.dating.app.social.dto.comment.PageCommentDetailDto;
import vn.dating.app.social.dto.comment.PageCommentDto;
import vn.dating.common.models.audit.DateAudit;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailDto extends DateAudit implements Serializable {
    private String content;
    private Long id;
    private String authorId;
    private String authorUsername;
    private String authorFirstName;
    private String authorLastName;
    private String url;
    private String title;
    private long countLike;
    private long countComment;
    private PageCommentDto comments = new PageCommentDto();
    private List<String> medias = new ArrayList<>();
    public void addMedial(String path){
        medias.add(path);
    }


}