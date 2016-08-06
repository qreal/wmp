package com.qreal.wmp.auth.database.client;

import org.springframework.security.core.GrantedAuthority;

//FIXME Hardcoded authorites
public class ClientAuthority implements GrantedAuthority {
    @Override
    public String getAuthority() {
        return "ROLE_CLIENT";
    }
}
