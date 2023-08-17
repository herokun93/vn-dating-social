package vn.dating.app.social.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.dating.app.social.models.Notify;

import java.util.Optional;

@Repository
public interface NotifyRepository extends JpaRepository<Notify,Long> {
    Notify save(Notify notify);
    Optional<Notify> findByUserIdAndNotifyEntityId(String userId, Long notifyEntityId);
    Page<Notify> findByNotifyEntity(Long notifyEntityId, Pageable pageable);
    boolean existsByUserIdAndNotifyEntity_Id(String userId, Long notifyEntityId);
}
