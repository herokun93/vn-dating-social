package vn.dating.app.social.dto;

import lombok.*;
import vn.dating.common.models.audit.DateAudit;

import java.io.Serializable;

@Getter
@Setter
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowerNewDto extends DateAudit implements Serializable {
    private Long followedId;
    private Long followerId;
}
