package com.resources.auth.security.oauth;

import com.racquettrack.security.oauth.OAuth2UserDetailsLoader;
import com.resources.auth.database.users.User;
import com.resources.auth.database.users.UserAuthority;
import com.resources.auth.database.users.UserDAO;
import com.resources.auth.security.utils.RandomStringGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by tanvd on 03.04.16.
 */
public class OAuth2UserDetailsLoaderImpl implements OAuth2UserDetailsLoader {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2UserDetailsLoaderImpl.class);

    @Resource(name = "userService")
    UserDAO userService;

    @Resource(name="passwordEncoder")
    PasswordEncoder passwordEncoder;


    /**
     * Retrieves the {@link UserDetails} object.
     *
     * @param id The ID of the user in the OAuth Provider's system.
     * @return The {@link UserDetails} of the user if it exists, or null if it doesn't.
     */
    @Override
    public UserDetails getUserByUserId(Object id) {
        //TODO Here is vulnarebility, cause someone can login with id from github to user registrated with google.
        //TODO But Google and Github makes email verification so it is not really bad. But in future we need to fix it.
        logger.trace("Someone trying to login through oauth with {} username", id.toString());
        UserDetails user = userService.loadUserByUsername(id.toString());
        return user;
    }

    /**
     * Update the user with the information from the external system.
     *
     * @param userDetails The {@link UserDetails} object.
     * @param userInfo    The user info object returned from the OAuth Provider.
     */
    @Override
    public UserDetails updateUser(UserDetails userDetails, Map userInfo) {
        return userDetails;
    }

    /**
     * Creates a new user in the internal system. The internal system should store the {@param id} of the User to
     * establish a link between the OAuth Provider and the internal system.
     *
     * @param id       The id of the user given by the OAuth Provider.
     * @param userInfo The user info object returned from the OAuth Provider.
     * @return The created {@link UserDetails} object.
     */
    @Override
    public UserDetails createUser(Object id, Map userInfo) {
        if (!userService.get(id.toString()).isEmpty()) {
            throw  new IllegalArgumentException("Registration defined needed by oauth, but user already registered");
        }

        //Creating random password, login via standard login form shouldn't be accessible for oauth registrated users
        String password = RandomStringGenerator.generateString(20);


        User user = new User();
        user.setUsername(id.toString());
        user.setPassword(passwordEncoder.encode(password));

        GrantedAuthority authority = new UserAuthority("ROLE_USER");
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        authorities.add(authority);
        user.setAuthorities(authorities);
        userService.add(user);
        logger.trace("New user created from oauth with {} username", id.toString());
        return user;
    }

    /**
     * Expected to be called only when the user described by {@param userInfo} has already been determined to not
     * exist in the system. Implementations should return true when it is okay for the user to be created. For example,
     * implementations may want to return false if a significant time has elapsed between the user being created in
     * the OAuth Provider and now.
     *
     * @param userInfo A {@link Map} of the JSON user object returned by the OAuth Provider.
     * @return True if it is okay to create the user.
     */
    @Override
    public boolean isCreatable(Map userInfo) {
        return true;
    }
}
