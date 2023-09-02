package vn.dating.app.social.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.dating.app.social.models.Community;

import java.util.Optional;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {
    Optional<Community>findByName(String name);

    Page<Community> findCommunitiesByCreator_Id(String creatorId, Pageable pageable);
}
