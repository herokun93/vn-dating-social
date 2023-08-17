package vn.dating.app.gateway.services;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.dating.app.gateway.models.social.User;
import vn.dating.app.gateway.repositories.social.UserRepository;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getList(){
        return  userRepository.findAll();
    }

    public User createUserToSocial(UserRepresentation userRepresentation,String verify){
        User user = new User(userRepresentation,verify);
        return  userRepository.save(user);
    }
}
