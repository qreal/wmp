package com.qreal.robots.common.auth;

import com.qreal.robots.database.users.client.UserService;
import com.qreal.robots.database.users.model.UserRole;
import com.racquettrack.security.oauth.OAuth2UserDetailsLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * OAuth requests handler.
 */
@Component
public class OAuth2UserDetailsLoaderImpl implements OAuth2UserDetailsLoader {

    public static final String ROLE_USER = "ROLE_USER";

    @Autowired
    private UserService userService;

    @Override
    public UserDetails getUserByUserId(Object id) {
        if (!userService.isUserExist((String) id)) {
            return null;
        } else {
            com.qreal.robots.database.users.model.User user = userService.findByUserName((String) id);
            return convert(user);
        }
    }

    @Override
    public UserDetails updateUser(UserDetails userDetails, Map userInfo) {
        return userDetails;
    }

    @Override
    public UserDetails createUser(Object id, Map userInfo) {
        com.qreal.robots.database.users.model.User user = new com.qreal.robots.database.users.model.User((String) id,
                (String) id, true);
        Set<UserRole> roles = new HashSet<UserRole>();
        UserRole userRole = new UserRole(user, ROLE_USER);
        roles.add(userRole);
        user.setRoles(roles);
        userService.save(user);
        UserDetails userDetails = convert(user);
        return userDetails;
    }

    @Override
    public boolean isCreatable(Map userInfo) {
        return true;
    }

    private User convert(com.qreal.robots.database.users.model.User user) {
        return new User(user.getUsername(), user.getPassword(), user.isEnabled(), true, true, true,
                buildUserAuthority(user.getRoles()));
    }

    private List<GrantedAuthority> buildUserAuthority(Set<UserRole> userRoles) {
        Set<GrantedAuthority> setAuths = userRoles.stream().map(userRole -> new SimpleGrantedAuthority(userRole.
                getRole())).collect(Collectors.toSet());
        return new ArrayList<GrantedAuthority>(setAuths);
    }
}