package vn.dating.app.social.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.dating.app.social.dto.community.CommunityNewDto;
import vn.dating.app.social.dto.community.CommunityPageDto;
import vn.dating.app.social.models.Community;
import vn.dating.app.social.models.User;
import vn.dating.app.social.models.UserCommunity;
import vn.dating.app.social.models.eenum.CommunityType;
import vn.dating.app.social.models.eenum.UserCommunityRoleType;
import vn.dating.app.social.models.eenum.UserCommunityType;
import vn.dating.app.social.repositories.CommunityRepository;
import vn.dating.app.social.repositories.UserCommunityRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CommunityService {
    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private UserCommunityRepository userCommunityRepository;

    public Community createCommunity(User user,CommunityNewDto communityNewDto) {

        String creatorId = user.getId();

        Community community = new Community();
        community.setName(communityNewDto.getName());
        community.setCreator(user);
        community.setDescription(communityNewDto.getDescription());
        community.setType(communityNewDto.getType());
        community.setApproval(communityNewDto.isApproval());
        community.setAuth(creatorId);

        community = communityRepository.save(community);
        UserCommunity userCommunity = new UserCommunity();
        userCommunity.setUser(user);
        userCommunity.setCommunity(community);
        userCommunity.setType(UserCommunityType.ACTIVATED);
        userCommunity.setRole(UserCommunityRoleType.ADMIN);
        userCommunity.setAuth(creatorId);

        userCommunityRepository.save(userCommunity);

        return community;
    }

    public Optional<Community> getCommunityById(Long id) {
        return communityRepository.findById(id);
    }


    public Optional<Community> getCommunityByName(String name) {
        return communityRepository.findByName(name);
    }

    public CommunityPageDto getCommunitiesByCreatorId(String creatorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        // Implement the logic to fetch communities created by the user based on creatorId
        Page<Community> communityPage = communityRepository.findCommunitiesByCreator_Id( creatorId,  pageable);
        CommunityPageDto communityPageDto = new CommunityPageDto(communityPage);
        return communityPageDto;
    }


    public CommunityPageDto getCommunitiesOfUser(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        // Implement the logic to fetch communities created by the user based on creatorId


        Page<Community> communityPage = communityRepository.findCommunitiesByMemberUserId( userId,  pageable);
        CommunityPageDto communityPageDto = new CommunityPageDto(communityPage);
        return communityPageDto;
    }
    public List<Community> getAllCommunities() {
        return communityRepository.findAll();
    }

    public Optional<Community> updateCommunity(Long id, Community community) {
        if (communityRepository.existsById(id)) {
            community.setId(id);
            return Optional.of(communityRepository.save(community));
        }
        return Optional.empty();
    }
    public Community updateCommunity(Community community) {
        // Use the save method to update the community and return the updated entity
        return communityRepository.saveAndFlush(community);
    }

    public boolean userIsMember(User user, Community community) {
        // Assuming you have a list of members in the Community entity
        return community.getMembers().contains(user);
    }
    public boolean deleteCommunity(Long id) {
        if (communityRepository.existsById(id)) {
            communityRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

