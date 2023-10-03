package vn.dating.app.socket.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;
import vn.dating.common.models.audit.DateAudit;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.HashSet;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class User extends DateAudit {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @NaturalId
    @NotBlank
    @Size(max = 40)
    @Email
    private String email;
    @NotBlank

    @Column(name = "username")
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;
    private String avatar;
    private boolean online;

    private Instant lastOnline = Instant.now();

}
