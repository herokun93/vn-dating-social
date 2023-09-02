package vn.dating.app.social.models;

import lombok.*;
import vn.dating.common.models.audit.DateAudit;

import javax.persistence.*;


@Entity
@Table
@Getter
@Setter
@ToString
@Data
@NoArgsConstructor
public class Profile extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String bio;

    @OneToOne(mappedBy = "profile",fetch = FetchType.LAZY)
    private User user;


}