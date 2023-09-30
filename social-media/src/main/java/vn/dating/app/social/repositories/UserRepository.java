package vn.dating.app.social.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.dating.app.social.dto.community.CommunityUserResultDto;
import vn.dating.app.social.models.User;
import vn.dating.app.social.models.eenum.UserCommunityType;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User save(User user);

    Optional<User> findById(String id);


    Page<User> findUsersByCommunities_Community_Name(String communityName, Pageable pageable);

    @Query("SELECT new vn.dating.app.social.dto.community.CommunityUserResultDto(u.id, u.email, u.username, u.firstName, u.lastName, uc.status, uc.role) " +
            "FROM User u " +
            "JOIN u.communities uc " +
            "WHERE uc.community.name = :communityName")
    Page<CommunityUserResultDto> findUsersByCommunityName(String communityName, Pageable pageable);

    @Query("SELECT new vn.dating.app.social.dto.community.CommunityUserResultDto(u.id, u.email, u.username, u.firstName, u.lastName, uc.status, uc.role) " +
            "FROM User u " +
            "JOIN u.communities uc " +
            "WHERE uc.community.name = :communityName " +
            "AND uc.status = :userCommunityType")
    Page<CommunityUserResultDto> getUsersByCommunityNameAndUserType(
            String communityName, UserCommunityType userCommunityType, Pageable pageable);
}
