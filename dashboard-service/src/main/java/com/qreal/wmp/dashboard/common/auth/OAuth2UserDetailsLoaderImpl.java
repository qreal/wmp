package com.qreal.wmp.dashboard.common.auth;

import com.qreal.wmp.dashboard.database.exceptions.Aborted;
import com.qreal.wmp.dashboard.database.exceptions.ErrorConnection;
import com.qreal.wmp.dashboard.database.exceptions.NotFound;
import com.qreal.wmp.dashboard.database.users.client.UserService;
import com.qreal.wmp.dashboard.database.users.model.User;
import com.qreal.wmp.dashboard.database.users.model.UserRole;
import com.racquettrack.security.oauth.OAuth2UserDetailsLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/** Handles requests for user operations from OAuth authentication points.*/
@Component
public class OAuth2UserDetailsLoaderImpl implements OAuth2UserDetailsLoader<UserDetails, String> {

    public static final String ROLE_USER = "ROLE_USER";

    private static final Logger logger = LoggerFactory.getLogger(OAuth2UserDetailsLoaderImpl.class);

    private final UserService userService;

    @Autowired
    public OAuth2UserDetailsLoaderImpl(UserService userService) {
        this.userService = userService;
    }

    /**
     * Finds user using userService for OAuth authentication point.
     * If user was found point will authenticate session of this user.
     * If user was not found point will create new user.
     */
    @Override
    public UserDetails getUserByUserId(String id) {
        logger.trace("OAuth passed user {} for authentication", id);
        UserDetails userDetails = null;
        try {
            User user = userService.findByUserName(id);
            userDetails = convert(user);
            logger.trace("User {} was found", id);
        } catch (NotFound e) {
            logger.trace("User {} was not found", id);
        } catch (ErrorConnection e) {
            //TODO what to do in that case?
            logger.error("Fatal error: must find user but can't because of connection error with user service", id);
        }
        return userDetails;
    }

    /** Updates user retrieved from OAuth authentication point. Not used right now.*/
    @Override
    public UserDetails updateUser(UserDetails userDetails, Map<String, Object> userInfo) {
        logger.trace("OAuth tried to update user {}", userDetails.getUsername());
        logger.trace("No actual updating was performed for user {}.", userDetails.getUsername());
        return userDetails;
    }

    /**
     * Creates user retrieved from OAuth authentication point. User will be created if no user with this id was
     * found previously.
     */
    @Override
    public UserDetails createUser(String id, Map<String, Object> userInfo) {
        logger.trace("OAuth passed user {} for creation.", id);
        User user = new User(id, id, true);
        Set<UserRole> roles = new HashSet<>();
        UserRole userRole = new UserRole(user, ROLE_USER);
        roles.add(userRole);
        user.setRoles(roles);
        try {
            userService.save(user);
        } catch (Aborted e) {
            //TODO what to do in that case?
            logger.error("Fatal error: must create user but can't because operation was aborted.");
        } catch (ErrorConnection e) {
            //TODO what to do in that case?
            logger.error("Fatal error: must create user but can't because of connection error with user service.");
        }
        UserDetails userDetails = convert(user);
        logger.trace("User {} was created.", id);
        return userDetails;
    }

    /** Tells if user with such info allowed to register.*/
    @Override
    public boolean isCreatable(Map<String, Object> userInfo) {
        return true;
    }

    private org.springframework.security.core.userdetails.User convert(User user) {
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                user.isEnabled(), true, true, true, buildUserAuthority(user.getRoles()));
    }

    private List<GrantedAuthority> buildUserAuthority(Set<UserRole> userRoles) {
        Set<GrantedAuthority> setAuths = userRoles.stream().map(userRole ->
                new SimpleGrantedAuthority(userRole.getRole())).collect(Collectors.toSet());
        return new ArrayList<>(setAuths);
    }
}