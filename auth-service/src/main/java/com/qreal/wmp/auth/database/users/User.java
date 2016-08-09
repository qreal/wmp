package com.qreal.wmp.auth.database.users;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;

/** User in auth service. It can be admin or plain user.*/
@Entity
@Table(name = "Users")
public class User implements Serializable, UserDetails {
    /** Name of user.*/
    @Id
    @Column(name = "Username")
    private String username;

    /** Password of user.*/
    @Column(name = "Password")
    private String password;

    /** Security authorities of user.*/
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "authorities", referencedColumnName = "Username")
    private Collection<UserAuthority> authorities;

    public User(){
    }

    public User(String username, String password, Collection<GrantedAuthority> authority) {
        this.username = username;
        this.password = password;
        setAuthorities(authority);
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

    /** Serializes authorities in strings and returns.*/
    public Collection<String> getAuthoritiesInStringList() {
        Collection<String> collection = new ArrayList<String>();
        for (UserAuthority authority : authorities) {
            collection.add(authority.getAuthority());
        }
        return collection;
    }

    /** Sets authorities (converting from GrantedAuthority).*/
    public void setAuthorities(Collection<GrantedAuthority> authorities) {
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




