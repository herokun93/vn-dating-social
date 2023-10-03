package vn.dating.app.socket.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.dating.app.socket.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User save(User user);
    Optional<User> findById(String id);
}
