package vn.dating.app.social.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.dating.app.social.models.Post;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Post save(Post post);
    Page<Post> findByAuthorId(Long id, Pageable pageable);

//    @Query("SELECT * FROM Post p WHERE p.community_id IN (SELECT id FROM Community WHERE name = ?) ORDER BY p.created_at DESC")
    Page<Post> findByCommunityNameOrderByCreatedAtDesc(@Param("communityName") String communityName, Pageable pageable);


    Optional<Post> findByUrl(String url);

    Page<Post> findAllByOrderByUpdatedAtDesc(Pageable pageable);

    @Modifying
    @Query(value = "SELECT id FROM POST WHERE id =?1 ",nativeQuery = true)
    List findPostById(Long id);

//    @Query(value = "SELECT * FROM (SELECT p.*, COALESCE(MAX(c.created_at), p.created_at) AS max_comment_time " +
//            "FROM Post p " +
//            "LEFT JOIN comment c ON p.id = c.post_id " +
//            "GROUP BY p.id, p.created_at, p.updated_at, p.content, p.title, p.delete, p.url, p.author_id, p.type, p.state) AS p " +
//            "ORDER BY max_comment_time DESC", nativeQuery = true)
//    Page<Post> findPostByLatestComment(Pageable pageable);

    @Query(value = "SELECT p FROM Post p LEFT JOIN p.comments c " +
            "WHERE c.createdAt = (" +
            "SELECT MAX(c2.createdAt) FROM Comment c2 WHERE c2.post = p" +
            ") ")
    Page<Post> findPostByLatestComment(Pageable pageable);





    @Query(value = "SELECT p FROM Post p WHERE p.id IN (SELECT c.post.id FROM Comment c GROUP BY c.post.id HAVING MAX(c.createdAt) <= CURRENT_TIMESTAMP) ORDER BY p.createdAt DESC",nativeQuery = true)
    Page<Post> findPostsWithLastCommentCreatedAt(Pageable pageable);
//
//    @Query(value ="SELECT p FROM Post p JOIN FETCH p.comments c GROUP BY p.id ORDER BY MIN(c.createdAt) ASC",nativeQuery = true)
//    Page<Post> findPostsWithSortedOldestComments(Pageable pageable);



}
