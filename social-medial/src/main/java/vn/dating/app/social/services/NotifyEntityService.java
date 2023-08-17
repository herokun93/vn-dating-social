package vn.dating.app.social.services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.dating.app.social.models.*;
import vn.dating.app.social.models.eenum.NotificationType;
import vn.dating.app.social.repositories.NotifyEntityRepository;

import java.util.Optional;


@Service
@Slf4j
public class NotifyEntityService {

    @Autowired
    private NotifyEntityRepository notifyEntityRepository;

    public NotifyEntity saveFollowPostNotifyEntity(Post followPost){
        NotifyEntity notifyEntity = new NotifyEntity();
        notifyEntity.setPost(followPost);
        notifyEntity.setType(NotificationType.POST_COMMENT);
        notifyEntity = notifyEntityRepository.save(notifyEntity);

        if(notifyEntity==null)return null;

        return notifyEntity;
    }

    public NotifyEntity createNotifyEntity(NotifyEntity notifyEntity) {
        // Implement the logic to create a new NotifyEntity
        // You can use your preferred method of accessing the database (e.g., JPA repository, EntityManager)

        // Example using JPA repository:
        return notifyEntityRepository.save(notifyEntity);
    }

    public NotifyEntity findByPostIdAndType(Long postId, NotificationType type) {
        // Implement the logic to find a NotifyEntity by postId and type
        // You can use your preferred method of accessing the database (e.g., JPA repository, EntityManager)

        // Example using JPA repository:
        return notifyEntityRepository.findByPostIdAndType(postId, type).orElse(null);
    }

    public NotifyEntity findByCommentIdAndType(Long commentId, NotificationType type) {
        // Implement the logic to find a NotifyEntity by commentId and type
        // You can use your preferred method of accessing the database (e.g., JPA repository, EntityManager)

        // Example using JPA repository:
        return notifyEntityRepository.findByCommentIdAndType(commentId, type);
    }
    public NotifyEntity savePostCommentNotifyEntity(Post post,String content){
        NotifyEntity notifyEntity = new NotifyEntity();
        notifyEntity.setPost(post);
        notifyEntity.setContent(content);

        notifyEntity.setType(NotificationType.POST_COMMENT);
        notifyEntity = notifyEntityRepository.save(notifyEntity);

        if(notifyEntity==null)return null;

        return notifyEntity;
    }

    public NotifyEntity saveCreatePostNotifyEntity(Post post){
        NotifyEntity notifyEntity = new NotifyEntity();
        notifyEntity.setPost(post);

        notifyEntity.setType(NotificationType.POST_CREATE);
        notifyEntity = notifyEntityRepository.save(notifyEntity);

        if(notifyEntity==null)return null;

        return notifyEntity;
    }

    public NotifyEntity saveCreateCommentNotifyEntity(Comment comment){
        NotifyEntity notifyEntity = new NotifyEntity();
        notifyEntity.setComment(comment);

        notifyEntity.setType(NotificationType.COMMENT_CREATE);
        notifyEntity = notifyEntityRepository.save(notifyEntity);

        if(notifyEntity==null)return null;

        return notifyEntity;
    }

    public NotifyEntity saveCreateReplyNotifyEntity(Reply reply){
        NotifyEntity notifyEntity = new NotifyEntity();
        notifyEntity.setReply(reply);

        notifyEntity.setType(NotificationType.COMMENT_CREATE);
        notifyEntity = notifyEntityRepository.save(notifyEntity);

        if(notifyEntity==null)return null;

        return notifyEntity;
    }

    public NotifyEntity savePostLikeNotifyEntity(Post post){
        NotifyEntity notifyEntity = new NotifyEntity();
        notifyEntity.setPost(post);
        notifyEntity.setType(NotificationType.POST_LIKE);
        notifyEntity = notifyEntityRepository.save(notifyEntity);

        if(notifyEntity==null)return null;

        return notifyEntity;
    }

    public NotifyEntity saveCommentReplyNotifyEntity(Comment comment){
        NotifyEntity notifyEntity = new NotifyEntity();
        notifyEntity.setComment(comment);
        notifyEntity.setType(NotificationType.COMMENT_REPLY);
        notifyEntity = notifyEntityRepository.save(notifyEntity);

        if(notifyEntity==null)return null;

        return notifyEntity;
    }

    public NotifyEntity saveCommentLikeNotifyEntity(Comment comment){
        NotifyEntity notifyEntity = new NotifyEntity();
        notifyEntity.setComment(comment);
        notifyEntity.setType(NotificationType.COMMENT_LIKE);
        notifyEntity = notifyEntityRepository.save(notifyEntity);

        if(notifyEntity==null)return null;

        return notifyEntity;
    }
    public NotifyEntity saveReplyLikeNotifyEntity(Reply reply){
        NotifyEntity notifyEntity = new NotifyEntity();
        notifyEntity.setReply(reply);
        notifyEntity.setType(NotificationType.REPLY_LIKE);
        notifyEntity = notifyEntityRepository.save(notifyEntity);

        if(notifyEntity==null)return null;

        return notifyEntity;
    }


    public  NotifyEntity findCommentOfPost(Long postId){
        return notifyEntityRepository.findByPostIdAndType(postId, NotificationType.POST_COMMENT).orElse(null);

    }
    public  NotifyEntity findLikeOfPost(Long postId){
        Optional<NotifyEntity> notifyEntity = notifyEntityRepository.findByPostIdAndType(postId, NotificationType.POST_COMMENT);
        if(notifyEntity.isEmpty()) return null;
        return notifyEntity.get();
    }
    public  NotifyEntity findLikeOfComment(Long commentId){
        return notifyEntityRepository.findByCommentIdAndType(commentId, NotificationType.COMMENT_LIKE);
    }

    public  NotifyEntity findReplyOfComment(Long commentId){
        return notifyEntityRepository.findByCommentIdAndType(commentId, NotificationType.COMMENT_REPLY);
    }

    public  NotifyEntity findLikeOfReply(Long replyId){
        return notifyEntityRepository.findByReplyIdAndType(replyId, NotificationType.REPLY_LIKE);
    }


}

