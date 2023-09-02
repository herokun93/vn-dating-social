package vn.dating.app.social.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import vn.dating.common.models.audit.DateAudit;

import javax.persistence.*;
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

    boolean anonymous;


    @OneToOne(mappedBy = "reply")
    private Media media;

}

