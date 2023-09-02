package vn.dating.app.social.dto.community;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class CommunityMemberDto {
    @NotBlank(message = "Name cannot be blank")
    @Size(min=3,max = 255, message = "Name must be less than or equal to 255 characters")
    @Pattern(regexp = "^[a-z0-9]+$", message = "Name must consist of lowercase letters (a-z) and digits (0-9) only, with no spaces")
    private String name;


}
