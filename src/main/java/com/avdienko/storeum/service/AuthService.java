package com.avdienko.storeum.service;

import com.avdienko.storeum.auth.UserDetailsImpl;
import com.avdienko.storeum.auth.jwt.JwtUtils;
import com.avdienko.storeum.exception.ResourceNotFoundException;
import com.avdienko.storeum.exception.RefreshTokenException;
import com.avdienko.storeum.model.ValidationResult;
import com.avdienko.storeum.model.entity.EmailConfirmToken;
import com.avdienko.storeum.model.entity.RefreshToken;
import com.avdienko.storeum.model.entity.Role;
import com.avdienko.storeum.model.entity.User;
import com.avdienko.storeum.payload.request.LoginRequest;
import com.avdienko.storeum.payload.request.LogoutRequest;
import com.avdienko.storeum.payload.request.RefreshTokenRequest;
import com.avdienko.storeum.payload.request.RegisterRequest;
import com.avdienko.storeum.payload.response.GenericResponse;
import com.avdienko.storeum.payload.response.JwtResponse;
import com.avdienko.storeum.payload.response.RefreshTokenResponse;
import com.avdienko.storeum.repository.RoleRepository;
import com.avdienko.storeum.repository.UserRepository;
import com.avdienko.storeum.validator.UserValidator;
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

import static com.avdienko.storeum.model.ValidationStatus.FAIL;
import static com.avdienko.storeum.model.entity.ERole.ROLE_USER;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final UserValidator validator;
    private final EmailConfirmationService emailConfirmationService;
    private final MailService mailService;

    public JwtResponse auth(LoginRequest request) {
        log.info("Login request received for user with email={}", request.getEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        MDC.put("userId", String.valueOf(userDetails.getId()));

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String jwt = jwtUtils.generateJwt(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        log.info("Logged in successfully");

        return JwtResponse.builder()
                .accessToken(jwt)
                .refreshToken(refreshToken.getToken())
                .id(userDetails.getId())
                .firstName(userDetails.getFirstName())
                .email(userDetails.getEmail())
                .roles(roles)
                .build();
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

        String errorMessage = String.format("Role with value=%s was not found in DB", ROLE_USER);
        Role userRole = roleRepository.findByName(ROLE_USER)
                .orElseThrow(() -> new ResourceNotFoundException(errorMessage)
                );

        String encodedPwd = encoder.encode(request.getPassword());
        User user = User.builder()
                .firstName(request.getFirstName())
                .email(request.getEmail())
                .password(encodedPwd)
                .roles(Collections.singletonList(userRole))
                .isEnabled(false)
                .build();

        userRepository.save(user);

        MDC.put("userId", String.valueOf(user.getId()));
        log.info("User was created, user={} ", user);

        EmailConfirmToken token = emailConfirmationService.createToken(user.getId());
        mailService.send(request.getEmail(), user.getFirstName(), token.getToken());

        return new GenericResponse<>(user, HttpStatus.CREATED);
    }

    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        log.info("Refresh token request received for token={}", requestRefreshToken);

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromEmail(user.getFirstName());
                    log.info("Token was refreshed successfully");
                    return new RefreshTokenResponse(token, requestRefreshToken);
                })
                .orElseThrow(() -> new RefreshTokenException(requestRefreshToken, "Refresh token is not in database"));
    }

    public String logout(LogoutRequest request) {
        refreshTokenService.deleteByUserId(request.getUserId());
        log.info("Logged out successfully");
        return "Log out successful";
    }
}
