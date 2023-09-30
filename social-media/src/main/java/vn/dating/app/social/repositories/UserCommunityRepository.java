package vn.dating.app.social.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.dating.app.social.models.Community;
import vn.dating.app.social.models.UserCommunity;
import vn.dating.app.social.models.eenum.UserCommunityType;

import java.util.Optional;


@Repository
public interface UserCommunityRepository extends JpaRepository<UserCommunity,Long> {

    boolean existsByUser_IdAndCommunity_Id(String userId, Long communityId);
    boolean existsByUser_IdAndCommunity_Name(String userId, String communityName);
    void deleteByUserIdAndCommunityId(String userId, Long communityId);


    Optional<UserCommunity> findByUser_IdAndCommunity_Name(String userId, String communityName);

//    @Query("SELECT uc FROM UserCommunity uc " +
//            "WHERE uc.user.id = :userId " +
//            "AND uc.community.name = :communityName " +
//            "AND uc.type = :type")
//    Optional<UserCommunity>findByUser_IdAndCommunity_NameAndType(
//            @Param("userId") String userId,
//            @Param("communityName") String communityName,
//            @Param("type") UserCommunityType type
//    );

//    Page<UserCommunity> findByUserId(String userId, Pageable pageable);

    @Query("SELECT COUNT(uc) > 0 FROM UserCommunity uc " +
            "JOIN Post p ON uc.community.id = p.community.id " +
            "WHERE uc.user.id = :userId AND p.url = :postUrl")
    boolean isUserMemberOfSameCommunity(@Param("userId") String userId, @Param("postUrl") String postUrl);
}
