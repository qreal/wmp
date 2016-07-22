package com.qreal.robots.components.authorization.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qreal.robots.components.dashboard.model.robot.Robot;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    private String username;

    private String password;

    private boolean enabled;

    @JsonIgnore
    private Set<UserRole> userRole = new HashSet<>(0);

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
        this.userRole = userRole;
    }

    public User(String username, String password, boolean enabled, Set<UserRole> userRole, Set<Robot> robots) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.userRole = userRole;
        this.robots = robots;
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
    public Set<UserRole> getUserRole() {
        return this.userRole;
    }

    public void setUserRole(Set<UserRole> userRole) {
        this.userRole = userRole;
    }


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "owner")
    public Set<Robot> getRobots() {
        return this.robots;
    }

    public void setRobots(Set<Robot> robots) {
        this.robots = robots;
    }
}