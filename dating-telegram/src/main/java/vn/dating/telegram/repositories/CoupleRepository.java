package vn.dating.telegram.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import vn.dating.telegram.model.Couple;
import vn.dating.telegram.model.CoupleStatus;
import vn.dating.telegram.model.User;

import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
public interface CoupleRepository extends JpaRepository<Couple, Long> {
    boolean existsByUser1OrUser2AndStatus(User user1, User user2, CoupleStatus status);
    boolean existsByUser1OrUser2(User user1, User user2);
    Optional<Couple> findByUser1OrUser2AndStatus(User user1, User user2,CoupleStatus status);
    Couple save(Couple couple);



//    @Query(value = "SELECT u FROM User u WHERE NOT EXISTS (SELECT c FROM Couple c WHERE c.status <> 'PENDING' AND (c.user1 = u OR c.user2 = u))",nativeQuery = true)
//    List<User> findPendingUsers();

    List<Couple> findByStatus(CoupleStatus status);
//    List<Couple> findSingleCouples();
}

