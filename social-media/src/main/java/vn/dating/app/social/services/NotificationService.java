package vn.dating.app.social.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.dating.app.social.dto.NotificationDataDto;
import vn.dating.app.social.models.*;
import vn.dating.app.social.repositories.CommentRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class NotificationService {


    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private NotifyActorService notifyActorService;

    @Autowired
    private NotifyEntityService notifyEntityService;

    @Autowired
    private NotifyService notifyService;






//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;

//    public void sendNotification(Notification notification, NotificationDto notificationDto) {
//
//        int pageNumber = 1;
//        int pageSize = AppConstants.DEFAULT_PAGE_SIZE_I;
//        List<User> userList = new ArrayList<>();
//        boolean hasMore = true;
//        while (hasMore) {
//            Page<User> userPage = postService.getUsersByPostId(notificationDto.getPostId(), pageNumber, pageSize);
//            List<User> currentPage = userPage.getContent();
//            userList.addAll(currentPage);
//            hasMore = currentPage.size() == pageSize;
//            pageNumber++;
//        }
//        userList.add(notification.getSource());
//
//
//        List<String> userIdList = userList.stream()
//                .map(User::getId)
//                .distinct()
//                .collect(Collectors.toList());
//
//        for (int index = 0; index< userIdList.size();index++){
//            String userId = userIdList.get(index);
//            messagingTemplate.convertAndSendToUser(userId,"/topic/notifications", notificationDto);
//        }
//    }

    public void sendNotification(NotifyEntity notifyEntity) {

        if(notifyEntity.getNotifies() ==null){

        }else{
            int userSize = notifyEntity.getNotifies().size();
            List<String> userIds = new ArrayList<>();

//            for(int i=0;i< userSize;i++){
//                User user = notifyEntity.getNotifies().get(i).getUser();
//                if(user.isOnline()){
//                    userIds.add(user.getId());
//                }
//            }

            NotificationDataDto notificationDataDto = new NotificationDataDto();
            notificationDataDto.setContent(notifyEntity.getContent());
            notificationDataDto.setCreatedAt(notifyEntity.getCreatedAt());
            notificationDataDto.setUpdatedAt(notifyEntity.getUpdatedAt());
            notificationDataDto.setType(notifyEntity.getType());

            switch (notifyEntity.getType()) {
                case NEW_FOLLOWER:
                    // handle new follower notification
                    break;
                case ACCEPT_FOLLOW:
                    // handle accepted follow request notification
                    break;
                case POST_COMMENT:
                    notificationDataDto.setUrl(notifyEntity.getPost().getUrl());
                    notificationDataDto.setId(notifyEntity.getComment().getId());
                    // handle comment on a post notification
                    break;
                case POST_LIKE:
                    // handle like on a post notification
                    break;
                case COMMENT_REPLY:
                    // handle reply to a comment notification
                    break;
                case COMMENT_LIKE:
                    // handle like on a comment notification
                    break;
                case REPLY_LIKE:
                    // handle like on a reply notification
                    break;
                default:
                    // handle unrecognized notification type
                    break;
            }



            for(int i=1;i<userIds.size();i++){
//                messagingTemplate.convertAndSendToUser(userIds.get(i),"/topic/notifications", notificationDataDto);
            }

        }

    }





    public boolean createListenCommentAndLikeOfPostBySource(User sourceUser, Post post, String content){

        NotifyEntity notifyEntityComment = notifyEntityService.findCommentOfPost(post.getId());
        NotifyEntity notifyEntityLike = notifyEntityService.findCommentOfPost(post.getId());

        if(notifyEntityComment==null){
            notifyEntityComment = notifyEntityService.savePostCommentNotifyEntity(post,content);
        }
        if(notifyEntityLike==null){
            notifyEntityLike = notifyEntityService.savePostLikeNotifyEntity(post);
        }

        Notify notifyComment = notifyService.findByUserIdAndNotifyEntity(sourceUser.getId(),notifyEntityComment.getId());
        Notify notifyLike = notifyService.findByUserIdAndNotifyEntity(sourceUser.getId(),notifyEntityComment.getId());

        if(notifyComment==null){
            notifyComment = notifyService.saveNotify(sourceUser,notifyEntityComment);
        }
        if(notifyLike==null){
            notifyLike = notifyService.saveNotify(sourceUser,notifyEntityLike);
        }

        if(notifyComment !=null && notifyLike !=null) return true;
        return false;
    }

    public boolean createListenPostBySource(User sourceUser,Post post,String content){

        NotifyEntity notifyEntityComment = notifyEntityService.findCommentOfPost(post.getId());
        NotifyEntity notifyEntityLike = notifyEntityService.findCommentOfPost(post.getId());

        if(notifyEntityComment==null){
            notifyEntityComment = notifyEntityService.savePostCommentNotifyEntity(post,content);
        }
        if(notifyEntityLike==null){
            notifyEntityLike = notifyEntityService.savePostLikeNotifyEntity(post);
        }

        Notify notifyComment = notifyService.findByUserIdAndNotifyEntity(sourceUser.getId(),notifyEntityComment.getId());
        Notify notifyLike = notifyService.findByUserIdAndNotifyEntity(sourceUser.getId(),notifyEntityComment.getId());

        if(notifyComment==null){
            notifyComment = notifyService.saveNotify(sourceUser,notifyEntityComment);
        }
        if(notifyLike==null){
            notifyLike = notifyService.saveNotify(sourceUser,notifyEntityLike);
        }

        if(notifyComment !=null && notifyLike !=null) return true;
        return false;
    }

    public NotifyEntity createListenCommentOfPost(User writeUser, Comment createComment){
        NotifyEntity notifyEntityCommentPost = notifyEntityService.findCommentOfPost(createComment.getId());

        if(notifyEntityCommentPost==null){
            notifyEntityCommentPost = notifyEntityService.savePostCommentNotifyEntity(createComment.getPost(),createComment.getContent());
        }

        Notify notify = notifyService.findByUserIdAndNotifyEntity(writeUser.getId(),notifyEntityCommentPost.getId());
        if(notify==null){
            notify = notifyService.saveNotify(writeUser,notifyEntityCommentPost);
        }

        return notifyEntityCommentPost;

    }


    public Notify createListenLikeOfComment(User writeUser,Comment createComment){
        NotifyEntity notifyEntityLikeComment = notifyEntityService.findLikeOfComment(createComment.getId());

        if(notifyEntityLikeComment==null){
            notifyEntityLikeComment = notifyEntityService.saveCommentLikeNotifyEntity(createComment);
        }
        Notify notify = notifyService.findByUserIdAndNotifyEntity(writeUser.getId(), notifyEntityLikeComment.getId());
        if(notify== null) {
            notify = notifyService.saveNotify(writeUser,notifyEntityLikeComment);
        }
        return notify;
    }

    public Notify createListenReplyOfComment(User writeUser,Comment createComment){
        NotifyEntity notifyEntityReplyComment = notifyEntityService.findReplyOfComment(createComment.getId());

        if(notifyEntityReplyComment==null){
            notifyEntityReplyComment = notifyEntityService.saveCommentLikeNotifyEntity(createComment);
        }
        Notify notify = notifyService.findByUserIdAndNotifyEntity(writeUser.getId(), notifyEntityReplyComment.getId());
        if(notify== null) {
            notify = notifyService.saveNotify(writeUser,notifyEntityReplyComment);
        }
        return notify;
    }

    public Notify createListenLikeOfReply(User writeUser, Reply createReply){
        NotifyEntity notifyEntityLikeReply = notifyEntityService.findLikeOfComment(createReply.getId());

        if(notifyEntityLikeReply==null){
            notifyEntityLikeReply = notifyEntityService.saveReplyLikeNotifyEntity(createReply);
        }
        Notify notify = notifyService.findByUserIdAndNotifyEntity(writeUser.getId(), notifyEntityLikeReply.getId());
        if(notify== null) {
            notify = notifyService.saveNotify(writeUser,notifyEntityLikeReply);
        }
        return notify;
    }
}
