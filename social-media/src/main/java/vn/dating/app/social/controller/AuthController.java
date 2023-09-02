package vn.dating.app.social.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.dating.app.social.models.User;
import vn.dating.app.social.services.AuthService;
import vn.dating.common.dto.CreateUserDto;
import vn.dating.common.response.CreateAuthResponse;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/social/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/public")
    @ResponseStatus(HttpStatus.OK)
    public String getPublic(){
        return "public";
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CreateAuthResponse> createUser(@Valid @RequestBody CreateUserDto createUserDto){

        User saveUser = authService.createUserSocial(createUserDto);
        if(saveUser==null)   return ResponseEntity.badRequest().body(new CreateAuthResponse("Cannot save user", "SOCIAL"));

        return ResponseEntity.ok().body(new CreateAuthResponse("User created, data saved, and API called", "SOCIAL"));
    }
}
