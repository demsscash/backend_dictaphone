package com.personnel.personnelservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SecurityAuditorAware implements AuditorAware<String>  {
    private static final Logger logger = LoggerFactory.getLogger(SecurityAuditorAware.class);
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            logger.info("Utilisateur authentifié : " + authentication.getName());
            return Optional.of(authentication.getName());
        }
        logger.warn("Aucun utilisateur authentifié !");
        return Optional.of("anonymousUser");
    }
}
