package vn.dating.app.social.models;

import lombok.*;
import vn.dating.app.social.models.eenum.LikeModeType;
import vn.dating.app.social.models.eenum.ReactType;
import vn.dating.common.models.audit.DateAudit;

import javax.persistence.Entity;
import javax.persistence.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "likes")
public class Like  extends DateAudit {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_id")
    private Reply reply;

    @Enumerated(EnumType.STRING)
    private ReactType react;

    @Enumerated(EnumType.STRING)
    private LikeModeType type;
}
