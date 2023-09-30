package vn.dating.app.social.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.dating.app.social.models.Comment;
import vn.dating.app.social.models.eenum.PostStatus;

import java.util.Date;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByPostId(Long id, Pageable pageable);
    Page<Comment> findByUserId(Long id, Pageable pageable);
    long countByPostId(Long postId);

    Page<Comment> findByPost_Url(String postUrl, Pageable pageable);
    @Query("SELECT c FROM Comment c " +
            "WHERE c.post.url = :postUrl " +
            "AND c.post.state = :postState")
    Page<Comment> findByPost_UrlAndPost_State(@Param("postUrl") String postUrl, @Param("postState") PostStatus postState, Pageable pageable);

    @Query(value ="SELECT c FROM Comment c WHERE c.post.id = :postId AND c.createdAt < :oldCreatedAt ORDER BY c.createdAt DESC",nativeQuery = true)
    Page<Comment> findCommentsByPostIdAndOldCreatedAt(@Param("postId") Long postId, @Param("oldCreatedAt") Date oldCreatedAt, Pageable pageable);

}
