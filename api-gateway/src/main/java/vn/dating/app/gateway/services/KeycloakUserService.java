package vn.dating.app.gateway.services;


import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.admin.client.token.TokenManager;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import vn.dating.app.gateway.dto.AuthRefreshToken;
import vn.dating.common.dto.CreateUserDto;
import vn.dating.app.gateway.dto.LoginDto;
import vn.dating.app.gateway.dto.UserBaseResult;
import vn.dating.app.gateway.models.social.User;


import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.SecureRandom;
import java.util.*;

//@Service
//@Slf4j
//public class KeycloakUserService {
//
//    @Value("${keycloak.auth-server-url}")
//    private String authServerUrl;
//
//    @Value("${keycloak.realm}")
//    private String realm;
//
//    @Value("${keycloak.resource}")
//    private String clientId;
//
//    @Value("${keycloak.credentials.secret}")
//    private String clientSecret;
//
//    @Autowired
//    private AuthService authService;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private Keycloak keycloak;
//
////    @Value("${keycloak.admin.username}")
//    private String adminUsername ="admin";
//
////    @Value("${keycloak.admin.password}")
//    private String adminPassword ="admin";
//
//    public UserRepresentation createUser(CreateUserDto createUserDto) {
//
//        log.info(createUserDto.toString());
//
//        UsersResource usersResource = keycloak.realm(realm).users();
//
//        UserRepresentation user = new UserRepresentation();
//
//        user.setUsername(createUserDto.getUsername());
//        user.setEmail(createUserDto.getEmail());
//        user.setFirstName(createUserDto.getFirstName());
//        user.setLastName(createUserDto.getLastName());
//        user.setEmail(createUserDto.getEmail());
//
//        user.setEnabled(true);
//
//        CredentialRepresentation credential = new CredentialRepresentation();
//        credential.setType(CredentialRepresentation.PASSWORD);
////        credential.setValue(createUserDto.getPassword());
//
//
//
//        user.setCredentials(Arrays.asList(credential));
//        Response response = usersResource.create(user);
//
//        if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {
//            log.info("Cannot create new user at Keycloak");
//
//            return null;
//        }
//
//
//        String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
//        user.setId(userId);
//
//
//        LoginDto loginDto = new LoginDto();
////        loginDto.setPassword(createUserDto.getPassword());
//        loginDto.setUsername(createUserDto.getUsername());
//
//        AccessTokenResponse accessTokenResponse = login(loginDto);
//        if(accessTokenResponse==null) return null;
//
//        UserBaseResult userBaseResult = new UserBaseResult(user);
//        String jsonPayload = convertUserBaseResultToJson(userBaseResult);
//        createUserSocial(jsonPayload,accessTokenResponse.getToken());
//
//        return user;
//    }
//
//
//
//    public boolean createShareUser(CreateUserDto createUserDto) {
//
//
//        UsersResource usersResource = keycloak.realm(realm).users();
//
//        UserRepresentation keyCloakUser = new UserRepresentation();
//        keyCloakUser.setUsername(createUserDto.getUsername());
//        keyCloakUser.setEmail(createUserDto.getEmail());
//        keyCloakUser.setFirstName(createUserDto.getFirstName());
//        keyCloakUser.setLastName(createUserDto.getLastName());
//        keyCloakUser.setEmail(createUserDto.getEmail());
//
//        keyCloakUser.setEnabled(true);
//
//
//        CredentialRepresentation credential = new CredentialRepresentation();
//        credential.setType(CredentialRepresentation.PASSWORD);
////        credential.setValue(createUserDto.getPassword());
//
//        keyCloakUser.setCredentials(Collections.singletonList(credential));
//
//
//
//
//        Response response = usersResource.create(keyCloakUser);
//
//        if (response.getStatus() != Response.Status.CREATED.getStatusCode()) return false;
//
//        String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
//
////        setRoleForUser(userId,"user");
//
//        keyCloakUser.setId(userId);
//
//
//
//
//
//
//        User newUser  = userService.createUserToSocial(keyCloakUser,generateVerificationToken());
//
//        if(newUser ==null){
//            return false;
//        }
//        return true;
//    }
//
//    public void setRoleForUser(String userId, String roleName) {
//        Keycloak keycloak = KeycloakBuilder.builder()
//                .serverUrl("http://localhost:8181/auth")
//                .realm(realm)
//                .clientId(clientId)
//                .clientSecret(clientSecret)
//                .username("admin")
//                .password("admin")
//                .build();
//
//        RealmResource realmResource = keycloak.realm(realm);
//        UserResource userResource = realmResource.users().get(userId);
//
//        RoleRepresentation roleRepresentation = realmResource.roles().get(roleName).toRepresentation();
//
//        userResource.roles().realmLevel().add(Collections.singletonList(roleRepresentation));
//    }
//
//    public void initiateEmailVerification(String userId) {
//        Keycloak keycloak = KeycloakBuilder.builder()
//                .serverUrl(authServerUrl)
//                .realm(realm)
//                .clientId(clientId)
//                .clientSecret(clientSecret)
//                .username(adminUsername)
//                .password(adminPassword)
//                .build();
//
//        UserRepresentation user = keycloak.realm(realm).users().get(userId).toRepresentation();
//        user.setEmailVerified(false);
//
//        keycloak.realm(realm).users().get(userId).update(user);
//    }
//
//
//    public String generateVerificationToken() {
//        // Set the desired length of the verification token
//        int tokenLength = 128;
//
//        // Define the character set to be used for the token
//        String characterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";
//
//        // Create a byte array to hold the random bytes
//        byte[] randomBytes = new byte[tokenLength];
//
//        // Generate random bytes using SecureRandom
//        SecureRandom secureRandom = new SecureRandom();
//        secureRandom.nextBytes(randomBytes);
//
//        // Encode the random bytes using Base64 encoding
//        byte[] base64Bytes = Base64.getUrlEncoder().withoutPadding().encode(randomBytes);
//
//        // Convert the encoded bytes to a string using the defined character set
//        StringBuilder verificationToken = new StringBuilder();
//        for (byte base64Byte : base64Bytes) {
//            int index = Math.floorMod(base64Byte, characterSet.length());
//            verificationToken.append(characterSet.charAt(index));
//        }
//
//        return verificationToken.toString();
//    }
//
//
//
//    public AccessTokenResponse login(LoginDto loginDto) {
//        AccessTokenResponse response = null;
//        try {
//            Keycloak ikeycloak = KeycloakBuilder.builder()
//                    .serverUrl(authServerUrl)
//                    .realm(realm)
//                    .clientId(clientId)
//                    .clientSecret(clientSecret)
//                    .username(loginDto.getUsername())
//                    .password(loginDto.getPassword())
//                    .grantType(OAuth2Constants.PASSWORD)
//                    .build();
//            TokenManager tokenManager = ikeycloak.tokenManager();
//
//            response = tokenManager.getAccessToken();
//
//        } catch (Exception e) {
////            new ResourceNotFoundException("User", "Email", "dang thang");
//            log.error("Server error: " + e.getMessage());
//
//        }
//        return response;
//    }
//
//
//
//    public boolean createUserSocial(String jsonPayload, String accessToken){
//        log.info("createUserSocial(String jsonPayload, String accessToken)");
//        String url ="http://localhost:1994/api/v1/social/users/create";
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(org.springframework.http.MediaType.valueOf(MediaType.APPLICATION_JSON));
//        headers.set("Authorization", "Bearer " + accessToken);
//        HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);
//        try {
//            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
//            String responseBody = response.getBody();
//            return true;
//            // Process response as needed
//        } catch (Exception e) {
//           // e.printStackTrace();
//            // Handle exception as needed
//            return false;
//        }
//    }
//
//    public String convertUserBaseResultToJson(UserBaseResult user) {
//        Gson gson = new Gson();
//        String jsonPayload = gson.toJson(user);
//        return jsonPayload;
//    }
//
//    public AccessTokenResponse getNewAccessToken(AuthRefreshToken authRefreshToken) {
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(org.springframework.http.MediaType.valueOf(MediaType.APPLICATION_FORM_URLENCODED));
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("grant_type", "refresh_token");
//        body.add("client_id", clientId);
//        body.add("client_secret", clientSecret);
//        body.add("refresh_token", authRefreshToken.getRefreshToken());
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
//        try {
//        AccessTokenResponse response = restTemplate.postForObject(authServerUrl +"/realms/" + realm +"/protocol/openid-connect/token", request, AccessTokenResponse.class);
//        return  response;
//        } catch (Exception e) {
////            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public String getUserIdByToken(){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (!(authentication instanceof AnonymousAuthenticationToken)) {
//            String currentUserName = authentication.getName();
//            return currentUserName;
//        }else {
//            return null;
//        }
//    }
//
//
//}
