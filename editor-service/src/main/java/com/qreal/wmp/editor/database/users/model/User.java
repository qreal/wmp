package com.qreal.wmp.editor.database.users.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qreal.wmp.editor.database.robots.model.Robot;
import com.qreal.wmp.thrift.gen.TUser;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/** User in authorization service.*/
public class User {
    /** Name of the user (primary key too).*/
    private String username;

    /** Hash of the user's password.*/
    private String password;

    /** Is the user banned.*/
    private Boolean enabled;

    /** Roles of the user.*/
    @JsonIgnore
    private Set<UserRole> roles = new HashSet<>();

    /** User's robots.*/
    @JsonIgnore
    private Set<Robot> robots = new HashSet<>();

    public User() {
    }

    public User(String username, String password, boolean enabled) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }

    /** User constructor (except robots).*/
    public User(String username, String password,
                boolean enabled, Set<UserRole> userRole) {
        this(username, password, enabled);
        this.roles = userRole;
    }

    /** Full User constructor.*/
    public User(String username, String password, boolean enabled, Set<UserRole> userRole, Set<Robot> robots) {
        this(username, password, enabled, userRole);
        this.robots = robots;
    }

    /** Constructor-converter from Thrift TUser to User.*/
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

    public @NotNull Set<UserRole> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

    public @NotNull Set<Robot> getRobots() {
        return this.robots;
    }

    public void setRobots(Set<Robot> robots) {
        this.robots = robots;
    }

    /** Converter from User to Thrift TUser.*/
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
