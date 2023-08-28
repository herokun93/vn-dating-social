package vn.dating.app.social.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.dating.app.social.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User save(User user);
}
