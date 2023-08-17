package vn.dating.app.social.models;

import lombok.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@Data
@NoArgsConstructor
public class User {
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



    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @OneToMany(mappedBy = "followed", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Follower> followers;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Post> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Like> likes;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Notify> notifies;

    @PrePersist
    public void prePersist() {
        this.url = UUID.randomUUID().toString();
    }


}
