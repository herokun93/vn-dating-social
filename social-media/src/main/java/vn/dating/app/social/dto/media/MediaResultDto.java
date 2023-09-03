package vn.dating.app.social.dto.media;

import lombok.*;
import vn.dating.app.social.dto.community.CommunityResultDto;
import vn.dating.app.social.models.Community;
import vn.dating.app.social.models.Media;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MediaResultDto {
    private long id;
    private String path;
    private Instant createdAt;
    private Instant updatedAt;

    public static MediaResultDto fromEntity(Media media){
        MediaResultDto mediaResultDto = new MediaResultDto();

        mediaResultDto.setId(media.getId());
        mediaResultDto.setPath(media.getPath());
        mediaResultDto.setCreatedAt(media.getCreatedAt());
        mediaResultDto.setUpdatedAt(media.getUpdatedAt());

        return  mediaResultDto;
    }

    public static List<MediaResultDto> fromEntities(List<Media> mediaList) {
        return mediaList.stream()
                .map(MediaResultDto::fromEntity)
                .collect(Collectors.toList());
    }
}
