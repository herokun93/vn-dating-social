package vn.dating.app.social.dto;

import lombok.*;
import vn.dating.app.social.models.eenum.NotificationType;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDataDto  implements Serializable {
    private Long id;
    private String content;
    private String url;
    private Instant createdAt;
    private Instant updatedAt;
    private NotificationType type;
}
