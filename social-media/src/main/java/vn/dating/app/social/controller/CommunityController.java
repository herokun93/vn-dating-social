package vn.dating.app.social.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.dating.app.social.dto.ResponseMessage;
import vn.dating.app.social.dto.ResponseObject;
import vn.dating.app.social.dto.community.*;
import vn.dating.app.social.models.Community;
import vn.dating.app.social.models.User;
import vn.dating.app.social.models.UserCommunity;
import vn.dating.app.social.services.AuthService;
import vn.dating.app.social.services.CommunityService;
import vn.dating.app.social.services.UserCommunityService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/social/communities")
public class CommunityController {
    @Autowired
    private CommunityService communityService;

    @Autowired
    private AuthService authService;



    @Autowired
    private UserCommunityService userCommunityService;

    // Endpoint to create a new community
    @PostMapping
    public ResponseEntity<ResponseObject> createCommunity(Principal principal, @Valid @RequestBody CommunityNewDto communityNewDto) {

         User user = authService.getCurrentUserById( principal);

        Optional<Community> communityOptional = communityService.getCommunityByName(communityNewDto.getName());

         if(communityOptional.isPresent())  return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("NOTOK", ResponseMessage.EXISTS, CommunityResultDto.fromEntity(communityOptional.get())));


        Community createdCommunity = communityService.createCommunity(user,communityNewDto);

        if(createdCommunity==null)  return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("NOTOK", ResponseMessage.CANNOT_CREATED,""));


        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", ResponseMessage.CREATED, CommunityResultDto.fromEntity(createdCommunity))

        );
    }

    @PostMapping("/join")
    public ResponseEntity<ResponseObject> joinCommunity(Principal principal,
                                                        @Valid @RequestBody CommunityJoinDto communityJoinDto) {
        // Get the current user
        User user = authService.getCurrentUserById(principal);

        // Check if the community exists
        Optional<Community> communityOptional = communityService.getCommunityByName(communityJoinDto.getName());



        if (communityOptional.isPresent()) {
            // Community exists, so add the user to it
            Community community = communityOptional.get();
            long communityId  = community.getId();

            boolean check = userCommunityService.doesUserCommunityExist(user.getId(), communityId);
            if(check){
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", ResponseMessage.EXISTS, CommunityResultDto.fromEntity(community))

                );
            }

            UserCommunity userCommunity = new UserCommunity();
            userCommunity.setUser(user);
            userCommunity.setCommunity(community);
            userCommunity.setActive(true);

            userCommunityService.save(userCommunity);

            // You can return a success response or any other response as needed
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", ResponseMessage.CREATED, CommunityResultDto.fromEntity(community))

            );
        } else {
            // Community doesn't exist, return a bad request response or handle it as needed
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("NOTOK", ResponseMessage.NOT_FOUND,"")

            );
        }
    }


    @PostMapping("/leave")
    public ResponseEntity<ResponseObject> leaveCommunity(Principal principal,
                                                         @RequestParam("communityName") String communityName) {
        // Get the current user
        User user = authService.getCurrentUserById(principal);

        // Check if the community exists
        Optional<Community> communityOptional = communityService.getCommunityByName(communityName);

        if (communityOptional.isPresent()) {
            // Community exists, so attempt to leave it
            Community community = communityOptional.get();

            boolean check = userCommunityService.doesUserCommunityExist(user.getId(), community.getId());

            if (check) {
                // User is a member, proceed to remove them from the community
                userCommunityService.leaveCommunity(user.getId(), community.getId());

                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", ResponseMessage.DELETED, CommunityResultDto.fromEntity(community))
                );
            } else {
                // User is not a member of the community, return a response accordingly
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ResponseObject("NOTOK", ResponseMessage.NOT_A_MEMBER, "")
                );
            }
        } else {
            // Community doesn't exist, return a bad request response or handle it as needed
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("NOTOK", ResponseMessage.NOT_FOUND, "")
            );
        }
    }


    @GetMapping("/ismember")
    public ResponseEntity<ResponseObject> checkIsMember(Principal principal,
                                                @Valid @RequestBody CommunityIsMemberDto communityIsMemberDto) {
        // Get the current user
        User user = authService.getCurrentUserById(principal);

        // Check if the community exists
        Optional<Community> communityOptional = communityService.getCommunityByName(communityIsMemberDto.getName());

        if (communityOptional.isPresent()) {
            // Community exists, so check if the user is a member
            Community community = communityOptional.get();

            if (community.getMembers().contains(user)) {
                // User is a member of the community
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", ResponseMessage.IS_MEMBER, CommunityResultDto.fromEntity(community))

                );
            } else {
                // User is not a member of the community
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("NOTOK", ResponseMessage.NOT_MEMBER, CommunityResultDto.fromEntity(community))

                );
            }
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("NOTOK", ResponseMessage.NOT_FOUND,"")

            );
        }
    }

    @GetMapping("/creator")
    public ResponseEntity<ResponseObject> getCommunitiesByCreator(Principal principal,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size) {
        User user = authService.getCurrentUserById(principal);


        CommunityPageDto  communityPageDto = communityService.getCommunitiesByCreatorId(user.getId(), page, size);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", ResponseMessage.SUCCESSFUL,communityPageDto)

        );
    }

    @GetMapping("/users")
    public ResponseEntity<ResponseObject> getUsersByCommunityName(@Valid @RequestBody CommunityMemberDto communityMemberDto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        CommunityPageMemberDto communityPageMemberDto =  authService.getCommunitiesByCommunityName(communityMemberDto.getName(), page, size);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", ResponseMessage.SUCCESSFUL,communityPageMemberDto)

        );
    }

}
