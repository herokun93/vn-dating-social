package vn.dating.app.social.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.dating.app.social.dto.ResponseMessage;
import vn.dating.app.social.dto.ResponseObject;
import vn.dating.app.social.dto.like.LikeNewPostDto;
import vn.dating.app.social.models.Like;
import vn.dating.app.social.models.Post;
import vn.dating.app.social.models.User;
import vn.dating.app.social.services.*;

import javax.validation.Valid;
import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/api/social/likes")
public class LikeController {


    @Autowired
    private LikeService likeService;

    @Autowired
    private AuthService authService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserCommunityService userCommunityService;



    @PostMapping("/post")
    public ResponseEntity<ResponseObject>  createLikePost( @Valid @RequestBody LikeNewPostDto likeNewPostDto,
                                                          Principal principal) {

        User user = authService.getCurrentUserById(principal);

        Post post = postService.findByUrlAndAndState(likeNewPostDto.getPostUrl());

       if(post==null){
           return ResponseEntity.status(HttpStatus.OK).body(
                   new ResponseObject("OK", ResponseMessage.NOT_FOUND,"Post not exist Or not accept")
           );
       }

        boolean isMember = userCommunityService.isUserMemberOfSameCommunity(principal.getName(), likeNewPostDto.getPostUrl());

       if(!isMember){
           return ResponseEntity.status(HttpStatus.OK).body(
                   new ResponseObject("OK", ResponseMessage.NOT_MEMBER,"Not member")
           );
       }


        Like liked = likeService.findByUserIdAndPostUrl(user.getId(), likeNewPostDto.getPostUrl());

       if(liked!=null){
           if(liked.getReact()==likeNewPostDto.getReact()){
               return ResponseEntity.status(HttpStatus.OK).body(
                       new ResponseObject("OK", ResponseMessage.SUCCESSFUL,"Liked")
               );
           }else{
               liked.setReact(likeNewPostDto.getReact());
               likeService.save(liked);
               return ResponseEntity.status(HttpStatus.OK).body(
                       new ResponseObject("OK", ResponseMessage.SUCCESSFUL,"Liked")
               );
           }

       }else{
           Like newLike = new Like();
           newLike.setUser(user);
           newLike.setPost(post);
           newLike.setAuth(user.getId());
           newLike.setReact(likeNewPostDto.getReact());

           likeService.save(newLike);

           return ResponseEntity.status(HttpStatus.OK).body(
                   new ResponseObject("OK", ResponseMessage.SUCCESSFUL,"Liked")
           );
       }
    }
}
