package vn.dating.app.social.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.dating.app.social.models.Community;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {
    Optional<Community>findByName(String name);

    Page<Community> findCommunitiesByCreator_Id(String creatorId, Pageable pageable);

    @Query(value = "SELECT c.* FROM Community c " +
            "INNER JOIN user_community uc ON c.id = uc.community_id " +
            "WHERE uc.user_id = :userId", countQuery = "SELECT COUNT(*) FROM Community c " +
            "INNER JOIN user_community uc ON c.id = uc.community_id " +
            "WHERE uc.user_id = :userId", nativeQuery = true)
    Page<Community> findCommunitiesByMemberUserId(@Param("userId") String userId, Pageable pageable);

}
