package vn.dating.app.gateway.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

    @NotBlank
    @Size(max = 50)
    private String password;

    @NotBlank
    @Size(max = 50,min = 2)

    private String username;
}
