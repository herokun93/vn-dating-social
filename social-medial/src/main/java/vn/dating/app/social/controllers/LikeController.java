package vn.dating.app.social.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import vn.dating.app.gateway.utils.UserCustom;
import vn.dating.app.social.dto.like.LikeNewCommentDto;
import vn.dating.app.social.dto.like.LikeNewPostDto;
import vn.dating.app.social.dto.like.LikeNewReplyDto;
import vn.dating.app.social.mapper.LikeMapper;
import vn.dating.app.social.models.*;
import vn.dating.app.social.models.eenum.ReactType;
import vn.dating.app.social.services.*;


@RestController
@RequestMapping("/api/v1/social/likes")
public class LikeController {

    @Autowired
    private  LikeService likeService;

    @Autowired
    private  UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private  PostService postService;

    @Autowired
    private ReplyService replyService;

    @Autowired
    private AuthService authService;



    @PostMapping("/post")
    public ResponseEntity createPostLike(@RequestBody LikeNewPostDto likeNewPostDto,
                                         JwtAuthenticationToken authenticationToken) {

        UserCustom userCustom = authService.getUserCustom(authenticationToken);
        if(!userCustom.isUser()) return  new ResponseEntity<>("Need login",HttpStatus.FORBIDDEN);



        User user = userService.findById(userCustom.getId());

        Post post = postService.findById(likeNewPostDto.getPostId());
        if(user ==null || post ==null)  return new ResponseEntity<>("Post Or Use is null",HttpStatus.BAD_REQUEST);


        Like like = likeService.findByUserIdAndPostId(user.getId(), likeNewPostDto.postId);


        if (like != null) {
            ReactType isLike = like.getReact();

            if(isLike== ReactType.LIKE) return new ResponseEntity<>(LikeMapper.toPostViewLike(like), HttpStatus.OK);
            else {
                like.setReact(ReactType.LIKE);
                like = likeService.save(like);
                return new ResponseEntity<>(LikeMapper.toPostViewLike(like), HttpStatus.OK);
            }
        }


        Like newLike = new Like();
        newLike.setUser(user);
        newLike.setPost(post);
        newLike.setReact(ReactType.LIKE);

        newLike = likeService.save(newLike);
        return new ResponseEntity<>(LikeMapper.toPostViewLike(newLike), HttpStatus.OK);
    }

    @PostMapping("/comment")
    public ResponseEntity createCommentLike(@RequestBody LikeNewCommentDto likeNewCommentDto,
                                         JwtAuthenticationToken authenticationToken) {

        UserCustom userCustom = authService.getUserCustom(authenticationToken);
        if(!userCustom.isUser()) return  new ResponseEntity<>("Need login",HttpStatus.FORBIDDEN);

        User user = userService.findById(userCustom.getId());
        Comment comment = commentService.findCommentById(likeNewCommentDto.getCommentId());

        if(user ==null || comment ==null)  return new ResponseEntity<>("Comment Or Use is null",HttpStatus.BAD_REQUEST);


        Like like = likeService.findByUserIdAndComment(userCustom.getId(), likeNewCommentDto.getCommentId());


        if (like != null) {
            ReactType isLike = like.getReact();

            if(isLike== ReactType.LIKE) return new ResponseEntity<>(LikeMapper.toCommentViewLike(like), HttpStatus.OK);
            else {
                like.setReact(ReactType.LIKE);
                like = likeService.save(like);
                return new ResponseEntity<>(LikeMapper.toCommentViewLike(like), HttpStatus.OK);
            }
        }


        Like newLike = new Like();
        newLike.setUser(user);
        newLike.setComment(comment);
        newLike.setReact(ReactType.LIKE);

        newLike = likeService.save(newLike);
        return new ResponseEntity<>(LikeMapper.toCommentViewLike(newLike), HttpStatus.OK);
    }

