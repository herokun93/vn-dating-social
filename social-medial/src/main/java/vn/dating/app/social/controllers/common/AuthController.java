package vn.dating.app.social.controllers.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/social/authMedial")
@Slf4j
public class AuthController {



    @GetMapping("")
    public ResponseEntity getPost() {

        log.info("authMedial");


        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
