package com.qreal.wmp.auth.security.oauth;

import com.qreal.wmp.auth.database.users.UserAuthority;
import com.racquettrack.security.oauth.OAuth2UserDetailsLoader;
import com.qreal.wmp.auth.database.users.User;
import com.qreal.wmp.auth.database.users.UserDAO;
import com.qreal.wmp.auth.security.utils.RandomStringGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/** Handles requests for user operations from OAuth authentication points.*/
@Component
public class OAuth2UserDetailsLoaderImpl implements OAuth2UserDetailsLoader<UserDetails, String> {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2UserDetailsLoaderImpl.class);

    @Resource(name = "userService")
    private UserDAO userService;

    @Resource(name = "passwordEncoder")
    private PasswordEncoder passwordEncoder;

    /**
     * Finds user using userService for OAuth authentication point.
     * If user was found point will authenticate session of this user.
     * If user was not found point will create new user.
     */
    @Override
    public UserDetails getUserByUserId(String id) {
        //TODO Here is vulnarebility, cause someone can login with id from github to user registrated with google.
        //TODO But Google and Github makes email verification so it is not really bad. But in future we need to fix it.
        logger.trace("Someone trying to login through oauth with {} username", id);
        UserDetails user = userService.loadUserByUsername(id);
        logger.trace("User {} was found", id);
        return user;
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

        //Creating random password, login via standard login form shouldn't be accessible for oauth registrated users
        String password = RandomStringGenerator.generateString(20);

        GrantedAuthority authority = new UserAuthority("ROLE_USER");
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(authority);

        User user = new User(id, passwordEncoder.encode(password), authorities);
        userService.add(user);
        logger.trace("User {} was created.", id);
        return user;
    }

    /** Tells if user with such info allowed to register.*/
    @Override
    public boolean isCreatable(Map<String, Object> userInfo) {
        return true;
    }
}
