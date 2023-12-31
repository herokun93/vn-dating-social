package vn.dating.app.social.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import vn.dating.app.social.dto.ResponseMessage;
import vn.dating.app.social.dto.ResponseObject;
import vn.dating.app.social.dto.auth.UserBaseResult;
import vn.dating.app.social.models.User;
import vn.dating.app.social.services.AuthService;
import vn.dating.app.social.utils.UtilsAvatar;
import vn.dating.common.dto.CreateUserDto;
import vn.dating.common.response.CreateAuthResponse;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@Slf4j
@RestController
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/social/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/public")
    public String getPublic(){
        return "public";
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ResponseObject> getMe(Principal principal){

        User user = authService.getCurrentUserById(principal);

        UserBaseResult userBaseResult = new UserBaseResult();

        userBaseResult.setId(user.getId());
        userBaseResult.setAvatar(UtilsAvatar.toPath(user.getAvatar()));
        userBaseResult.setUsername(user.getUsername());

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", ResponseMessage.SUCCESSFUL,userBaseResult)
        );
    }

//    @PostMapping("/create")
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity<ResponseObject> createUser(@Valid @RequestBody CreateUserDto createUserDto){
//
////        User saveUser = authService.createUserSocial(createUserDto);
////        if(saveUser==null)   return ResponseEntity.status(HttpStatus.OK).body(
////                new ResponseObject("OK", ResponseMessage.SUCCESSFUL,userBaseResult)
////        );
//        return ResponseEntity.status(HttpStatus.OK).body(
//                new ResponseObject("OK", ResponseMessage.SUCCESSFUL,createUserDto));
//
////        return ResponseEntity.ok().body(new CreateAuthResponse("User created, data saved, and API called", "SOCIAL"));
//    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ResponseObject> createUser(@RequestBody CreateUserDto createUserDto){

        Optional<User> getUser = authService.getUserById(createUserDto.getId());
        if(getUser.isEmpty()){

            User saveUser = authService.createUserSocial(createUserDto);

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", ResponseMessage.SUCCESSFUL,createUserDto));
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", ResponseMessage.EXISTS,createUserDto));

    }
}
