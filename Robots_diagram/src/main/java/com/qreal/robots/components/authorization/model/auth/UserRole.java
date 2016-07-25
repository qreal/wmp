package com.qreal.robots.components.authorization.model.auth;

import com.qreal.robots.thrift.gen.TUserRole;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Struct represents UserRole.
 * field user -- owner of role
 * field role -- name of role
 * field userRoleID surrogate key for role (todo -- create static table of roles)
 */

@Entity
@Table(name = "user_roles",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"role", "owner"}))
public class UserRole {

    private Integer id;

    private User user;

    private String role;

    public UserRole() {
    }

    public UserRole(User user, String role) {
        this.user = user;
        this.role = role;
    }

    public UserRole(int id, User user, String role) {
        this.id = id;
        this.user = user;
        this.role = role;
    }

    public UserRole(TUserRole tUserRole, User user) {
        if (tUserRole.isSetId()) {
            id = tUserRole.getId();
        }

        if (tUserRole.isSetRole()) {
            role = tUserRole.getRole();
        }

        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id",
            unique = true, nullable = false)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner", nullable = false)
    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "role", nullable = false, length = 45)
    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public TUserRole toTUserRole() {
        TUserRole tUserRole = new TUserRole();

        if (id != null) {
            tUserRole.setId(id);
        }

        if (role != null) {
            tUserRole.setRole(role);
        }

        return tUserRole;
    }
}