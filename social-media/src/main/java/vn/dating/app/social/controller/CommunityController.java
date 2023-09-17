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
import vn.dating.app.social.models.eenum.CommunityType;
import vn.dating.app.social.models.eenum.UserCommunityRoleType;
import vn.dating.app.social.models.eenum.UserCommunityType;
import vn.dating.app.social.services.AuthService;
import vn.dating.app.social.services.CommunityService;
import vn.dating.app.social.services.PostService;
import vn.dating.app.social.services.UserCommunityService;
import vn.dating.common.community.OderCommunity;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
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
    private PostService postService;



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

            boolean check = userCommunityService.doesUserCommunityExistByUserIdAndCommunityId(user.getId(), communityId);
            if(check){
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", ResponseMessage.EXISTS, CommunityResultDto.fromEntity(community))

                );
            }

            UserCommunity userCommunity = new UserCommunity();
            userCommunity.setUser(user);
            userCommunity.setCommunity(community);

            CommunityType communityType = community.getType();

            boolean checkCommunityType = (communityType == CommunityType.PRIVATE || communityType ==CommunityType.PROTECTED );

            if(checkCommunityType){
                userCommunity.setType(UserCommunityType.PENDING);
            }else{
                userCommunity.setType(UserCommunityType.ACTIVATED);
            }
            userCommunity.setRole(UserCommunityRoleType.USER);


            userCommunityService.save(userCommunity);

            // You can return a success response or any other response as needed
            if(checkCommunityType){
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", ResponseMessage.WANTING, CommunityResultDto.fromEntity(community))

                );
            }
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

            boolean check = userCommunityService.doesUserCommunityExistByUserIdAndCommunityId(user.getId(), community.getId());

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



    @GetMapping("/view")
    public ResponseEntity<ResponseObject> getViewCommunity(Principal principal,
                                                           @RequestParam("communityName") String communityName){

        Optional<Community> communityOptional = communityService.getCommunityByName(communityName);
        if(communityOptional.isPresent()){
            Community community = communityOptional.get();
            if(community.getType()==CommunityType.PUBLIC){

            }
            if(community.getType()==CommunityType.PRIVATE){
                User user = authService.getCurrentUserById(principal);
                boolean check = userCommunityService.doesUserCommunityExistByUserIdAndCommunityId(user.getId(), community.getId());
            }

        }


        return ResponseEntity.status(HttpStatus.OK).body(

                new ResponseObject("OK", ResponseMessage.SUCCESSFUL,"")

        );
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

    @GetMapping("/public")
    public String getPublic(){
        log.error("social-community-public");

        return "social-community-public";
    }



    @GetMapping("/list")
    public ResponseEntity<ResponseObject> getCommunitiesOfUser(Principal principal,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size) {
        User user = authService.getCurrentUserById(principal);



        CommunityPageDto  communityPageDto = communityService.getCommunitiesOfUser(user.getId(), page, size);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", ResponseMessage.SUCCESSFUL,communityPageDto)

        );
    }

    @GetMapping("/users")
    public ResponseEntity<ResponseObject> getUsersByCommunityName(@Valid @RequestBody CommunityByName communityByName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        CommunityPageMemberDto communityPageMemberDto =  authService.getMembersByCommunityName(communityByName.getName(), page, size);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", ResponseMessage.SUCCESSFUL,communityPageMemberDto)

        );
    }

    @GetMapping("/post")
    public ResponseEntity<ResponseObject> getPageOfCommunity(@RequestParam() String name ,
                                                             Principal principal,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {

//        boolean communityExist = communityService.getCommunityByName(name).isPresent();

        Optional<Community> community = communityService.getCommunityByName(name);

        if(community.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", ResponseMessage.NOT_FOUND,"Community not exist")

            );
        }




        if(community.get().getType().equals(CommunityType.PRIVATE)){

            boolean isUser = authService.isUser(principal);

            if(!isUser){
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", ResponseMessage.NOT_MEMBER,"Not member")

                );
            }

            boolean isMemberOfCommunity =userCommunityService.doesUserCommunityExistByUserIdAndCommunityName(principal.getName(), name);;

            if(!isMemberOfCommunity){
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", ResponseMessage.NOT_MEMBER,"Community private")

                );
            }
        }


        CommunityPageHeaderPostDto communityPageHeaderPostDto =  postService.findByCommunityNameOrderByCreatedAtDesc(name, page, size);

        if(authService.isUser(principal)){
            boolean isMemberOfCommunity =userCommunityService.doesUserCommunityExistByUserIdAndCommunityName(principal.getName(), name);;

            if(isMemberOfCommunity){
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", ResponseMessage.IS_MEMBER,communityPageHeaderPostDto)

                );
            }


        }


        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", ResponseMessage.PUBLIC,communityPageHeaderPostDto)

        );


    }

    @GetMapping("/open")
    public ResponseEntity<ResponseObject> openCommunity(@RequestParam() String name ,
                                                             Principal principal,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {

        Optional<Community> community = communityService.getCommunityByName(name);

        if(community.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", ResponseMessage.NOT_FOUND,"Community not exist")

            );
        }




        if(community.get().getType().equals(CommunityType.PRIVATE)){

            boolean isUser = authService.isUser(principal);

            if(!isUser){
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", ResponseMessage.NOT_MEMBER,"Not member")

                );
            }

            boolean isMemberOfCommunity =userCommunityService.doesUserCommunityExistByUserIdAndCommunityName(principal.getName(), name);;

            if(!isMemberOfCommunity){
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", ResponseMessage.NOT_MEMBER,"Community private")

                );
            }
        }


        CommunityOpenDto communityOpenDto =  postService.openCommunityByCommunity(community.get(), page, size);

        if(authService.isUser(principal)){
            boolean isMemberOfCommunity =userCommunityService.doesUserCommunityExistByUserIdAndCommunityName(principal.getName(), name);;

            if(isMemberOfCommunity){
                List<String>  listOderCommunity = OderCommunity.getList();
                boolean checkIsOder = true;
                for (int index =0;index<listOderCommunity.size();index++ ){
                    if(listOderCommunity.get(index).contains(name)){
                        checkIsOder = false;
                    }
                }

                log.info(checkIsOder+"");
                communityOpenDto.getCommunity().setWritePost(checkIsOder);

                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", ResponseMessage.IS_MEMBER,communityOpenDto)
                );
            }


        }


        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", ResponseMessage.PUBLIC,communityOpenDto)

        );
    }

//    @GetMapping("/users")
//    public ResponseEntity<ResponseObject> getUsersByCommunityName(@Valid @RequestBody CommunityMemberDto communityMemberDto,
//                                                                  @RequestParam(defaultValue = "0") int page,
//                                                                  @RequestParam(defaultValue = "10") int size) {
//
//        CommunityPageMemberDto communityPageMemberDto =  authService.getCommunitiesByCommunityName(communityMemberDto.getName(), page, size);
//
//        return ResponseEntity.status(HttpStatus.OK).body(
//                new ResponseObject("OK", ResponseMessage.SUCCESSFUL,communityPageMemberDto)
//
//        );
//    }

}
