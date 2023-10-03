package vn.dating.app.socket.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.dating.app.socket.model.User;
import vn.dating.app.socket.repositories.UserRepository;
import vn.dating.common.dto.CreateUserDto;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void saveUser(CreateUserDto createUserDto){

        User user = new User();

        user.setId(createUserDto.getId());
        user.setEmail(createUserDto.getEmail());
        user.setUsername(createUserDto.getUsername());
        user.setFirstName(createUserDto.getFirstName());
        user.setLastName(createUserDto.getLastName());
        user.setOnline(false);

        userRepository.save(user);
    }
}
