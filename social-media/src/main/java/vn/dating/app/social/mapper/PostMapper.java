package vn.dating.app.social.mapper;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import vn.dating.app.social.dto.MediaDetailsDto;
import vn.dating.app.social.dto.comment.PageCommentDto;
import vn.dating.app.social.dto.post.PostDetailDto;
import vn.dating.app.social.dto.post.PostNewDto;
import vn.dating.app.social.dto.post.PostViewDto;
import vn.dating.app.social.models.Comment;
import vn.dating.app.social.models.Media;
import vn.dating.app.social.models.Post;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PostMapper {

    public static PostNewDto toPostNew(Post post){
        ModelMapper modelMapper = new ModelMapper();
        PostNewDto postNewDto = modelMapper.map(post,PostNewDto.class);
        return postNewDto;
    }

    public static PostViewDto toPostCreateView(Post post){
        ModelMapper modelMapper = new ModelMapper();
        PostViewDto postViewDto = modelMapper.map(post,PostViewDto.class);
        postViewDto.setCountComment(0);
        postViewDto.setCountLike(0);
        postViewDto.setMedia(new ArrayList<>());
        return postViewDto;
    }

    public static List<PostViewDto> toGetPosts(List<Post> posts){

        ModelMapper modelMapper = new ModelMapper();
        Type listType = new TypeToken<List<PostViewDto>>() {}.getType();
        return modelMapper.map(posts,listType);
    }

    public static PostViewDto toPostView(Post post,long commentCount,long likeCount){
        ModelMapper modelMapper = new ModelMapper();
        PostViewDto postViewDto = modelMapper.map(post,PostViewDto.class);
        postViewDto.setCountLike(likeCount);
        postViewDto.setCountComment(commentCount);
        return postViewDto;
    }

    public static PostViewDto toPostView(Post post){
        ModelMapper modelMapper = new ModelMapper();
        PostViewDto postViewDto = modelMapper.map(post,PostViewDto.class);
        return postViewDto;
    }

    public static List<PostViewDto> toListPostDtos(List<Post> postList){

        List<PostViewDto> postViewDtos = new ArrayList<>();

        log.info(postList.toString());
        postList.forEach(post -> {
            PostViewDto postViewDto = toPostView(post);
            postViewDto.setCountLike(post.getLikes().size());

            log.info(String.valueOf(post.getComments().size()));

            postViewDto.setCountComment(post.getComments().size());
            postViewDtos.add(postViewDto);
        });
        return  postViewDtos;
    }

//    public static PostDetailDto toPostDetail(Post post){
//
//
//        ModelMapper modelMapper = new ModelMapper();
//
//        PostDetailDto postDetailDto = modelMapper.map(post, PostDetailDto.class);
//
//        postDetailDto.setComments(post.getComments().stream()
//                .map(comment->modelMapper.map(comment, CommentDetailsDto.class)).collect(Collectors.toList()));
//
//        List<LikeViewDto> likeViewDtoList = new ArrayList<>();
//        post.getLikes().forEach(like -> {
//            if(like.getReact() != ReactType.CLEAR) likeViewDtoList.add(LikeMapper.toViewLike(like));
//        });
//
//        postDetailDto.setLikes(likeViewDtoList);
//
//        return postDetailDto;
//
//    }

    public static PostDetailDto toPostDetail(Post post,
                                             Page<Comment> commentPage,
                                             long commentCount,
                                             long likeCount,
                                             List<MediaDetailsDto> mediaDetailsDtos)
    {


        ModelMapper modelMapper = new ModelMapper();

        PostDetailDto postDetailDto = modelMapper.map(post, PostDetailDto.class);

        PageCommentDto pageCommentDto = new PageCommentDto(commentPage);

        postDetailDto.setComments(pageCommentDto);

        postDetailDto.setCountLike(likeCount);
        postDetailDto.setCountComment(commentCount);


        for(int index =0; index<mediaDetailsDtos.size();index++){
            postDetailDto.addMedial(mediaDetailsDtos.get(index).getPath());
        }



        return postDetailDto;

    }

    public static PostDetailDto toPostDetail(Post post, Page<Comment> comments){


        ModelMapper modelMapper = new ModelMapper();

        PostDetailDto postDetailDto = modelMapper.map(post, PostDetailDto.class);

        PageCommentDto pageCommentDto = new PageCommentDto(comments);

        postDetailDto.setComments(pageCommentDto);


        postDetailDto.setCountLike(post.getLikes().size());
        postDetailDto.setCountComment(post.getComments().size());

        List<Media> media = post.getMedia().stream().toList();
        for (int index =0;index<media.size();index++){
            postDetailDto.addMedial(media.get(index).getPath());
        }


        return postDetailDto;

    }
}
