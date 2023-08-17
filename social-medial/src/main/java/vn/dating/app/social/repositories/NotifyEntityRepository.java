package vn.dating.app.social.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.dating.app.social.models.eenum.NotificationType;
import vn.dating.app.social.models.NotifyEntity;

import java.util.Optional;

@Repository
public interface NotifyEntityRepository extends JpaRepository<NotifyEntity,Long> {
    NotifyEntity save(NotifyEntity notify);

    @Override
    Optional<NotifyEntity> findById(Long aLong);

    Optional<NotifyEntity> findByPostIdAndType(Long postId, NotificationType type);
    NotifyEntity findByCommentIdAndType(Long commentId, NotificationType type);
    NotifyEntity findByReplyIdAndType(Long commentId, NotificationType type);
    NotifyEntity findByLikeIdAndType(Long commentId, NotificationType type);
}
