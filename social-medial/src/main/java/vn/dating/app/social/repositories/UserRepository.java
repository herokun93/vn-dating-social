package vn.dating.app.social.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.dating.app.social.models.User;


import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Modifying
    @Query(value = "SELECT id FROM users WHERE id =?1 ",nativeQuery = true)
    List findUserById( Long id);

    Optional<User> findById(String id);

    User findByUsername(String username);

    User save(User user);

//    Optional<User> save(User user);

    @Modifying
    @Query(value = "SELECT id FROM users WHERE id =?1 ",nativeQuery = true)
    List existsUserById( Long id);
}
