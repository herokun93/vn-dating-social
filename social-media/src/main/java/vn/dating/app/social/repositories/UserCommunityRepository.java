package vn.dating.app.social.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.dating.app.social.models.UserCommunity;


@Repository
public interface UserCommunityRepository extends JpaRepository<UserCommunity,Long> {

    boolean existsByUser_IdAndCommunity_Id(String userId, Long communityId);
    boolean existsByUser_IdAndCommunity_Name(String userId, String communityName);
    void deleteByUserIdAndCommunityId(String userId, Long communityId);

    @Query("SELECT COUNT(uc) > 0 FROM UserCommunity uc " +
            "JOIN Post p ON uc.community.id = p.community.id " +
            "WHERE uc.user.id = :userId AND p.url = :postUrl")
    boolean isUserMemberOfSameCommunity(@Param("userId") String userId, @Param("postUrl") String postUrl);
}
