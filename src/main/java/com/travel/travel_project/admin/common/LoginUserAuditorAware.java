package com.travel.travel_project.admin.common;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

public class LoginUserAuditorAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return empty();
        }

        User user = (User) authentication.getPrincipal();
        return ofNullable(user.getUsername());
    }
}
