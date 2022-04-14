package com.storeum.auth;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AccessChecker {

    public boolean hasUserId(Authentication authentication, Long userId) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getId().equals(userId);
    }
}
