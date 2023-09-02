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
public class LikeNewReplyDto extends DateAudit implements Serializable {
    private Long replyId;
}
