package vn.dating.app.social.services;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import vn.dating.app.social.dto.community.CommunityPageMemberDto;
import vn.dating.app.social.dto.community.CommunityUserResultDto;
import vn.dating.app.social.exceptions.UserNotFoundException;
import vn.dating.app.social.models.Community;
import vn.dating.app.social.models.User;
import vn.dating.app.social.models.eenum.UserCommunityType;
import vn.dating.app.social.repositories.UserRepository;
import vn.dating.common.dto.CreateUserDto;

import java.security.Principal;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public User createUserSocial(CreateUserDto createUserDto){
        User user = new User();

        Random random = new Random();
        int randomNumber = random.nextInt(7) + 1; // Generates a random number between 0 and 7, then adds 1
        System.out.println("Random number between 1 and 10: " + randomNumber);
        String avatar = "cat"+randomNumber+".jpeg";

        user.setId(createUserDto.getId());
        user.setEmail(createUserDto.getEmail());
        user.setUsername(createUserDto.getUsername());
        user.setLastName(createUserDto.getLastName());
        user.setFirstName(createUserDto.getFirstName());
        user.setAuth(createUserDto.getId());
        user.setAvatar(avatar);
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

    public boolean isUser(Principal principal) {

        if(principal==null){
            return false;
        }
        return true;

    }




//    public CommunityPageMemberDto getCommunitiesByCommunityName(String communityName, int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        // Implement the logic to fetch communities created by the user based on creatorId
//        Page<User> userPage = userRepository.findUsersByCommunities_Community_Name( communityName,  pageable);
//        CommunityPageMemberDto communityPageMemberDto = new CommunityPageMemberDto(userPage);
//        return communityPageMemberDto;
//    }

    public CommunityPageMemberDto getMembersByCommunityName(String communityName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CommunityUserResultDto> userPage = userRepository.findUsersByCommunityName( communityName,  pageable);
        CommunityPageMemberDto communityPageMemberDto = new CommunityPageMemberDto(userPage);
        return communityPageMemberDto;
    }

    public CommunityPageMemberDto getUsersByCommunityNameAndUserType(String communityName, UserCommunityType userCommunityType,int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CommunityUserResultDto> userPage = userRepository.getUsersByCommunityNameAndUserType( communityName, userCommunityType, pageable);
        CommunityPageMemberDto communityPageMemberDto = new CommunityPageMemberDto(userPage);
        return communityPageMemberDto;
    }
}
