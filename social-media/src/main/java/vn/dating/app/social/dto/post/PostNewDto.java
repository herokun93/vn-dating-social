package vn.dating.app.social.dto.post;

import lombok.*;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PostNewDto  {
    private String content;
    private String title;
    private boolean anonymous;
    private String community;
    @NotBlank
    private Flux<FilePart> files;
}
