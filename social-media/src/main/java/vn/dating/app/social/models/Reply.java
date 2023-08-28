package vn.dating.app.social.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import vn.dating.common.models.audit.DateAudit;

import java.util.Set;



import javax.persistence.Entity;
import javax.persistence.*;

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

    private boolean anonymous;
}

