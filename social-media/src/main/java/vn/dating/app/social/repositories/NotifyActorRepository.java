package vn.dating.app.social.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.dating.app.social.models.NotifyActor;

import java.util.Optional;

@Repository
public interface NotifyActorRepository extends JpaRepository<NotifyActor,Long> {
    NotifyActor save(NotifyActor notifyActor);
    Optional<NotifyActor> findBySourceIdAndNotifyEntityId(String sourceId, Long notifyEntityId);
}
