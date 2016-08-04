package com.qreal.robots.database.users.model;

import com.qreal.robots.thrift.gen.TUserRole;

/**
 * UserRole in authorization service.
 */
public class UserRole {

    /**
     * Surrogate key for role (maybe static table for roles?).
     */
    private Integer id;

    /**
     * Owner of role.
     */
    private User user;

    /**
     * Name of role.
     */
    private String role;

    public UserRole() {
    }

    public UserRole(User user, String role) {
        this.user = user;
        this.role = role;
    }

    /**
     * Full UserRole constructor.
     */
    public UserRole(int id, User user, String role) {
        this.id = id;
        this.user = user;
        this.role = role;
    }

    /**
     * Constructor-converter from Thrift TUserRole to UserRole.
     */
    public UserRole(TUserRole tUserRole, User user) {
        if (tUserRole.isSetId()) {
            id = tUserRole.getId();
        }

        if (tUserRole.isSetRole()) {
            role = tUserRole.getRole();
        }

        this.user = user;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Converter from UserRole to Thrift TUserRole.
     */
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
