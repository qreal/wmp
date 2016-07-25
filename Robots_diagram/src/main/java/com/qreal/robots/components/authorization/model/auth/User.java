package com.qreal.robots.components.authorization.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qreal.robots.components.dashboard.model.robot.Robot;
import com.qreal.robots.thrift.gen.TRobot;
import com.qreal.robots.thrift.gen.TUser;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User {

    private String username;

    private String password;

    private Boolean enabled;

    @JsonIgnore
    private Set<UserRole> roles = new HashSet<>(0);

    @JsonIgnore
    private Set<Robot> robots = new HashSet<>(0);

    public User() {
    }

    public User(String username, String password, boolean enabled) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }

    public User(String username, String password,
                boolean enabled, Set<UserRole> userRole) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.roles = userRole;
    }

    public User(String username, String password, boolean enabled, Set<UserRole> userRole, Set<Robot> robots) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.roles = userRole;
        this.robots = robots;
    }

    public  User(TUser tUser) {
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
            roles = tUser.getRoles().stream().map(tUserRole -> new UserRole(tUserRole, this)).collect(Collectors
                    .toSet());
        }

        if (tUser.isSetRobots()) {
            robots = tUser.getRobots().stream().map(tRobot -> new Robot(tRobot, this)).collect(Collectors.toSet());
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
    public Set<UserRole> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "owner")
    public Set<Robot> getRobots() {
        return this.robots;
    }

    public void setRobots(Set<Robot> robots) {
        this.robots = robots;
    }

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