    @PostMapping("/reply")
    public ResponseEntity createReplyLike(@RequestBody LikeNewReplyDto likeNewReplyDto,
                                            JwtAuthenticationToken authenticationToken) {

        UserCustom userCustom = authService.getUserCustom(authenticationToken);
        if(!userCustom.isUser()) return  new ResponseEntity<>("Need login",HttpStatus.FORBIDDEN);

        User user = userService.findById(userCustom.getId());
        Reply reply = replyService.findReplyById(likeNewReplyDto.getReplyId());

        if(user ==null || reply ==null)  return new ResponseEntity<>("Reply Or Use is null",HttpStatus.BAD_REQUEST);


        Like like = likeService.findByUserIdAndReply(userCustom.getId(), reply.getId());


        if (like != null) {
            ReactType isLike = like.getReact();

            if(isLike== ReactType.LIKE) return new ResponseEntity<>(LikeMapper.toReplyViewLike(like), HttpStatus.OK);
            else {
                like.setReact(ReactType.LIKE);
                like = likeService.save(like);
                return new ResponseEntity<>(LikeMapper.toReplyViewLike(like), HttpStatus.OK);
            }
        }


        Like newLike = new Like();
        newLike.setUser(user);
        newLike.setReply(reply);
        newLike.setReact(ReactType.LIKE);

        newLike = likeService.save(newLike);
        return new ResponseEntity<>(LikeMapper.toReplyViewLike(newLike), HttpStatus.OK);
//        return new ResponseEntity<>("", HttpStatus.OK);
    }
    @DeleteMapping("/post")
    public ResponseEntity deleteLikePost(@RequestBody LikeNewPostDto likeNewPostDto,JwtAuthenticationToken authenticationToken) {

        UserCustom userCustom = authService.getUserCustom(authenticationToken);
        if(!userCustom.isUser()) return  new ResponseEntity<>("Need login",HttpStatus.FORBIDDEN);

        User user = userService.findById(userCustom.getId());

        Post post = postService.findById(likeNewPostDto.getPostId());
        if(user ==null || post ==null)  return new ResponseEntity<>("Post Or Use is null",HttpStatus.BAD_REQUEST);

        Like like = likeService.findByUserIdAndPostId(userCustom.getId(), likeNewPostDto.postId);
        if (like == null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        like.setReact(ReactType.CLEAR);
//        like = likeService.save(like);
        likeService.delete(like);
        return new ResponseEntity<>("Deleted",HttpStatus.OK);
    }

    @DeleteMapping("/comment")
    public ResponseEntity deleteLikeComment(@RequestBody LikeNewCommentDto likeNewCommentDto,JwtAuthenticationToken authenticationToken) {

        UserCustom userCustom = authService.getUserCustom(authenticationToken);
        if(!userCustom.isUser()) return  new ResponseEntity<>("Need login",HttpStatus.FORBIDDEN);

        User user = userService.findById(userCustom.getId());

        Comment comment = commentService.findCommentById(likeNewCommentDto.getCommentId());
        if(user ==null || comment ==null)  return new ResponseEntity<>("Comment Or Use is null",HttpStatus.BAD_REQUEST);

        Like like = likeService.findByUserIdAndComment(userCustom.getId(), likeNewCommentDto.getCommentId());
        if (like == null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
//        like.setReact(ReactType.CLEAR);
//        like = likeService.save(like);
        likeService.delete(like);
        return new ResponseEntity<>("Deleted",HttpStatus.OK);
    }

    @DeleteMapping("/reply")
    public ResponseEntity deleteLikeReply(@RequestBody LikeNewReplyDto likeNewReplyDto,JwtAuthenticationToken authenticationToken) {

        UserCustom userCustom = authService.getUserCustom(authenticationToken);
        if(!userCustom.isUser()) return  new ResponseEntity<>("Need login",HttpStatus.FORBIDDEN);

        User user = userService.findById(userCustom.getId());

        Reply reply = replyService.findReplyById(likeNewReplyDto.getReplyId());
        if(user ==null || reply ==null)  return new ResponseEntity<>("Reply Or Use is null",HttpStatus.BAD_REQUEST);

        Like like = likeService.findByUserIdAndReply(userCustom.getId(), likeNewReplyDto.getReplyId());
        if (like == null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
//        like.setReact(ReactType.CLEAR);
//        like = likeService.save(like);
        likeService.delete(like);
        return new ResponseEntity<>("Deleted",HttpStatus.OK);
    }
}

