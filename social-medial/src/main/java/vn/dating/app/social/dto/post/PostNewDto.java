package vn.dating.app.social.dto.post;

import lombok.*;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;


import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostNewDto implements Serializable {
    private String content;
    private String title;
    @NotBlank
    private Flux<FilePart> files;
}
