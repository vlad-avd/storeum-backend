package com.avdienko.storeum.payload.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class JwtResponse {
    private String accessToken;
    private String refreshToken;
    private Long id;
    private String username;
    //TODO: are fields below really needed ?
    private String email;
    private List<String> roles;
}
