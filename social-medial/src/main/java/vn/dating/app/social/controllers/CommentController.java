package vn.dating.app.social.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vn.dating.app.gateway.utils.UserCustom;
import vn.dating.app.social.dto.comment.CommentDetailsDto;
import vn.dating.app.social.dto.comment.CommentDto;
import vn.dating.app.social.dto.comment.CommentReply;
import vn.dating.app.social.dto.reply.PageReplyDto;
import vn.dating.app.social.mapper.CommentMapper;
import vn.dating.app.social.models.*;
import vn.dating.app.social.models.eenum.NotificationType;
import vn.dating.app.social.services.*;
import vn.dating.app.social.utils.PagedResponse;

import java.util.List;


@RestController
@RequestMapping("/api/v1/social/comments")
@Slf4j
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;

    @Autowired
    private  PostService postService;

    @Autowired
    private ReplyService replyService;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotifyActorService notifyActorService;


    @Autowired
    private NotifyService notifyService;

    @Autowired
    private NotifyEntityService notifyEntityService;

    @Autowired
    private AuthService authService;




    @PostMapping( consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<CommentDetailsDto> createComment(
                                                @RequestPart("files") Flux<FilePart> filePartFlux,
                                                @RequestPart("postId") String postIdStr,
                                                @RequestPart("content") String content,
                                                 JwtAuthenticationToken authenticationToken) {


        Long postId = Long.parseLong(postIdStr);

        UserCustom userCustom = authService.getUserCustom(authenticationToken);
        if(!userCustom.isUser()) {
            return Mono.error(new RuntimeException("User not found. Need to login first."));
        }

        User createBy = userService.findById(userCustom.getId());

        Post post = postService.findById(postId);
        if(createBy ==null || post ==null)  {
            return Mono.error(new RuntimeException("User not found. Need to login first."));
        }
        Comment newComment = commentService.saveComment(createBy,post, content);
        CommentDetailsDto  commentDetailsDto = CommentMapper.toNewComment(newComment);


        postService.postUpdateTime(post);

        filePartFlux.collectList().flatMap(fileParts -> {
            System.out.println("Files size "+fileParts.size());
            if (fileParts.size() == 0) {
                return Mono.just(commentDetailsDto);
            } else {


                Mono<List<String>> listMono =  filePartFlux
                        .flatMap(filePart -> mediaService.onlySaveFile(filePart)) // Save each file and get their paths
                        .collectList();

                return listMono.flatMap(filePaths -> {
                    commentDetailsDto.setMediaPath(filePaths.stream().findFirst().get());
                    mediaService.saveMediaComment(commentDetailsDto.getId(), filePaths.stream().findFirst().get());
                    return Mono.just(commentDetailsDto);
                });

            }
        });


        return Mono.just(commentDetailsDto);

//        sendCommentNotifications(newComment);
    }

    private void sendCommentNotifications(Comment comment) {
        // Retrieve the post author
        User postAuthor = comment.getPost().getAuthor();
        Long postId = comment.getPost().getId();
        // Check if a NotifyEntity already exists for COMMENT_CREATE and the postId
        NotifyEntity existingNotifyEntity = notifyEntityService.findByPostIdAndType(postId, NotificationType.COMMENT_CREATE);

        if (existingNotifyEntity == null) {
            // Create a new notification entity for the comment
            existingNotifyEntity = new NotifyEntity();
            existingNotifyEntity.setType(NotificationType.COMMENT_CREATE);
            existingNotifyEntity.setPost(comment.getPost());
            existingNotifyEntity.setComment(comment);
        }

        NotifyEntity savedNotifyEntity = notifyEntityService.createNotifyEntity(existingNotifyEntity);

        // Implement the logic to check if the user has already received a notification for the notifyEntity
        boolean checkRegisterReceiveNotifyComment = notifyService.hasUserReceivedNotification(comment.getUser(), savedNotifyEntity.getId());

        if (!checkRegisterReceiveNotifyComment) {
            // Create a notification for the post author
            Notify notifyForAuthor = new Notify();
            notifyForAuthor.setUser(postAuthor);
            notifyForAuthor.setNotifyEntity(savedNotifyEntity);

            // Save the notification for the post author
            notifyService.saveNotify(notifyForAuthor);
        }
    }



    @GetMapping("/reply")
    public ResponseEntity getCommentOfPost(@RequestBody CommentReply commentReply,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        Comment comment = commentService.findCommentById(commentReply.getCommentId());

        if(comment==null) return new ResponseEntity<>("Comment not exist", HttpStatus.BAD_REQUEST);

        PageReplyDto pagedResponse = replyService.getPageReplyByCommentId(commentReply.getCommentId(),page,size);

        return new ResponseEntity<>(pagedResponse, HttpStatus.OK);
    }


}