package vn.dating.app.gateway.repositories.social;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.dating.app.gateway.models.social.User;

public interface UserRepository extends JpaRepository<User,String> {
}
