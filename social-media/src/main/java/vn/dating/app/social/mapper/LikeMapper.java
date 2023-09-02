package vn.dating.app.social.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import vn.dating.app.social.dto.like.LikeCommentDto;
import vn.dating.app.social.dto.like.LikePostDto;
import vn.dating.app.social.dto.like.LikeReplyDto;
import vn.dating.app.social.models.Like;

import java.lang.reflect.Type;
import java.util.List;

public class LikeMapper {
    public static LikePostDto toPostViewLike(Like like){
        ModelMapper modelMapper = new ModelMapper();
        LikePostDto likePostDto = modelMapper.map(like, LikePostDto.class);
        return likePostDto;
    }

    public static LikeCommentDto toCommentViewLike(Like like){
        ModelMapper modelMapper = new ModelMapper();
        LikeCommentDto likeCommentDto = modelMapper.map(like, LikeCommentDto.class);
        return likeCommentDto;
    }

    public static LikeReplyDto toReplyViewLike(Like like){
        ModelMapper modelMapper = new ModelMapper();
        LikeReplyDto likeReplyDto = modelMapper.map(like, LikeReplyDto.class);
        return likeReplyDto;
    }


    public static List<LikePostDto> toViewLikes(List<Like> likes){
        ModelMapper modelMapper = new ModelMapper();
        Type listType = new TypeToken<List<LikePostDto>>() {}.getType();
        return modelMapper.map(likes,listType);
    }
}
