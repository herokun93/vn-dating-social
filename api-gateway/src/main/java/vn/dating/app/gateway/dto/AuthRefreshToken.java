package vn.dating.app.gateway.dto;


import lombok.*;

@Getter
@Setter
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRefreshToken {
    private String refreshToken;
}
