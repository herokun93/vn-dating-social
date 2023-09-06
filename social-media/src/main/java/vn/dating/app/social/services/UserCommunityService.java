package vn.dating.app.social.services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.dating.app.social.dto.community.CommunityPageDto;
import vn.dating.app.social.models.Community;
import vn.dating.app.social.models.UserCommunity;
import vn.dating.app.social.repositories.CommunityRepository;
import vn.dating.app.social.repositories.UserCommunityRepository;

@Service
@Slf4j
public class UserCommunityService {

    @Autowired
    private UserCommunityRepository userCommunityRepository;




    public boolean doesUserCommunityExistByUserIdAndCommunityId(String userId, Long communityId) {
        return userCommunityRepository.existsByUser_IdAndCommunity_Id(userId, communityId);
    }

    public boolean doesUserCommunityExistByUserIdAndCommunityName(String userId, String communityName) {
        return userCommunityRepository.existsByUser_IdAndCommunity_Name(userId, communityName);
    }

    public boolean isUserMemberOfSameCommunity(String userId,String postUrl){
        return  userCommunityRepository.isUserMemberOfSameCommunity(userId,postUrl);
    }







    public UserCommunity save(UserCommunity userCommunity){
        return userCommunityRepository.save(userCommunity);
    }

    public void leaveCommunity(String userId, Long communityId) {
        // Implement the logic to remove the user from the community
        userCommunityRepository.deleteByUserIdAndCommunityId(userId, communityId);
    }

}
