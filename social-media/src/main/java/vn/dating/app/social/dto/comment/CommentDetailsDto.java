package vn.dating.app.social.dto.comment;

import lombok.*;
import vn.dating.app.social.dto.reply.ReplyDto;
import vn.dating.common.models.audit.DateAudit;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommentDetailsDto extends DateAudit implements Serializable {
    private Long id;
    private String content;
    private String userId;
    private String userUsername;
    private String userFirstName;
    private String userLastName;
    private List<ReplyDto> replies;
    private long countLike;
    private long countReply;
    private String mediaPath;
    private Long postId;
}
