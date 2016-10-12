package com.qreal.wmp.db.user.model.auth;

import com.qreal.wmp.thrift.gen.TUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/** User in authorization service.*/
@Entity
@Table(name = "users")
@Data
public class UserSerial {
    /** Name of the user (primary key too).*/
    @Id
    @Column(name = "username", unique = true, nullable = false, length = 45)
    private String username;

    /** Hash of the user's password.*/
    @Column(name = "password", nullable = false, length = 60)
    private String password;

    /** Is the user banned.*/
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    /** Roles of the user.*/
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "role_id", referencedColumnName = "username")
    private Set<UserRoleSerial> roles = new HashSet<>();

    /** User's robots.*/
    @ElementCollection
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
