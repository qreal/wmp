package com.qreal.wmp.editor.common.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

/**
 * Represents authentication object of user.
 * It can return name of authenticated user, which interacts with the service.
 */
public class AuthenticatedUser {
    public static String getUserName() {
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return authenticatedUser.getUsername();
    }

}
