package vn.dating.app.social.dto.like;

import lombok.*;
import vn.dating.common.models.audit.DateAudit;

import java.io.Serializable;

@Getter
@Setter
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeCommentDto extends DateAudit implements Serializable {
    private Long id;
    private Long commentId;
    private String userId;
    private String userUsername;
    private String userFistName;
    private String userLastName;
}
