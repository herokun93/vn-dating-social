package vn.dating.app.social.dto.comment;

import lombok.*;
import vn.dating.common.models.audit.DateAudit;

@Getter
@Setter
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto extends DateAudit {
    private Long id;
    private String content;
    private String userId;
    private String userUsername;
    private String userFirstName;
    private String userLastName;
    private long countLike;
    private long countReply;
    private Long postId;
    private String mediaPath="";
}
