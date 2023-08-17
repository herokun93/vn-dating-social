package vn.dating.app.social.models;

import lombok.*;

import vn.dating.app.social.models.User;
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
public class Follower extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followed_id")
    private User followed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id")
    private User follower;

    @JoinColumn(name = "accepted")
    private boolean accepted;
}
