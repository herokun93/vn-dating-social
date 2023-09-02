package vn.dating.app.social.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import vn.dating.app.social.dto.community.CommunityPageDto;
import vn.dating.app.social.dto.community.CommunityPageMemberDto;
import vn.dating.app.social.exceptions.ResourceNotFoundException;
import vn.dating.app.social.models.Community;
import vn.dating.app.social.models.User;
import vn.dating.app.social.repositories.UserRepository;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UserService {

    @Autowired
    private  UserRepository userRepository;




    public User findById(String userId) {
        return  userRepository.findById(userId).orElse(null);
    }

//    public CommunityPageMemberDto getCommunitiesByCommunityName(String communityName, int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        // Implement the logic to fetch communities created by the user based on creatorId
//        Page<User> userPage = userRepository.findUsersByCommunityName( communityName,  pageable);
//        CommunityPageMemberDto communityPageMemberDto = new CommunityPageMemberDto(userPage);
//        return communityPageMemberDto;
//    }


    public String getCurrentUserByToken(String token){
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:1994/api/auth/current";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        int statusCode = responseEntity.getStatusCodeValue();

        if(statusCode == HttpStatus.OK.value()){
            String response = responseEntity.getBody();
            log.info(response);
            return response;
        }

       return null;
    }

    public String getUserByToken(String token){

        return UUID.randomUUID().toString();
    }

    public User save(User user){
        return  userRepository.save(user);
    }

//    public User addUser(String email,String username,String keyid){
//        User user = new User();
//        user.setUsername(username);
//        user.setEmail(email);
//        user.setKeyid(keyid);
//
//        user  = userRepository.save(user).orElse(null);
//
//        return user;
//    }



}

