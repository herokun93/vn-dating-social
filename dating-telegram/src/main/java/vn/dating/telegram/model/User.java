package vn.dating.telegram.model;




import lombok.Getter;
import lombok.Setter;
import vn.dating.common.models.audit.DateAudit;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Setter
@Getter
public class User extends DateAudit {

    @Id
    private Long chatId;
    private String firstName;
    private String lastName;
    private String username;

    @OneToOne(mappedBy = "user1")
    private Couple couple1;

    @OneToOne(mappedBy = "user2")
    private Couple couple2;



}
