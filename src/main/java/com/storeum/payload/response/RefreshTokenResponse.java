package com.storeum.payload.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class RefreshTokenResponse {
    private String accessToken;
    private String refreshToken;
}
