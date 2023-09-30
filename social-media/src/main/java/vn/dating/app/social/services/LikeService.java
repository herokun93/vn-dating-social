package vn.dating.app.social.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.dating.app.social.models.Like;
import vn.dating.app.social.models.eenum.ReactType;
import vn.dating.app.social.repositories.LikeRepository;

@Service
public class LikeService {

    private final LikeRepository likeRepository;

    @Autowired
    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    public Like save(Like like) {
        return likeRepository.save(like);
    }

    public Like findByUserIdAndPostUrl(String userId,String postUrl){
        return likeRepository.findByUserIdAndPostUrl(userId,postUrl);
    }

    public void delete(Like like) {
        likeRepository.delete(like);
    }

    public Like findById(long id) {
       return likeRepository.findById(id).orElse(null);
    }

    public Like findByUserIdAndPostIdAndLiked(Long userId, Long postId, ReactType reactType){
        return likeRepository.findByUserIdAndPostIdAndReact(userId, postId,reactType);}
    public Like findByUserIdAndPostId(String userId,Long postId){
        return likeRepository.findByUserIdAndPostId(userId, postId);}

    public Like findByUserIdAndComment(String userId,Long commentId){
        return likeRepository.findByUserIdAndCommentId(userId, commentId);}

    public Like findByUserIdAndReply(String userId,Long replyId){
        return likeRepository.findByUserIdAndReplyId(userId, replyId);}

    public long countLikesByPostId(Long postId) {
        return likeRepository.countByPostId(postId);
    }
}
