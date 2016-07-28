package com.qreal.robots.model.auth.serial;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qreal.robots.model.auth.User;
import com.qreal.robots.model.auth.UserRole;
import com.qreal.robots.model.robot.Robot;
import com.qreal.robots.thrift.gen.TUser;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * User in authorization service.
 */
@Entity
@Table(name = "users")
public class UserSerial {

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
    @JsonIgnore
    private Set<UserRoleSerial> roles = new HashSet<>(0);

    /**
     * User's robots.
     */
    @JsonIgnore
    private Set<Long> robots = new HashSet<>(0);

    public UserSerial() {
    }

    public UserSerial(String username, String password, boolean enabled) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }

    /**
     * User constructor (except robots).
     */
    public UserSerial(String username, String password,
                boolean enabled, Set<UserRoleSerial> userRole) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.roles = userRole;
    }

    /**
     * Full User constructor.
     */
    public UserSerial(String username, String password, boolean enabled, Set<UserRoleSerial> userRole, Set<Long> robots) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.roles = userRole;
        this.robots = robots;
    }

    /**
     * Constructor-converter from Thrift TUser to User.
     */
    public UserSerial(User user) {

        if (user.getUsername() != null) {
            username = user.getUsername();
        }

        if (user.getPassword() != null) {
            password = user.getPassword();
        }

        enabled = user.isEnabled();

        if (user.getRoles() != null) {
            roles = user.getRoles().stream().map(role -> new UserRoleSerial(role, this)).collect(Collectors.toSet());
        }
    }

    @Id
    @Column(name = "username", unique = true,
            nullable = false, length = 45)
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "password",
            nullable = false, length = 60)
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "enabled", nullable = false)
    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    public Set<UserRoleSerial> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<UserRoleSerial> roles) {
        this.roles = roles;
    }

    @ElementCollection
    public Set<Long> getRobots() {
        return this.robots;
    }

    public void setRobots(Set<Long> robots) {
        this.robots = robots;
    }

    public User toUser() {
        User user = new User();

        if (username != null) {
            user.setUsername(username);
        }

        if (password != null) {
            user.setPassword(password);
        }

        user.setEnabled(enabled);

        if (roles != null) {
            user.setRoles(roles.stream().map(role -> role.toUserRole(user)).collect(Collectors.toSet()));
        }

        user.setRobots(new HashSet<Robot>());

        return user;
    }

}
