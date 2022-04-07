package com.storeum.payload.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class JwtResponse {
    private String accessToken;
    private String refreshToken;
    private Long id;
    private String firstName;
    private String email;
    private List<String> roles;
}
