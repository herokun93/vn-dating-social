package vn.dating.app.social.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.dating.app.social.dto.auth.UserBaseResult;
import vn.dating.app.social.models.User;
import vn.dating.app.social.services.UserService;
import vn.dating.app.social.utils.LoggedIn;

@RestController
@RequestMapping("/api/v1/social/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    @PostMapping("/create")
    public ResponseEntity createUser(@RequestBody UserBaseResult userBaseResult) {


        log.info("create user");
        String userId = LoggedIn.getUserId();
        if(userId==null)return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        User user = new User();

        user.setId(userBaseResult.getId());
        user.setEmail(userBaseResult.getEmail());
        user.setId(userBaseResult.getId());
        user.setUsername(userBaseResult.getUsername());
        user.setLastName(userBaseResult.getLastName());
        user.setFirstName(userBaseResult.getFirstName());
        log.info(userBaseResult.toString());

//
//
//        user = userService.save(user);
//        if(user==null)new ResponseEntity<>("not ok ", HttpStatus.BAD_REQUEST);
//        log.info("create user OK");

        return new ResponseEntity<>(userBaseResult.toString(), HttpStatus.OK);
    }

    @GetMapping("/public")
    public ResponseEntity getPublic() {
        return new ResponseEntity<>("Public", HttpStatus.OK);
    }
}
