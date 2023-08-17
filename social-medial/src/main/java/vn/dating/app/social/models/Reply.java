package vn.dating.app.social.models;

import lombok.*;
import vn.dating.app.social.models.Comment;
import vn.dating.app.social.models.Like;
import vn.dating.app.social.models.User;
import vn.dating.common.models.audit.DateAudit;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Reply extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @OneToMany(mappedBy = "reply",fetch = FetchType.EAGER)
    private Set<Like> likes;



    @OneToOne(mappedBy = "reply")
    private Media media;

}

