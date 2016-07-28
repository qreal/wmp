package com.qreal.robots.model.auth;

import com.qreal.robots.model.auth.serial.UserSerial;
import com.qreal.robots.model.robot.Robot;
import com.qreal.robots.thrift.gen.TUser;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * User in authorization service.
 */
public class User {

    /**
     * Name of user (primary key too).
     */
    private String username;

    /**
     * Hash of user password.
     */
    private String password;

    /**
     * Is user banned.
     */
    private Boolean enabled;

    /**
     * Roles of user.
     */
    private Set<UserRole> roles = new HashSet<UserRole>();

    /**
     * User's robots.
     */
    private Set<Robot> robots = new HashSet<Robot>();

    public User() {
    }

    public User(String username, String password, boolean enabled) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }

    /**
     * User constructor (except robots).
     */
    public User(String username, String password,
                boolean enabled, Set<UserRole> userRole) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.roles = userRole;
    }

    /**
     * Full User constructor.
     */
    public User(String username, String password, boolean enabled, Set<UserRole> userRole, Set<Robot> robots) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.roles = userRole;
        this.robots = robots;
    }

    /**
     * Constructor-converter from Thrift TUser to User.
     */
    public User(TUser tUser) {
        if (tUser.isSetUsername()) {
            username = tUser.getUsername();
        }

        if (tUser.isSetPassword()) {
            password = tUser.getPassword();
        }

        if (tUser.isSetEnabled()) {
            enabled = tUser.isEnabled();
        }

        if (tUser.isSetRoles()) {
            roles = tUser.getRoles().stream().map(tUserRole -> new UserRole(tUserRole, this)).collect(Collectors.
                    toSet());
        }

        if (tUser.isSetRobots()) {
            robots = tUser.getRobots().stream().map(tRobot -> new Robot(tRobot, this)).collect(Collectors.toSet());
        }
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<UserRole> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

    public Set<Robot> getRobots() {
        return this.robots;
    }

    public void setRobots(Set<Robot> robots) {
        this.robots = robots;
    }

    /**
     * Converter from User to Thrift TUser.
     */
    public TUser toTUser() {
        TUser tUser = new TUser();

        if (username != null) {
            tUser.setUsername(username);
        }

        if (password != null) {
            tUser.setPassword(password);
        }

        if (enabled != null) {
            tUser.setEnabled(enabled);
        }

        if (roles != null) {
            tUser.setRoles(getRoles().stream().map(UserRole::toTUserRole).collect(Collectors.toSet()));
        }

        if (robots != null) {
            tUser.setRobots(getRobots().stream().map(Robot::toTRobot).collect(Collectors.toSet()));
        }

        return tUser;
    }
}
