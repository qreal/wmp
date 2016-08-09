package com.qreal.wmp.auth.database.client;

import org.springframework.security.core.GrantedAuthority;

/** Client authority. Hardcoded for now.*/
public class ClientAuthority implements GrantedAuthority {
    @Override
    public String getAuthority() {
        return "ROLE_CLIENT";
    }
}
