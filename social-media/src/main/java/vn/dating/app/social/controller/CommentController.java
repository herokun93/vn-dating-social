package vn.dating.app.social.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.dating.app.social.dto.ResponseMessage;
import vn.dating.app.social.dto.ResponseObject;
import vn.dating.app.social.dto.comment.CommentDetails;
import vn.dating.app.social.dto.comment.CommentSuccDto;
import vn.dating.app.social.models.Comment;
import vn.dating.app.social.models.Media;
import vn.dating.app.social.models.Post;
import vn.dating.app.social.models.User;
import vn.dating.app.social.repositories.MedialRepository;
import vn.dating.app.social.services.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/social/comments")
public class CommentController {
    @Autowired
    private MedialRepository medialRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private UserCommunityService userCommunityService;

    @Autowired
    private AuthService authService;

    @Autowired
    private KafkaService kafkaService;



    @PostMapping(value ="/create",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseObject> createComment(Principal principal,
                                                @RequestPart("file") MultipartFile file,
                                                @RequestPart("url") String postUrl,
                                                        @RequestPart("anonymous") @Pattern(regexp = "^(true|false)$",
                                                                message = "Anonymous must be 'true' or 'false'") String anonymous,
                                                @RequestPart("content") @NotBlank(message = "Title must not be blank")
                                                    @Size(min = 3, max = 255, message = "Title must be between 3 and 255 characters") String content) {

        log.info("New comment");
        kafkaService.sendMessage();

        User user = authService.getCurrentUserById(principal);
        boolean isAnonymous = Boolean.parseBoolean(anonymous);

        boolean checkComment = userCommunityService.isUserMemberOfSameCommunity(user.getId(),postUrl);

        if(checkComment){

            Comment comment = new Comment();
            comment.setAnonymous(isAnonymous);
            comment.setContent(content);

            if(!file.isEmpty()){
                String fileMedia = mediaService.onlySaveFileToLocal(file,false);
                Media media = new Media();
                media.setPath(fileMedia);
                media.setComment(comment);
                comment.setMedia(media);
            }

            comment = commentService.save(comment);

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", ResponseMessage.CREATED, CommentSuccDto.fromEntity(comment))
            );

        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("NOTOK", ResponseMessage.NOT_A_MEMBER, "")
        );
    }

    @PostMapping(value ="/createWithoutFile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseObject> createCommentWithoutFile(Principal principal,
                                                        @RequestPart("url") String postUrl,
                                                        @RequestPart("anonymous") @Pattern(regexp = "^(true|false)$",
                                                                message = "Anonymous must be 'true' or 'false'") String anonymous,
                                                        @RequestPart("content") @NotBlank(message = "Title must not be blank")
                                                        @Size(min = 3, max = 255, message = "Title must be between 3 and 255 characters") String content) {


        log.info("New comment");
        kafkaService.sendMessage();

        User user = authService.getCurrentUserById(principal);

        boolean isAnonymous = Boolean.parseBoolean(anonymous);
        boolean checkComment = userCommunityService.isUserMemberOfSameCommunity(user.getId(),postUrl);

        if(checkComment){

            Post post = postService.findByUrl(postUrl);

            Comment comment = new Comment();
            comment.setAnonymous(isAnonymous);
            comment.setContent(content);
            comment.setAuth(user.getId());
            comment.setUser(user);
            comment.setPost(post);

            comment = commentService.save(comment);

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK", ResponseMessage.CREATED, CommentDetails.fromEntity(comment))
            );

        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("NOTOK", ResponseMessage.NOT_A_MEMBER, "")
        );
    }
}
