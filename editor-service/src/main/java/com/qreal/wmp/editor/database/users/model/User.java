package com.qreal.wmp.editor.database.users.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qreal.wmp.editor.database.robots.model.Robot;
import com.qreal.wmp.thrift.gen.TUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/** User in authorization service.*/
@Data
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
