package vn.dating.app.social.models;

import lombok.*;
import org.hibernate.annotations.NaturalId;
import vn.dating.common.models.audit.DateAudit;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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


    @Column(name = "url",nullable = false)
    private String url;

    @OneToMany(mappedBy = "user")
    private Set<UserCommunity> communities = new HashSet<>();





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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Set<UserCommunity> getCommunities() {
        return communities;
    }

    public void setCommunities(Set<UserCommunity> communities) {
        this.communities = communities;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public List<Follower> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Follower> followers) {
        this.followers = followers;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public List<Notify> getNotifies() {
        return notifies;
    }

    public void setNotifies(List<Notify> notifies) {
        this.notifies = notifies;
    }
}
