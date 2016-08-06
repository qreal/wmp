package com.qreal.wmp.auth.database.users;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;

/**
 * This is a class of user.
 * This class also used as entity in database.
 */
@Entity
@Table(name = "USERS")
public class User implements Serializable, UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "LOGIN")
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.EAGER)
    private Collection<UserAuthority> authorities;

    public User(){
    }

    public User(String username, String password, Collection<GrantedAuthority> authority) {
        this.username = username;
        this.password = password;
        setAuthorities(authority);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<GrantedAuthority> getAuthorities() {
        return (Collection<GrantedAuthority>) (Collection<?>) authorities;
    }

    /**
     * Serialize authorities in strings and returns.
     */
    public Collection<String> getAuthoritiesInStringList() {
        Collection<String> collection = new ArrayList<String>();
        for (UserAuthority authority : authorities) {
            collection.add(authority.getAuthority());
        }
        return collection;
    }

    /**
     * Sets authorities (converting from GrantedAuthority).
     */
    public void setAuthorities(Collection<GrantedAuthority> authorities) {
        for (GrantedAuthority authority : authorities) {
            UserAuthority authorityCasted = (UserAuthority) authority;
            authorityCasted.setUser(this);
        }
        this.authorities = (Collection<UserAuthority>) (Collection<?>) authorities;
    }

    public boolean isEnabled() {
        return true;
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isAdmin() {
        return getAuthoritiesInStringList().contains("ROLE_ADMIN");
    }
}




