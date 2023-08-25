package vn.dating.app.gateway.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import vn.dating.app.gateway.dto.AuthRefreshToken;
import vn.dating.app.gateway.dto.CreateUserDto;
import vn.dating.app.gateway.dto.LoginDto;
import vn.dating.app.gateway.services.AuthService;
import vn.dating.app.gateway.services.KeycloakUserService;
import vn.dating.app.gateway.services.UserService;
import vn.dating.app.gateway.utils.UserCustom;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


//@RestController
//@RequestMapping("/api/auth")
//@RequiredArgsConstructor
//@Slf4j
//public class AuthController {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private KeycloakUserService keycloakUserService;
//
//    @Autowired
//    private AuthService authService;
//
//    @PostMapping("/login")
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity login(@Valid @RequestBody LoginDto loginDto) {
//        AccessTokenResponse accessTokenResponse = keycloakUserService.login(loginDto);
//        if(accessTokenResponse ==null) return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//
//
//        UserCustom userCustom =  authService.decodeToken(accessTokenResponse.getToken());
//        if(!userCustom.isVerify())return  new ResponseEntity<>("Please verify",HttpStatus.BAD_REQUEST);
//
//        return new ResponseEntity<>( accessTokenResponse, HttpStatus.OK);
//    }
//
//    @PostMapping("/freshToken")
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity getFreshToken(@RequestBody AuthRefreshToken authRefreshToken) {
//
//        AccessTokenResponse response = keycloakUserService.getNewAccessToken(authRefreshToken);
//        if(response==null) return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//
//        return new ResponseEntity<>( response, HttpStatus.OK);
//    }
//
////    @PostMapping("/register")
////    public ResponseEntity register(@RequestBody CreateUserDto createUserDto){
////        UserRepresentation userRepresentation = keycloakUserService.createUser(createUserDto);
////        if(userRepresentation == null) return ResponseEntity.badRequest().build();
////        return  ResponseEntity.ok(userRepresentation);
////    }
//
//    @PostMapping("/register")
//    public ResponseEntity register(@RequestBody @Valid CreateUserDto createUserDto){
//
//        log.info("test register");
//
//
//        boolean userRepresentation = keycloakUserService.createShareUser(createUserDto);
//        if(userRepresentation == false) {
//            return  ResponseEntity.badRequest().body("");
//        }
//        return  ResponseEntity.ok("OK");
//    }
//
//
//
//    @GetMapping("/verify/{verify}")
//    public ResponseEntity sendVerificationEmail(@PathVariable("verify") String verify,JwtAuthenticationToken authenticationToken) {
//        log.info(verify);
//        UserCustom userCustom = authService.getUserCustom(authenticationToken);
//        if(!userCustom.isUser()) return ResponseEntity.badRequest().build();
//
//        // Generate a unique verification token
////        String verificationToken = generateVerificationToken();
//
//
//        // Generate the verification link with the token
////        String verificationLink = generateVerificationLink(userId, verificationToken);
//
//        // Redirect the user to the verification link
//        return  ResponseEntity.ok(keycloakUserService.generateVerificationToken());
//    }
//
//
//    @GetMapping("/current")
//    public ResponseEntity getCurrent(Principal principal, JwtAuthenticationToken accessToken){
//        if(principal == null){
//            return new ResponseEntity<>("Post not exist",HttpStatus.BAD_REQUEST);
//        }else{
//
//
//
//            accessToken.getToken().getClaims().forEach((e,data)->{
//                log.info(e.toString());
//                log.info(data.toString());
//            });
//
//            return  new ResponseEntity<>(principal.getName(),HttpStatus.OK);
//        }
//    }
//
//    @GetMapping("/public")
//    public ResponseEntity getPublic(){
//
//        return  new ResponseEntity<>(userService.getList(),HttpStatus.OK);
//
//    }
//
//    @PostMapping("/add")
//    public ResponseEntity addUser(@RequestBody  CreateUserDto createUserDto){
//
//        log.info(createUserDto.toString());
//
//        return  new ResponseEntity<>(createUserDto.toString(),HttpStatus.OK);
//
//    }
//
//    @GetMapping("/private")
//    public ResponseEntity getPrivate(JwtAuthenticationToken accessToken){
//        UserCustom userCustomByToken = authService.getUserCustom(accessToken,"user");
//
////        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
////        log.info(jwt.getTokenValue());
//        return  new ResponseEntity<>("Role user",HttpStatus.OK);
//    }
//    @PostMapping("/limit")
//    public ResponseEntity postLimit(@RequestBody  CreateUserDto createUserDto){
//        log.info(createUserDto.toString());
//        return  new ResponseEntity<>(createUserDto.toString(),HttpStatus.OK);
//    }
//}
