package vn.dating.app.social.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.dating.app.social.models.Comment;
import vn.dating.app.social.models.Reply;

import java.util.Optional;

@Repository
public interface ReplyRepository extends JpaRepository<Reply,Long> {

    Reply save(Reply reply);

    @Override
    Optional<Reply> findById(Long aLong);

    Page<Reply> findByCommentId(Long commentId, Pageable pageable);
}
