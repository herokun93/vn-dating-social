package vn.dating.app.social.models;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import vn.dating.common.models.audit.DateAudit;

import javax.persistence.*;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Comment  extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToOne(mappedBy = "comment",fetch = FetchType.EAGER)
    @JoinColumn(name = "comment_id")
    private Media media;

    boolean anonymous;



    @OneToMany(mappedBy = "comment",fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private Set<Like> likes;

    @OneToMany(mappedBy = "comment",fetch = FetchType.EAGER)
//    @OrderColumn(name = "reply_id")
    @Fetch(FetchMode.SELECT)
    private Set<Reply> replies;
}
