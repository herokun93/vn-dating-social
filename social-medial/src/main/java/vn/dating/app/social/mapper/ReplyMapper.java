package vn.dating.app.social.mapper;

import org.modelmapper.ModelMapper;
import vn.dating.app.social.dto.reply.ReplyDto;
import vn.dating.app.social.models.Reply;

import java.util.ArrayList;
import java.util.List;

public class ReplyMapper {
    public static ReplyDto toReply(Reply reply){
        ModelMapper modelMapper = new ModelMapper();
        ReplyDto replyDto = modelMapper.map(reply,ReplyDto.class);
        return replyDto;
    }

    public static ReplyDto toDetailsReply(Reply reply){
        ModelMapper modelMapper = new ModelMapper();
        ReplyDto replyDto = modelMapper.map(reply,ReplyDto.class);

        replyDto.setCountLike(reply.getLikes().size());
//        List<LikeViewDto> likeViewDtoList = LikeMapper.toViewLikes(reply.getLikes());
//        replyDto.setLikes(new ArrayList<>());

        return replyDto;
    }

    public static ReplyDto toCreateReply(Reply reply){
        ModelMapper modelMapper = new ModelMapper();
        ReplyDto replyDto = modelMapper.map(reply,ReplyDto.class);
        replyDto.setCountLike(0);
//        replyDto.setLikes(new ArrayList<>());
        return replyDto;
    }
    public static List<ReplyDto> toListReplies(List<Reply> replies){
        ModelMapper modelMapper = new ModelMapper();

        List<ReplyDto> replyDtos = new ArrayList<>();
        replies.forEach(reply -> {
            ReplyDto replyDto = modelMapper.map(reply,ReplyDto.class);
            replyDto.setCountLike(reply.getLikes().size());
            replyDtos.add(replyDto);
        });

        return replyDtos;
    }
}
