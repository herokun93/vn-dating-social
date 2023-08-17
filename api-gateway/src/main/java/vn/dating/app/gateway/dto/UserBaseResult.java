package vn.dating.app.gateway.dto;

import lombok.*;
import org.keycloak.representations.idm.UserRepresentation;

@Getter
@Setter
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBaseResult {
    protected String id;
    protected String username;
    protected String firstName;
    protected String lastName;
    protected String email;

    public UserBaseResult(UserRepresentation userRepresentation){
        this.id = userRepresentation.getId();
        this.username = userRepresentation.getUsername();
        this.firstName = userRepresentation.getFirstName();
        this.lastName = userRepresentation.getLastName();
        this.lastName = userRepresentation.getLastName();
        this.email = userRepresentation.getEmail();
    }
}
