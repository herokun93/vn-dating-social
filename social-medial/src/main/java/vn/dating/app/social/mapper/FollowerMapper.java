package vn.dating.app.social.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import vn.dating.app.social.dto.FollowerViewDto;
import vn.dating.app.social.models.Follower;


import java.lang.reflect.Type;
import java.util.List;

public class FollowerMapper {
    public static FollowerViewDto toFollow(Follower follower){
        ModelMapper modelMapper = new ModelMapper();
        FollowerViewDto followerViewDto = modelMapper.map(follower,FollowerViewDto.class);
        return  followerViewDto;
    }
    public static List<FollowerViewDto> toFollowers(List<Follower> followers){
        ModelMapper modelMapper = new ModelMapper();
        Type listType = new TypeToken<List<FollowerViewDto>>() {}.getType();
        return modelMapper.map(followers,listType);
    }

//    public static List<FollowerViewDto> toFollowers(List<Follower> followers){
//        List<FollowerViewDto>  followerViewDtoList = new ArrayList<>();
//        followers.forEach(follower -> {
//            followerViewDtoList.add(toFollow(follower));
//        });
//        return  followerViewDtoList;
//    }
}
