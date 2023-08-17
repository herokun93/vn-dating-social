package vn.dating.chat.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/chat/messages")
@Slf4j
public class MessageController {

    @GetMapping
    public ResponseEntity getAllComments(Principal principal) {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("authentication");
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            log.info(currentUserName);
        }else {
            log.info("Cannot user name");
        }


        return new ResponseEntity<>("/api/v1/chat/messages", HttpStatus.OK);
    }
}
