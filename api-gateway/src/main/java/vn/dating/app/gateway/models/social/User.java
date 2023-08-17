package vn.dating.app.gateway.models.social;

import lombok.*;
import org.keycloak.representations.idm.UserRepresentation;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id", nullable = false)
    private String id;


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


    @Column(name = "online")
    private boolean online;


    @Column(name = "last_online")
    private Instant lastOnline;


    @Column(name = "url",nullable = false)
    private String url;

    @Column(name = "verify")
    private String verify;

    @Column(name = "verify_exp")
    private Instant verifyExp;

    public User(UserRepresentation userRepresentation,String verify){
        this.id = userRepresentation.getId();
        this.username = userRepresentation.getUsername();
        this.firstName = userRepresentation.getFirstName();
        this.lastName = userRepresentation.getLastName();
        this.lastName = userRepresentation.getLastName();
        this.email = userRepresentation.getEmail();
        this.verify = verify;
        this.verifyExp = Instant.now().plusSeconds(6000);
    }


    @PrePersist
    public void prePersist() {
        this.url = UUID.randomUUID().toString();
    }


}
