package vn.dating.app.social.dto.community.other;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommunitiesOtherDto {

    private List<CommunityOtherBase> data = new ArrayList<>();

}
