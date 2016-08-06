package com.qreal.wmp.auth.database.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class provides access to database for spring security.
 * It uses userService to get records from base.
 */
@Service("userServiceSec")
@Transactional
public class UserDAOSec implements UserDetailsService {

    @Autowired
    private UserDAO userService;

    public void setUserService(UserDAO userService) {
        this.userService = userService;
    }

    /**
     * Loads user from local DB by id.
     */
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        UserDetails user = userService.loadUserByUsername(login);
        if (user == null) {
            throw new UsernameNotFoundException("Username not found");
        }
        return user;
    }
}