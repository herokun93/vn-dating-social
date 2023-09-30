package vn.dating.app.social.dto.like;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LikeNewPostDto {
    private String postUrl;
    private int react;
}
