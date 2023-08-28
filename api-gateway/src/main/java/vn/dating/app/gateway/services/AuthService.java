package vn.dating.app.gateway.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import vn.dating.app.gateway.configs.exception.AuthenticationTokenForbidden;
import vn.dating.app.gateway.configs.exception.AuthenticationTokenMissingException;
import vn.dating.app.gateway.models.social.User;
import vn.dating.app.gateway.repositories.social.UserRepository;
import vn.dating.app.gateway.utils.UserCustom;
import vn.dating.common.dto.CreateUserDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AuthService {
//    private boolean checkEmailVerify = false;
//
//    private List<String> roles(JwtAuthenticationToken authenticationToken){
//        if(authenticationToken==null) throw new AuthenticationTokenMissingException("Authentication token is missing");
//        Jwt jwt = authenticationToken.getToken();
//
//
//        List<String> realmAccess = jwt.getClaimAsStringList("realm_access");
//        List<String> roles = null;
//        if (realmAccess != null && !realmAccess.isEmpty()) {
//            String realmAccessJson = realmAccess.get(0);
//            try {
//                JsonNode realmAccessNode = new ObjectMapper().readTree(realmAccessJson);
//                JsonNode rolesNode = realmAccessNode.get("roles");
//                if (rolesNode != null && rolesNode.isArray() && rolesNode.size() > 0) {
//                    roles = new ArrayList<>();
//                    for (JsonNode roleNode : rolesNode) {
//                        roles.add(roleNode.asText());
//                    }
//                }
//            } catch (IOException e) {
////                log.error("Failed to parse realm_access claim", e);
//            }
//        }
//
//        String emailVerify = jwt.getClaimAsString("email_verified");
//        if(emailVerify.equals("true")) checkEmailVerify= true;
//
//
//        if (roles != null) {
//            return  roles;
//
//        }
//        return new ArrayList<>();
//    }
//
//
//    public UserCustom decodeToken(String token) {
//        UserCustom userCustom = new UserCustom();
//        try {
//            SignedJWT signedJWT = SignedJWT.parse(token);
//            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
//
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode claimsNode = mapper.readTree(claimsSet.toPayload().toString());
//
//            JsonNode realmAccessNode = claimsNode.get("realm_access");
//            if (realmAccessNode != null) {
//
//                JsonNode rolesNode = realmAccessNode.get("roles");
//                if (rolesNode != null && rolesNode.isArray()) {
//                    for (JsonNode roleNode : rolesNode) {
//                        String role = roleNode.asText();
//                        userCustom.addRole(role);
//                    }
//                }
//            }
//
//            userCustom.setVerify(claimsNode.get("email_verified").asBoolean());
//            userCustom.setId(claimsNode.get("sid").asText());
//
////            log.info(userCustom.toString());
//
//
//
//            return userCustom;
//        } catch (java.text.ParseException e) {
//           return null;
//        } catch (JsonMappingException e) {
//            return null;
//
////            throw new RuntimeException(e);
//        } catch (JsonProcessingException e) {
//            return null;
////            throw new RuntimeException(e);
//        }
//    }
//
//    public String getUserId(JwtAuthenticationToken authenticationToken){
//        if(authenticationToken==null) throw new AuthenticationTokenMissingException("Authentication token is missing");
//        Jwt jwt = authenticationToken.getToken();
//        log.info(jwt.getExpiresAt().toString());
//        return  jwt.getSubject();
//    }
//
//
//
//
//
//    public UserCustom getUserCustom(JwtAuthenticationToken authenticationToken){
//
//        if(authenticationToken==null) {
//            throw new AuthenticationTokenMissingException("Authentication token is missing");
//
//        }
//
//        UserCustom userCustom = new UserCustom();
//        String userId = getUserId(authenticationToken);
//
//        List<String> roles = roles(authenticationToken);
//
//
//        userCustom.setId(userId);
//        userCustom.setRoles(roles);
//        userCustom.setVerify(checkEmailVerify);
//
//        return  userCustom;
//
//    }
//    public UserCustom getUserCustom(JwtAuthenticationToken authenticationToken,String role){
//
//        if(authenticationToken==null) throw new AuthenticationTokenMissingException("Authentication token is missing");
//
//
//
//        UserCustom userCustom = new UserCustom();
//        String userId = getUserId(authenticationToken);
//        List<String> roles = roles(authenticationToken);
//        userCustom.setId(userId);
//        userCustom.setRoles(roles);
//
//        if(!userCustom.isRole(role))  throw new AuthenticationTokenForbidden("You are not "+ role);
//
//        return  userCustom;
//
//    }

    @Autowired
    private UserRepository userRepository;



    public User createGatewayUser(CreateUserDto createUserDto){
        User user = new User(createUserDto);
        User saveUser = userRepository.save(user);;
        return  saveUser;
    }

    public void createSocialUser(CreateUserDto createUserDto){

    }




}