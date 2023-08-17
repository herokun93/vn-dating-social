package vn.dating.app.social.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.dating.app.social.dto.FollowerNewDto;
import vn.dating.app.social.mapper.FollowerMapper;
import vn.dating.app.social.models.Follower;
import vn.dating.app.social.models.User;
import vn.dating.app.social.services.FollowerService;
import vn.dating.app.social.services.UserService;


@RestController
@RequestMapping("/api/v1/social/followers")
public class FollowerController {

    private final FollowerService followerService;
    private final UserService userService;

    @Autowired
    public FollowerController(FollowerService followerService,UserService userService) {
        this.followerService = followerService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity follow(@RequestBody FollowerNewDto followerNewDto) {


        if(followerNewDto.getFollowerId() == followerNewDto.getFollowedId())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        User from = userService.getUserById(followerNewDto.getFollowerId());
        User  to = userService.getUserById(followerNewDto.getFollowedId());

        if(from==null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(to==null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Follower followed = followerService.findByFollowedIdAndFollowerId(followerNewDto.getFollowedId(), followerNewDto.getFollowerId());
        if(followed !=null) return new ResponseEntity<>(FollowerMapper.toFollow(followed),HttpStatus.OK);

        followed = followerService.follow(from, to);
        return new ResponseEntity<>(FollowerMapper.toFollow(followed), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity unfollow(@RequestBody FollowerNewDto followerNewDto) {

        if(followerNewDto.getFollowerId() == followerNewDto.getFollowedId())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        User  from = userService.getUserById(followerNewDto.getFollowedId());
        User  to = userService.getUserById(followerNewDto.getFollowedId());

        if(from==null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(to==null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Follower followed = followerService.findByFollowedIdAndFollowerId(followerNewDto.getFollowedId(), followerNewDto.getFollowerId());
        if(followed != null) {
            followerService.unfollow(followed);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity findFollowers(@PathVariable long userId,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {

        User  from = userService.getUserById(userId);

        if(from==null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);



        return new ResponseEntity(followerService.findListByFollowerId(userId,page,size), HttpStatus.OK);
    }

    @GetMapping("/{userId}/followed")
    public ResponseEntity findFollowed(@PathVariable long userId,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {

        User  from = userService.getUserById(userId);

        if(from==null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);



        return new ResponseEntity(followerService.findListByFollowedId(userId,page,size), HttpStatus.OK);
    }

}

