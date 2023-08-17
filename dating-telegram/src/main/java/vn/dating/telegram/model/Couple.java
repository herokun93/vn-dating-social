package vn.dating.telegram.model;

import lombok.Getter;
import lombok.Setter;
import vn.dating.common.models.audit.DateAudit;

import javax.persistence.*;


@Entity
@Table(name = "Couple")
@Setter
@Getter
public class Couple extends DateAudit {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user1_id")
    private User user1;

    @OneToOne
    @JoinColumn(name = "user2_id")
    private User user2;

    @Enumerated(EnumType.STRING)
    private CoupleStatus status;


}
