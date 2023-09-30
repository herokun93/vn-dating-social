package vn.dating.app.social.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.dating.app.social.models.Like;
import vn.dating.app.social.models.eenum.ReactType;

import java.util.List;


public interface LikeRepository extends JpaRepository<Like, Long> {
    Like findByUserIdAndPostIdAndReact(Long userId, Long postId, ReactType reactType);
    Like findByUserIdAndPostId(String userId,Long postId);
    Like findByUserIdAndPostUrl(String userId,String postUrl);
    Like findByUserIdAndCommentId(String userId,Long commentId);
    Like findByUserIdAndReplyId(String userId,Long replyId);
    long countByPostId(Long postId);

    List<Long> findDistinctUserIdsByReactIn(List<Integer> reactTypes);
    // Additional custom methods can be defined here if needed
}