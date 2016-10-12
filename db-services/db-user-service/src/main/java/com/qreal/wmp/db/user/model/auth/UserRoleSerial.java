package com.qreal.wmp.db.user.model.auth;

import com.qreal.wmp.thrift.gen.TUserRole;
import lombok.Data;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/** UserRole in authorization service.*/
@Entity
@Table(name = "user_roles")
@Data
public class UserRoleSerial {

    /** Surrogate key for role (maybe static table for roles?).*/
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    /** Name of the role. */
    @Column(name = "role", nullable = false, length = 45)
    private String role;

    public UserRoleSerial() {
    }

    public UserRoleSerial(String role) {
        this.role = role;
    }

    /** Constructor-converter from Thrift TUserRole to UserRole.*/
    public UserRoleSerial(TUserRole tUserRole) {
        if (tUserRole.isSetId()) {
            id = tUserRole.getId();
        }

        if (tUserRole.isSetRole()) {
            role = tUserRole.getRole();
        }
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
