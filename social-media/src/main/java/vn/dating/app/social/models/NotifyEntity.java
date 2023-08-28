package vn.dating.app.social.models;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import vn.dating.app.social.models.eenum.NotificationType;
import vn.dating.common.models.audit.DateAudit;


import javax.persistence.Entity;
import javax.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@Setter
@Getter
@Builder
public class NotifyEntity extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private NotificationType type;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    @Fetch(FetchMode.SELECT)
    private User author;





    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "like_id")
    private Like like;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_id")
    private Reply reply;

    @OneToMany(mappedBy = "notifyEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Notify> notifies;

    @OneToMany(mappedBy = "notifyEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<NotifyActor> notifyActors;

    private boolean status;
    private String content;

    public NotifyEntity() {
        this.setCreatedAt(Instant.now());
        this.setUpdatedAt(Instant.now());
    }
}
