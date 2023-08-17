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
import vn.dating.app.social.dto.reply.ReplyDto;
import vn.dating.app.social.dto.reply.ReplyNewDto;
import vn.dating.app.social.mapper.ReplyMapper;
import vn.dating.app.social.models.*;
import vn.dating.app.social.models.eenum.NotificationType;
import vn.dating.app.social.services.*;

import java.util.List;
import java.util.Optional;


@Slf4j
@RestController
@RequestMapping("/api/v1/social/replies")
public class ReplyController {

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

//    @Autowired
//    private PostService postService;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private AuthService authService;

    @Autowired
    private ReplyService replyService;
    @Autowired
    private NotifyService notifyService;

    @Autowired
    private NotifyEntityService notifyEntityService;



    @PostMapping( consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ReplyDto> createReply(@RequestPart("files") Flux<FilePart> filePartFlux,
                                      @RequestPart("commentId") String commentIdStr,
                                      @RequestPart("content") String content,
                                      JwtAuthenticationToken authenticationToken) {

        UserCustom userCustom = authService.getUserCustom(authenticationToken);


        User createBy = userService.findById(userCustom.getId());

        Long commentId = Long.parseLong(commentIdStr);

        Optional<Comment> comment = commentService.findById(commentId);
        if(createBy ==null )  {
            return Mono.error(new RuntimeException("User not found. Need to login first."));
        }
        if(comment.get() ==null)  {
            return Mono.error(new RuntimeException("User comment not found"));
        }

        Reply newReply = replyService.saveReply(createBy,comment.get(),content);

        if(newReply == null)  {
            return Mono.error(new RuntimeException("Cannot store reply"));
        }

        ReplyDto replyDto = ReplyMapper.toCreateReply(newReply);

        //        sendReplyNotifications(newReply);

        filePartFlux.count().flatMap(count -> {
            if (count == 0) {
                return Mono.just(replyDto);
            } else {

                Mono<List<String>> listMono =  filePartFlux
                        .flatMap(filePart -> mediaService.onlySaveFile(filePart)) // Save each file and get their paths
                        .collectList();

                return listMono.flatMap(filePaths -> {
                    replyDto.setMediaPath(filePaths.stream().findFirst().get());
                    mediaService.saveMediaReply(replyDto.getId(), filePaths.stream().findFirst().get());
                    return Mono.just(replyDto);
                });

            }
        });


        return Mono.just(replyDto);
    }

    private void sendReplyNotifications(Reply reply) {
        // Retrieve the comment author
        User commentAuthor = reply.getComment().getUser();
        Long commentId = reply.getComment().getId();

        // Check if a NotifyEntity already exists for REPLY_CREATE and the commentId
        NotifyEntity existingNotifyEntity = notifyEntityService.findByCommentIdAndType(commentId, NotificationType.REPLY_CREATE);

        if (existingNotifyEntity == null) {
            // Create a new notification entity for the reply
            NotifyEntity notifyEntity = new NotifyEntity();
            notifyEntity.setType(NotificationType.REPLY_CREATE);
            notifyEntity.setReply(reply);

            // Save the notification entity
            NotifyEntity savedNotifyEntity = notifyEntityService.createNotifyEntity(notifyEntity);

            // Implement the logic to check if the user has already received a notification for the notifyEntity
            boolean checkRegisterReceiveNotifyReply = notifyService.hasUserReceivedNotification(commentAuthor, savedNotifyEntity.getId());

            if (!checkRegisterReceiveNotifyReply) {
                // Create a notification for the comment author
                Notify notifyForAuthor = new Notify();
                notifyForAuthor.setUser(commentAuthor);
                notifyForAuthor.setNotifyEntity(savedNotifyEntity);

                // Save the notification for the comment author
                notifyService.saveNotify(notifyForAuthor);
            }
        }
    }
}
