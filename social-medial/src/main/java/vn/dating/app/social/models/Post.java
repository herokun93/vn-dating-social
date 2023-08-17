package vn.dating.app.social.models;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import vn.dating.app.social.models.eenum.PostStatus;
import vn.dating.app.social.models.eenum.PostType;
import vn.dating.common.models.audit.DateAudit;

import javax.persistence.Entity;
import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Post extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String title;

    private Boolean delete;

    @Column(nullable = false)
    private String url;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    @Fetch(FetchMode.SELECT)
    private User author;

    @OneToMany(mappedBy = "post",fetch = FetchType.EAGER)
    private Set<Comment> comments;

    @OneToMany(mappedBy = "post",fetch = FetchType.EAGER)
    private Set<Media> media;


    @OneToMany(mappedBy = "post",fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private Set<Like> likes;

    @Enumerated(EnumType.STRING)
    private PostType type;

    @Enumerated(EnumType.STRING)
    private PostStatus state;

    @PrePersist
    public void prePersist() {
        this.url = UUID.randomUUID().toString();
    }

}