package vn.dating.app.social.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.dating.app.social.mapper.FollowerMapper;
import vn.dating.app.social.models.Follower;
import vn.dating.app.social.models.User;
import vn.dating.app.social.repositories.FollowerRepository;
import vn.dating.app.social.utils.PagedResponse;


import java.util.Collections;
import java.util.List;

@Service
public class FollowerService {

    private final FollowerRepository followerRepository;

    @Autowired
    public FollowerService(FollowerRepository followerRepository) {
        this.followerRepository = followerRepository;
    }

    public Follower follow(User from, User to) {
        Follower follower = new Follower();
        follower.setFollowed(to);
        follower.setFollower(from);
        follower.setAccepted(false);
        return followerRepository.save(follower);
    }

    public void unfollow(Follower follower) {
        followerRepository.delete(follower);
    }
    public boolean checkByFollowedIdAndFollowerId(long userId, long followerId){
        Follower follower = followerRepository.findByFollowedIdAndFollowerId(userId, followerId);
        if(follower == null) return false;
        else return true;
    }

    public Follower findByFollowedIdAndFollowerId(long userId, long followerId){
        Follower follower = followerRepository.findByFollowedIdAndFollowerId(userId, followerId);
        return follower;
    }

    public PagedResponse findListByFollowerId(Long id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Follower> followers = followerRepository.findByFollowerId(id,pageable);

        if(followers.getNumberOfElements()==0){
            return new PagedResponse<>(Collections.emptyList(), followers.getNumber(), followers.getSize(),
                    followers.getTotalElements(), followers.getTotalPages(), followers.isLast());
        }

        return new PagedResponse<>(FollowerMapper.toFollowers(followers.stream().toList()), followers.getNumber(), followers.getSize(), followers.getTotalElements(),
                followers.getTotalPages(), followers.isLast());
    }

    public PagedResponse findListByFollowerIdAndSeen(Long id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Follower> followers = followerRepository.findByFollowerIdAndAccepted(id,pageable,false);

        if(followers.getNumberOfElements()==0){
            return new PagedResponse<>(Collections.emptyList(), followers.getNumber(), followers.getSize(),
                    followers.getTotalElements(), followers.getTotalPages(), followers.isLast());
        }

        return new PagedResponse<>(FollowerMapper.toFollowers(followers.stream().toList()), followers.getNumber(), followers.getSize(), followers.getTotalElements(),
                followers.getTotalPages(), followers.isLast());
    }

    public PagedResponse findListByFollowedId(Long id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Follower> followers = followerRepository.findByFollowedId(id,pageable);

        if(followers.getNumberOfElements()==0){
            return new PagedResponse<>(Collections.emptyList(), followers.getNumber(), followers.getSize(),
                    followers.getTotalElements(), followers.getTotalPages(), followers.isLast());
        }

        return new PagedResponse<>(FollowerMapper.toFollowers(followers.stream().toList()), followers.getNumber(), followers.getSize(), followers.getTotalElements(),
                followers.getTotalPages(), followers.isLast());
    }

    public List<Follower> findFollowers(long userId) {
        return followerRepository.findByFollowedId(userId);
    }

    public List<Follower> findFollowing(long userId) {
        return followerRepository.findByFollowerId(userId);
    }
}

