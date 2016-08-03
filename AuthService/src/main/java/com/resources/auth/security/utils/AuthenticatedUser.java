package com.resources.auth.security.utils;

import com.resources.auth.database.users.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * This class represent authentication object of user.
 * It can return name of authenticated user, which interacts with the service or
 * his UserAuthorities.
 */
public class AuthenticatedUser {
    /**
     * Retrieves name of authenticated user from security context of spring.
     */
    public static String getAuthenticatedUserName()
    {
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return authenticatedUser.getUsername();
    }

    /**
     * Retrieves user authorities of authenticated user from security context of spring.
     */
    public static String getAuthenticatedUserAuthority() {
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String auth = "";
        for (GrantedAuthority authority : authenticatedUser.getAuthorities()) {
            auth += authority.getAuthority() + " ";
        }
        return auth;
    }
}
