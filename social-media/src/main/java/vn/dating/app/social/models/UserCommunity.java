package vn.dating.app.social.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import vn.dating.common.models.audit.DateAudit;

import javax.persistence.Entity;
import javax.persistence.*;


@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
public class UserCommunity extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "community_id")
    private Community community;

    private boolean active;

}
