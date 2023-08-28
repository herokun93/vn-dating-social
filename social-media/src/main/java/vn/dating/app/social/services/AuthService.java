package vn.dating.app.social.services;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.dating.app.social.models.User;
import vn.dating.app.social.repositories.UserRepository;
import vn.dating.common.dto.CreateUserDto;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    public User createUserSocial(CreateUserDto createUserDto){
        User user = new User(createUserDto);
        User savedUser = userRepository.save(user);;
        return  savedUser;
    }
}
