package vn.dating.app.social.dto.post;

import lombok.*;
import vn.dating.common.models.audit.DateAudit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostViewDto extends DateAudit implements Serializable {
    private String content;
    private Long id;
    private String authorId;
    private String authorUsername;
    private String authorFirstName;
    private String authorLastName;
    private String url;
    private long countLike;
    private long countComment;
    private String title;
    private List<String> media = new ArrayList<>();

}
