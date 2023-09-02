package vn.dating.app.social.dto.reply;

import lombok.*;
import vn.dating.common.models.audit.DateAudit;

import java.io.Serializable;

@Getter
@Setter
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyDto extends DateAudit implements Serializable {

    private Long id;
    private Long commentId;
    private String content;
    private String userId;
    private String userUsername;
    private String userFirstName;
    private String userLastName;
    private long countLike;
    private String mediaPath;
//    private List<LikePostDto> likes;
}
