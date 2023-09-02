package vn.dating.app.social.models;

import lombok.*;
import vn.dating.common.models.audit.DateAudit;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@Setter
@Getter
@Builder
public class NotifyActor extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notify_entity_id", nullable = false)
    private NotifyEntity notifyEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id", nullable = false)
    private User source;

    private boolean status;

    public NotifyActor() {
        this.setCreatedAt(Instant.now());
        this.setUpdatedAt(Instant.now());
    }
}
