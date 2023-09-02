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
public class ReplyNewDto extends DateAudit implements Serializable {
    private String content;
    private Long commentId;
}
