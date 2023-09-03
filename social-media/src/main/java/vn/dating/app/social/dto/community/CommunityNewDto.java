package vn.dating.app.social.dto.community;

import lombok.*;
import vn.dating.app.social.models.eenum.CommunityType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@Data
@NoArgsConstructor
public class CommunityNewDto {

    @NotBlank(message = "Name cannot be blank")
    @Size(min=3,max = 255, message = "Name must be less than or equal to 255 characters")
    @Pattern(regexp = "^[a-z0-9]+$", message = "Name must consist of lowercase letters (a-z) and digits (0-9) only, with no spaces")
    private String name;

    @NotBlank(message = "Description cannot be blank")
    @Size(max = 1000, message = "Description must be less than or equal to 1000 characters")
    private String description;
    private CommunityType type;
    private boolean approval;

}
