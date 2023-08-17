package vn.dating.app.social.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import vn.dating.app.social.dto.MediaDetailsDto;
import vn.dating.app.social.models.Comment;
import vn.dating.app.social.models.Post;
import vn.dating.app.social.models.Reply;

import java.lang.reflect.Type;
import java.util.List;

public class MediaMapper {

    public static List<MediaDetailsDto> toMediaOfPost(Post post){
        ModelMapper modelMapper = new ModelMapper();
        Type listType = new TypeToken<List<MediaDetailsDto>>() {}.getType();

        List<MediaDetailsDto> mediaDetailsDtos = modelMapper.map(post.getMedia(),listType);
        return mediaDetailsDtos;
    }

//    public static MediaDetailsDto toMediaOfComment(Comment comment){
//        ModelMapper modelMapper = new ModelMapper();
//        MediaDetailsDto mediaDetailsDto = modelMapper.map(comment.getMedia(), MediaDetailsDto.class);
//        return mediaDetailsDto;
//    }

//    public static MediaDetailsDto toMediaOfReply(Reply reply){
//        ModelMapper modelMapper = new ModelMapper();
//        MediaDetailsDto mediaDetailsDto = modelMapper.map(reply.getMedia(), MediaDetailsDto.class);
//        return mediaDetailsDto;
//    }


}
