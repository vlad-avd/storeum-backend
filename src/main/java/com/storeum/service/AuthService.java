package com.storeum.service;

import com.storeum.auth.CustomUserDetails;
import com.storeum.auth.jwt.JwtUtils;
import com.storeum.exception.ResourceNotFoundException;
import com.storeum.model.ValidationResult;
import com.storeum.model.entity.*;
import com.storeum.payload.request.*;
import com.storeum.payload.response.*;
import com.storeum.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.storeum.model.ValidationStatus.FAIL;
import static com.storeum.model.entity.ERole.ROLE_USER;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final UserValidator validator;
    private final EmailConfirmationService emailConfirmationService;
    private final MailService mailService;
    private final OAuthExchangeTokenService oAuthExchangeTokenService;

    public JwtResponse auth(LoginRequest request) {
        log.info("Login request received for user with email={}", request.getEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        MDC.put("userId", String.valueOf(userDetails.getId()));

        return buildJwtResponse(userDetails);
    }

    //TODO: does it work?
    @Transactional
    public GenericResponse<User> register(RegisterRequest request) {
        log.info("Register request received for user with email={}", request.getEmail());

        ValidationResult validationResult = validator.validateRegisterRequest(request);
        if (FAIL == validationResult.getValidationStatus()) {
            log.info("User register request validation failed, cause={}", validationResult.getErrorMessage());
            return new GenericResponse<>(validationResult.getErrorMessage());
        }

        Role userRole = roleService.getRoleByName(ROLE_USER);
        String encodedPwd = encoder.encode(request.getPassword());
        User user = User.builder()
                .firstName(request.getFirstName())
                .email(request.getEmail())
                .password(encodedPwd)
                .roles(Collections.singletonList(userRole))
                .isEnabled(false)
                .build();

        userService.save(user);

        MDC.put("userId", String.valueOf(user.getId()));

        EmailConfirmToken token = emailConfirmationService.createToken(user.getId());
        mailService.send(request.getEmail(), user.getFirstName(), token.getToken());

        return new GenericResponse<>(user, HttpStatus.CREATED);
    }

    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        log.info("Refresh token request received for token={}", requestRefreshToken);

        return refreshTokenService.getRefreshToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromEmail(user.getFirstName());
                    log.info("Token was refreshed successfully");
                    return new RefreshTokenResponse(token, requestRefreshToken);
                })
                .orElseThrow(
                        () -> new ResourceNotFoundException(String.format("Refresh token was not found for token=%s",
                                requestRefreshToken))
                );
    }

    public String logout(LogoutRequest request) {
        refreshTokenService.deleteByUserId(request.getUserId());
        log.info("Logged out successfully");
        return "Log out successful";
    }

    //TODO: check auth set up
    public JwtResponse exchangeOAuthToken(String token) {
        OAuthExchangeToken oAuthExchangeToken = oAuthExchangeTokenService.exchangeToken(token);
        User user = oAuthExchangeToken.getUser();
        MDC.put("userId", String.valueOf(user.getId()));

        CustomUserDetails userDetails = CustomUserDetails.build(user);
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return buildJwtResponse(userDetails);
    }

    private JwtResponse buildJwtResponse(CustomUserDetails userDetails) {
        String jwt = jwtUtils.generateJwt(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        log.info("Logged in successfully");

        return JwtResponse.builder()
                .accessToken(jwt)
                .refreshToken(refreshToken.getToken())
                .id(userDetails.getId())
                .firstName(userDetails.getFirstName())
                .email(userDetails.getEmail())
                .build();
    }
}
