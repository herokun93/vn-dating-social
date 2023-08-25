package vn.dating.app.social.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import vn.dating.app.gateway.utils.UserCustom;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AuthService {

    private boolean checkEmailVerify = false;

    public boolean checkRole(JwtAuthenticationToken authenticationToken,String role){
        Jwt jwt = authenticationToken.getToken();

        List<String> realmAccess = jwt.getClaimAsStringList("realm_access");
        List<String> roles = null;
        if (realmAccess != null && !realmAccess.isEmpty()) {
            String realmAccessJson = realmAccess.get(0);
            try {
                JsonNode realmAccessNode = new ObjectMapper().readTree(realmAccessJson);
                JsonNode rolesNode = realmAccessNode.get("roles");
                if (rolesNode != null && rolesNode.isArray() && rolesNode.size() > 0) {
                    roles = new ArrayList<>();
                    for (JsonNode roleNode : rolesNode) {
                        roles.add(roleNode.asText());
                    }
                }
            } catch (IOException e) {
//                log.error("Failed to parse realm_access claim", e);
            }
        }

        if (roles != null) {
            if (roles.contains(role)) return true;

        }
        return false;
    }

    public boolean isRoleUser(JwtAuthenticationToken authenticationToken){
        return  checkRole(authenticationToken,"user");
    }

    private List<String> roles(JwtAuthenticationToken authenticationToken){

        Jwt jwt = authenticationToken.getToken();

        List<String> realmAccess = jwt.getClaimAsStringList("realm_access");
        List<String> roles = null;
        if (realmAccess != null && !realmAccess.isEmpty()) {
            String realmAccessJson = realmAccess.get(0);
            try {
                JsonNode realmAccessNode = new ObjectMapper().readTree(realmAccessJson);
                JsonNode rolesNode = realmAccessNode.get("roles");
                if (rolesNode != null && rolesNode.isArray() && rolesNode.size() > 0) {
                    roles = new ArrayList<>();
                    for (JsonNode roleNode : rolesNode) {
                        roles.add(roleNode.asText());
                    }
                }
            } catch (IOException e) {
//                log.error("Failed to parse realm_access claim", e);
            }
        }

        String emailVerify = jwt.getClaimAsString("email_verified");
        if(emailVerify.equals("true")) checkEmailVerify= true;


        if (roles != null) {
            return  roles;

        }
        return new ArrayList<>();
    }

    public String getUserId(JwtAuthenticationToken authenticationToken){
        Jwt jwt = authenticationToken.getToken();
        return  jwt.getSubject();
    }

    public UserCustom getUserCustom(JwtAuthenticationToken authenticationToken){

        UserCustom userCustom = new UserCustom();

        userCustom.setId(getUserId(authenticationToken));
        userCustom.setRoles(roles(authenticationToken));
        userCustom.setVerify(checkEmailVerify);
        return  userCustom;

    }

}