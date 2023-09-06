package vn.dating.app.social.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.dating.app.social.models.Media;

import java.util.Optional;

public interface MedialRepository extends JpaRepository<Media, Long> {
    Page<Media> findByPostId(Long id, Pageable pageable);

    Optional<Media> findByPath(String path);


    Optional<Media> findByCommentId(Long aLong);
    Optional<Media> findByReplyId(Long aLong);
}
