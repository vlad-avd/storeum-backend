package com.storeum.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.storeum.model.entity.User;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.Serial;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
public class CustomUserDetails implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String firstName;
    private String email;
    @JsonIgnore
    private String password;
    private boolean isEnabled;
    private Map<String, Object> attributes;

    private Collection<? extends GrantedAuthority> authorities;

    public static CustomUserDetails build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return CustomUserDetails.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .email(user.getEmail())
                .password(user.getPassword())
                .authorities(authorities)
                .isEnabled(user.isEnabled())
                .build();
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }
}