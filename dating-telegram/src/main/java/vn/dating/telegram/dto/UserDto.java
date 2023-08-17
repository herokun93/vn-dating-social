package vn.dating.telegram.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long chatId;
    private String firstName;
    private String lastName;
    private String username;
}
