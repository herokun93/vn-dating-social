package vn.dating.app.gateway.models.social;

import lombok.*;
import vn.dating.common.dto.CreateUserDto;

import javax.persistence.*;
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
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private boolean online;
    private Instant lastOnline;
    private String avatar;
    private String url;
    public User(CreateUserDto createUserDto){
        this.id = createUserDto.getId();
        this.username = createUserDto.getUsername();
        this.firstName = createUserDto.getFirstName();
        this.lastName = createUserDto.getLastName();
        this.lastName = createUserDto.getLastName();
        this.email = createUserDto.getEmail();
    }


    @PrePersist
    public void prePersist() {
        this.url = UUID.randomUUID().toString();
    }


}
