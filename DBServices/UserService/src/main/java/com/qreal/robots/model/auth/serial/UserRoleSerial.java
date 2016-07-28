package com.qreal.robots.model.auth.serial;

import com.qreal.robots.model.auth.User;
import com.qreal.robots.model.auth.UserRole;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * UserRole in authorization service.
 */
@Entity
@Table(name = "user_roles",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"role", "owner"}))
public class UserRoleSerial {

    /**
     * Surrogate key for role (maybe static table for roles?).
     */
    private Integer id;

    /**
     * Owner of role.
     */
    private UserSerial user;

    /**
     * Name of role.
     */
    private String role;

    public UserRoleSerial() {
    }

    public UserRoleSerial(UserSerial user, String role) {
        this.user = user;
        this.role = role;
    }

    /**
     * Full UserRole constructor.
     */
    public UserRoleSerial(int id, UserSerial user, String role) {
        this.id = id;
        this.user = user;
        this.role = role;
    }

    /**
     * Constructor-converter from Thrift TUserRole to UserRole.
     */
    public UserRoleSerial(UserRole userRole, UserSerial user) {
        if (userRole.getId() != null) {
            id = userRole.getId();
        }

        if (userRole.getRole() != null) {
            role = userRole.getRole();
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
    public UserSerial getUser() {
        return this.user;
    }

    public void setUser(UserSerial user) {
        this.user = user;
    }

    @Column(name = "role", nullable = false, length = 45)
    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Converter from UserRole to Thrift TUserRole.
     */

    public UserRole toUserRole(User user){
        UserRole userRole = new UserRole();

        if (id != null) {
            userRole.setId(id);
        }

        if (role != null) {
            userRole.setRole(role);
        }

        userRole.setUser(user);
        return  userRole;
    }
}
