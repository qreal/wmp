package com.qreal.wmp.db.user.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qreal.wmp.thrift.gen.TUser;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/** User in authorization service.*/
@Entity
@Table(name = "users")
public class UserSerial {
    /** Name of user (primary key too).*/
    private String username;

    /** Hash of user password.*/
    private String password;

    /** Is user banned.*/
    private Boolean enabled;

    /** Roles of user.*/
    @JsonIgnore
    private Set<UserRoleSerial> roles = new HashSet<>();

    /** User's robots.*/
    @JsonIgnore
    private Set<Long> robots = new HashSet<>();

    public UserSerial() {
    }

    /** Constructor-converter from Thrift TUser to UserSerial (without robot).*/
    public UserSerial(TUser user) {

        if (user.isSetUsername()) {
            username = user.getUsername();
        }

        if (user.isSetPassword()) {
            password = user.getPassword();
        }

        if (user.isSetEnabled()) {
            enabled = user.isEnabled();
        }

        if (user.getRoles() != null) {
            roles = user.getRoles().stream().map(UserRoleSerial::new).collect(Collectors.toSet());
        }
    }

    @Id
    @Column(name = "username", unique = true, nullable = false, length = 45)
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "password", nullable = false, length = 60)
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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "role_id", referencedColumnName = "username")
    @NotNull
    public Set<UserRoleSerial> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<UserRoleSerial> roles) {
        this.roles = roles;
    }

    @ElementCollection
    @NotNull
    public Set<Long> getRobots() {
        return this.robots;
    }

    public void setRobots(Set<Long> robots) {
        this.robots = robots;
    }

    /** Converter from UserSerial to TUser (without robots).*/
    public TUser toTUser() {
        TUser tUser = new TUser();

        if (username != null) {
            tUser.setUsername(username);
        }

        if (password != null) {
            tUser.setPassword(password);
        }

        tUser.setEnabled(enabled);

        if (roles != null) {
            tUser.setRoles(roles.stream().map(UserRoleSerial::toTUserRole).collect(Collectors.toSet()));
        }

        tUser.setRobots(new HashSet<>());

        return tUser;
    }

}
