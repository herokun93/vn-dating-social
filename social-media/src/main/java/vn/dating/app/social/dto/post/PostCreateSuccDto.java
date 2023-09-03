package vn.dating.app.social.dto.post;

import lombok.*;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import vn.dating.app.social.dto.media.MediaResultDto;
import vn.dating.app.social.models.Post;

import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateSuccDto {
    private String content;
    private String title;
    private Long id;
    boolean anonymous;
    private Instant createdAt;
    private Instant updatedAt;
    private String community;
    private String url;
    private List<MediaResultDto> media= new ArrayList<>();

    public static PostCreateSuccDto fromEntity(Post post){
        PostCreateSuccDto postCreateSuccDto =  new PostCreateSuccDto();

        postCreateSuccDto.setCreatedAt(post.getCreatedAt());
        postCreateSuccDto.setUpdatedAt(post.getUpdatedAt());
        postCreateSuccDto.setUrl(post.getUrl());
        postCreateSuccDto.setId(post.getId());
        postCreateSuccDto.setTitle(post.getTitle());
        postCreateSuccDto.setContent(post.getContent());
        postCreateSuccDto.setAnonymous(post.isAnonymous());
        postCreateSuccDto.setCommunity(post.getCommunity().getName());

        List<MediaResultDto> media= MediaResultDto.fromEntities(post.getMedia().stream().toList());

        postCreateSuccDto.setMedia(media);

        return postCreateSuccDto;
    }
}
