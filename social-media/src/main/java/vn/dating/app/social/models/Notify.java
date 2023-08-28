package vn.dating.app.social.models;

import lombok.*;
import vn.dating.common.models.audit.DateAudit;


import javax.persistence.Entity;
import javax.persistence.*;
import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Notify extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notify_entity_id", nullable = false)
    private NotifyEntity notifyEntity;

    private boolean status;


    public Notify() {
        this.setCreatedAt(Instant.now());
        this.setUpdatedAt(Instant.now());
    }
}
