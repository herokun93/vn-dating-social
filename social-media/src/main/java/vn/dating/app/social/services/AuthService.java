package vn.dating.app.social.services;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.dating.app.social.dto.community.CommunityPageMemberDto;
import vn.dating.app.social.exceptions.UserNotFoundException;
import vn.dating.app.social.models.Community;
import vn.dating.app.social.models.User;
import vn.dating.app.social.repositories.UserRepository;
import vn.dating.common.dto.CreateUserDto;

import java.security.Principal;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public User createUserSocial(CreateUserDto createUserDto){
        User user = new User();
        User savedUser = userRepository.save(user);;
        return  savedUser;
    }
    public User getCurrentUserById(Principal principal) throws UserNotFoundException {

        if(principal==null) throw new UserNotFoundException("Token not working");

        String userId = principal.getName();
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new UserNotFoundException("User with ID " + userId + " not found");
        }
    }

    public CommunityPageMemberDto getCommunitiesByCommunityName(String communityName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        // Implement the logic to fetch communities created by the user based on creatorId
        Page<User> userPage = userRepository.findUsersByCommunities_Community_Name( communityName,  pageable);
        CommunityPageMemberDto communityPageMemberDto = new CommunityPageMemberDto(userPage);
        return communityPageMemberDto;
    }
}
