package com.storeum.auth;

import com.storeum.model.entity.OAuthExchangeToken;
import com.storeum.model.entity.User;
import com.storeum.service.OAuthExchangeTokenService;
import com.storeum.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;
    private final OAuthExchangeTokenService oAuthExchangeTokenService;

    @Value("${app.client-base-url}")
    private String clientBaseUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        GoogleOAuthUser googleOAuthUser = new GoogleOAuthUser(oAuth2User);
        String token = createOauthExchangeToken(googleOAuthUser.getEmail());
        String redirectUrl = String.format("%s/oauth/success?token=%s", clientBaseUrl, token);

        super.clearAuthenticationAttributes(request);
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

    private String createOauthExchangeToken(String userEmail) {
        User user = userService.getUserByEmail(userEmail);
        MDC.put("userId", String.valueOf(user.getId()));
        OAuthExchangeToken token = oAuthExchangeTokenService.createToken(user);
        return token.getToken();
    }
}
