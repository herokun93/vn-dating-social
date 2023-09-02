package vn.dating.app.social.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import vn.dating.app.social.dto.comment.CommentDetailsDto;
import vn.dating.app.social.dto.comment.CommentDto;
import vn.dating.app.social.models.Comment;
import vn.dating.app.social.models.Reply;

import java.util.ArrayList;
import java.util.List;

public class CommentMapper {
    public static CommentDetailsDto toNewComment(Comment comment){
        ModelMapper modelMapper = new ModelMapper();
        CommentDetailsDto commentViewDto = modelMapper.map(comment,CommentDetailsDto.class);
        commentViewDto.setCountReply(0);
        commentViewDto.setCountLike(0);
        commentViewDto.setReplies(new ArrayList<>());

        return  commentViewDto;
    }

//    public static List<CommentDetailsDto> toGetComments(List<Comment> comments){
//        ModelMapper modelMapper = new ModelMapper();
//        Type listType = new TypeToken<List<CommentDetailsDto>>() {}.getType();
//        return modelMapper.map(comments,listType);
//    }

    public static List<CommentDetailsDto> toGetComments(List<Comment> comments) {
         ModelMapper modelMapper = new ModelMapper();
        List<CommentDetailsDto> commentDetailsDtos = modelMapper.map(comments, new TypeToken<List<CommentDetailsDto>>() {}.getType());
        for (CommentDetailsDto commentDetailsDto : commentDetailsDtos) {
            commentDetailsDto.setCountReply(commentDetailsDto.getReplies().size());
            commentDetailsDto.setCountLike(commentDetailsDto.getCountLike());
            commentDetailsDto.setReplies(new ArrayList<>());
        }
        return commentDetailsDtos;
    }

    public static List<CommentDto> toGetCommentDtos(List<Comment> comments) {

        ModelMapper modelMapper = new ModelMapper();
        List<CommentDto> commentDtoList = new ArrayList<>();
        comments.forEach(comment -> {
            CommentDto commentDto = modelMapper.map(comment,CommentDto.class);


            commentDto.setCountLike(comment.getLikes().size());
            List<Reply> replies = comment.getReplies().stream().toList();
            commentDto.setCountReply(replies.size());
//            commentDto.setReplies(replies.subList(0, Math.min(replies.size(), 3)));
            commentDtoList.add(commentDto);


        });

        return commentDtoList;
    }
}
