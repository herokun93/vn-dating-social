package vn.dating.app.social.models;

import lombok.*;
import vn.dating.app.social.models.eenum.CommunityType;
import vn.dating.common.models.audit.DateAudit;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class Community extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private CommunityType type;

    private boolean approval;


    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;


    @OneToMany(mappedBy = "community")
    private Set<UserCommunity> members = new HashSet<>();

    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL)
    private Set<Post> posts = new HashSet<>();
}

