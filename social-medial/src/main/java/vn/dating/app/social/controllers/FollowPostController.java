package vn.dating.app.social.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.dating.app.social.dto.FollowerPostNewDto;
import vn.dating.app.social.services.PostService;
import vn.dating.app.social.services.UserService;

@RestController
@RequestMapping("/api/v1/social/followPost")
public class FollowPostController {



    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

//    @PostMapping("/follow")
//    public ResponseEntity follow(@RequestBody FollowerPostNewDto followPostDto) {
//
//        User user = userService.findById(followPostDto.getUserId());
//        if(user ==null){
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//        Post post = postService.findById(followPostDto.getPostId());
//        if(post ==null){
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//        Optional<FollowPost> getFollow = followPostService.findByUserIdAndPostId(followPostDto.getUserId(),followPostDto.getPostId());
//        if(getFollow.get() ==null){
//            FollowPost followPost =  followPostService.follow(user, post);
//            return new ResponseEntity<>(FollowerPostMapper.toFollowerPost(followPost), HttpStatus.OK);
//        }
//        return new ResponseEntity<>(FollowerPostMapper.toFollowerPost(getFollow.get()), HttpStatus.OK);
//    }

    @DeleteMapping("/unfollow")
    public ResponseEntity unfollow(@RequestBody FollowerPostNewDto followPostDto) {
//        User user = userService.findById(followPostDto.getUserId());
//        if(user ==null){
//            return new ResponseEntity(HttpStatus.BAD_REQUEST);
//        }
//
//        Post post = postService.findById(followPostDto.getPostId());
//        if(post ==null){
//            return new ResponseEntity(HttpStatus.BAD_REQUEST);
//        }
//
//        Optional<FollowPost> getFollow = followPostService.findByUserIdAndPostId(followPostDto.getUserId(),followPostDto.getPostId());
//        if(getFollow.get() ==null){
//            return new ResponseEntity(HttpStatus.OK);
//        }
//        followPostService.unfollow(followPostDto.getUserId(), followPostDto.getPostId());
        return new ResponseEntity(HttpStatus.OK);
    }


}
