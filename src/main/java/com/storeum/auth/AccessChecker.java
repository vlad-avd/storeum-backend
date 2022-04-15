package com.storeum.auth;

import org.slf4j.MDC;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AccessChecker {

    public boolean hasUserId(Authentication authentication, Long userId) {
        if (authentication instanceof AnonymousAuthenticationToken) {
            return false;
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if (userDetails.getId().equals(userId)) {
            MDC.put("userId", String.valueOf(userId));
            return true;
        } else {
            return false;
        }
    }
}
