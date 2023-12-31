package vn.dating.app.social.services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.dating.app.social.models.UserCommunity;
import vn.dating.app.social.models.eenum.UserCommunityType;
import vn.dating.app.social.repositories.UserCommunityRepository;

import java.util.Optional;

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
    public void deleteUserCommunity(UserCommunity userCommunity){
        userCommunityRepository.delete(userCommunity);
    }

    public void leaveUserCommunity(UserCommunity userCommunity){
        userCommunity.setStatus(UserCommunityType.LEAVE);
        userCommunityRepository.saveAndFlush(userCommunity);
    }
    public UserCommunity updateUserCommunity(UserCommunity userCommunity,UserCommunityType userCommunityType){
        userCommunity.setStatus(userCommunityType);
        return userCommunityRepository.save(userCommunity);
    }
    public Optional<UserCommunity> findByUserIdAndCommunityName(String userId, String communityName){
        return userCommunityRepository.findByUser_IdAndCommunity_Name(userId, communityName);
    }
//    public Optional<UserCommunity> findByUserIdAndCommunityAndType(String userId, String communityName){
//        return userCommunityRepository.findByUser_IdAndCommunity_NameAndType(userId, communityName,UserCommunityType.ACTIVATED);
//    }









    public UserCommunity save(UserCommunity userCommunity){
        return userCommunityRepository.save(userCommunity);
    }

    public void leaveCommunity(String userId, Long communityId) {
        // Implement the logic to remove the user from the community
        userCommunityRepository.deleteByUserIdAndCommunityId(userId, communityId);
    }

}
