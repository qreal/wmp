package com.qreal.wmp.dashboard.database.users.model;

import com.qreal.wmp.thrift.gen.TUserRole;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/** UserRole in authorization service.*/
@Data
@EqualsAndHashCode(exclude = "user")
@ToString(exclude = "user")
public class UserRole {
    /** Surrogate key for role (maybe static table for roles?). */
    private Integer id;

    /** Owner of the role. */
    private User user;

    /** Name of the role. */
    private String role;

    public UserRole() {
    }

    public UserRole(User user, String role) {
        this.user = user;
        this.role = role;
    }

    /** Full UserRole constructor.*/
    public UserRole(int id, User user, String role) {
        this(user, role);
        this.id = id;
    }

    /** Constructor-converter from Thrift TUserRole to UserRole.*/
    public UserRole(TUserRole tUserRole, User user) {
        if (tUserRole.isSetId()) {
            id = tUserRole.getId();
        }

        if (tUserRole.isSetRole()) {
            role = tUserRole.getRole();
        }

        this.user = user;
    }

    /** Converter from UserRole to Thrift TUserRole.*/
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
