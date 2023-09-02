package vn.dating.app.social.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.dating.app.social.models.UserCommunity;


@Repository
public interface UserCommunityRepository extends JpaRepository<UserCommunity,Long> {

    boolean existsByUser_IdAndCommunity_Id(String userId, Long communityId);
    void deleteByUserIdAndCommunityId(String userId, Long communityId);
}
