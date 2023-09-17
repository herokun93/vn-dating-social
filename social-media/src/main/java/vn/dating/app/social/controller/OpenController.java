package vn.dating.app.social.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.dating.app.social.dto.ResponseMessage;
import vn.dating.app.social.dto.ResponseObject;
import vn.dating.app.social.dto.community.CommunityPageDto;
import vn.dating.app.social.dto.open.OpenHomeDto;
import vn.dating.app.social.models.User;
import vn.dating.app.social.services.AuthService;
import vn.dating.app.social.services.CommunityService;

import java.security.Principal;

@RestController
@RequestMapping("/api/social/open")
@Slf4j
public class OpenController {

    @Autowired
    private AuthService authService;

    @Autowired
    private CommunityService communityService;

    @GetMapping("/home")
    public ResponseEntity<ResponseObject> getCommunitiesOfUser(Principal principal) {

        log.debug("request");

        OpenHomeDto openHomeDto = communityService.openHomeDto(principal);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", ResponseMessage.SUCCESSFUL,openHomeDto)

        );
    }
}
