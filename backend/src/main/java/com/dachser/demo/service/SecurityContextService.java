package com.dachser.demo.service;
import org.springframework.stereotype.Service;

@Service
public class SecurityContextService {

    /**
     * Simulates extracting the user ID from a validated JWT token. or some other authentication mechanism.
     * When Spring Security is implemented, this will be replaced with:
     * return ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
     */
    public Long getCurrentUserId() {
        // Hardcoded to our default 'system_admin' user ID inserted via SQL
        return 1L;
    }

    /**
     * Simulates extracting the username from a validated JWT token. or some other authentication mechanism.
     * When Spring Security is implemented, this will be replaced with:
     * return ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
     */
    public String getCurrentUsername() {
        return "system_admin";
    }
}
