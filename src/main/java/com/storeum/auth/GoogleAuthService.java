package com.storeum.auth;

import com.storeum.exception.ResourceNotFoundException;
import com.storeum.model.entity.Role;
import com.storeum.model.entity.User;
import com.storeum.repository.RoleRepository;
import com.storeum.service.AuthService;
import com.storeum.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Collections;

import static com.storeum.model.entity.ERole.ROLE_USER;

@Component
@RequiredArgsConstructor
public class GoogleAuthService extends DefaultOAuth2UserService {

    private final UserService userService;
    private final RoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        GoogleOAuthUser user = new GoogleOAuthUser(oAuth2User);
        googleOAuth(user);
        return user;
    }

    private void googleOAuth(GoogleOAuthUser oAuthUser) {
        try {
            userService.getUserByEmail(oAuthUser.getEmail());
        } catch (ResourceNotFoundException ex) {
            String errorMessage = String.format("Role with value=%s was not found in DB", ROLE_USER);
            Role userRole = roleRepository.findByName(ROLE_USER)
                    .orElseThrow(() -> new ResourceNotFoundException(errorMessage)
                    );

            User user = User.builder()
                    .firstName(oAuthUser.getName())
                    .email(oAuthUser.getEmail())
                    .roles(Collections.singletonList(userRole))
                    .isEnabled(false)
                    .build();

            userService.save(user);
        }
    }

}
