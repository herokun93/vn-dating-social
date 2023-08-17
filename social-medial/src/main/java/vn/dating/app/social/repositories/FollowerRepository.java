package vn.dating.app.social.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import vn.dating.app.social.models.Follower;

import java.util.List;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, Long> {
    Follower findByFollowedIdAndFollowerId(long userId, long followerId);
    List<Follower> findByFollowedId(long userId);
    List<Follower> findByFollowerId(long followerId);
    Page<Follower> findByFollowedId(Long toId, Pageable pageable);
    Page<Follower> findByFollowerIdAndAccepted(Long toId, Pageable pageable,boolean seen);
    Page<Follower> findByFollowerId(Long fromId, Pageable pageable);
}
