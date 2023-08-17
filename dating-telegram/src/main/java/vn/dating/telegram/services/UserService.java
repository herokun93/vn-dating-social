package vn.dating.telegram.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.dating.telegram.model.User;
import vn.dating.telegram.repositories.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public Optional<User> findByChatId(Long telegram){
        return userRepository.findByChatId(telegram);
    }

    public User save(User user){
        return  userRepository.save(user);
    }
}
