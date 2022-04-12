package com.storeum.auth;

import com.storeum.model.entity.OAuthExchangeToken;
import com.storeum.model.entity.User;
import com.storeum.repository.OAuthExchangeTokenRepository;
import com.storeum.service.AuthService;
import com.storeum.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;
    private final OAuthExchangeTokenRepository oAuthExchangeTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = "http://localhost:3000/oauth/success";
        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        GoogleOAuthUser googleOAuthUser = new GoogleOAuthUser(oAuth2User);
        String token = createOauthExchangeToken(googleOAuthUser.getEmail());

        super.clearAuthenticationAttributes(request);
        response.sendRedirect(String.format("%s?token=%s", targetUrl, token));
    }

    private String createOauthExchangeToken(String userEmail) {
        User user = userService.getUserByEmail(userEmail);

        OAuthExchangeToken exchangeToken = OAuthExchangeToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .isExchanged(false)
                .build();

        oAuthExchangeTokenRepository.save(exchangeToken);

        return exchangeToken.getToken();
    }
}
