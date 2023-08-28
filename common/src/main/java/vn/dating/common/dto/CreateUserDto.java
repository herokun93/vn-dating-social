package vn.dating.common.dto;

import lombok.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Getter
@Setter
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDto {
    private String id;

    @NotBlank
    @Size(min =2,max = 10)
    private String firstName;
    @NotBlank
    @Size(min =2,max = 10)
    private String lastName;
    @NotBlank
    @Size(min =2,max = 14)
    private String username;

    @Email
    @NotBlank
    private String email;

}
