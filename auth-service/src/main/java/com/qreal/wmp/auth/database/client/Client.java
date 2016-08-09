package com.qreal.wmp.auth.database.client;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/** OAuth client. Normally it is services which uses this authentication point. */
@Entity
@Table(name = "Clients")
public class Client implements ClientDetails {
    /** Name of client.*/
    @Id
    @Column(name = "ClientId")
    private String clientId;

    /** Ids of resources.*/
    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection
    @Column(name = "ResourceId")
    private Set<String> resourceIds;

    /** Is secret required. Normally it's true.*/
    @Column(name = "SecretRequired")
    private boolean isSecretRequired;

    /** Secret of client.*/
    @Column(name = "ClientSecret")
    private String clientSecret;

    /** Does client have scopes. Normally it's true.*/
    @Column(name = "Scoped")
    private boolean isScoped;

    /** Scopes of client, such as "write", "read" and etc.*/
    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection
    @Column(name = "Scope")
    private Set<String> scope;

    /** Flows of OAuth which client allowed to use. Normally it authorization_code.*/
    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection
    @Column(name = "AuthorizedGrantType")
    private Set<String> authorizedGrantTypes;

    /** Redirects which can be used by this client. If empty all redirects allowed.*/
    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection
    @Column(name = "RegisteredRedirectUri")
    private Set<String> registeredRedirectUri;

    /** Life time of access token.*/
    @Column(name = "AccessTokenValiditySeconds")
    private Integer accessTokenValiditySeconds;

    /** Life time of refresh token.*/
    @Column(name = "RefreshTokenValiditySeconds")
    private Integer refreshTokenValiditySeconds;

    /** Should user manually grant permission for scopes of client.*/
    @Column(name = "AutoApprove")
    private boolean isAutoApprove;

    public Client() {
    }

    /** Full Client constructor.*/
    public Client(String clientId, boolean isSecretRequired, String clientSecret,
                  boolean isScoped, Set<String> scope, Set<String> authorizedGrantTypes,
                  Integer accessTokenValiditySeconds, Integer refreshTokenValiditySeconds,
                  boolean isAutoApprove) {
        this.clientId = clientId;
        this.isSecretRequired = isSecretRequired;
        this.clientSecret = clientSecret;
        this.isScoped = isScoped;
        this.scope = scope;
        this.authorizedGrantTypes = authorizedGrantTypes;
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
        this.isAutoApprove = isAutoApprove;
    }

    public String getClientId() {
        return clientId;
    }

    @Override
    public Set<String> getResourceIds() {
        return resourceIds;
    }

    @Override
    public boolean isSecretRequired() {
        return isSecretRequired;
    }

    @Override
    public String getClientSecret() {
        return clientSecret;
    }

    @Override
    public boolean isScoped() {
        return isScoped;
    }

    @Override
    public Set<String> getScope() {
        return scope;
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return authorizedGrantTypes;
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return registeredRedirectUri;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new ClientAuthority());
        return authorities;
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return accessTokenValiditySeconds;
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return refreshTokenValiditySeconds;
    }

    @Override
    public boolean isAutoApprove(String string) {
        return isAutoApprove;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return null;
    }

    public void setIdClient(String idClient) {
        this.clientId = idClient;
    }

    public void setResourceIds(Set<String> resourceIds) {
        this.resourceIds = resourceIds;
    }

    public void setSecretRequired(boolean secretRequired) {
        isSecretRequired = secretRequired;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public void setScoped(boolean scoped) {
        isScoped = scoped;
    }

    public void setScope(Set<String> scope) {
        this.scope = scope;
    }

    public void setAuthorizedGrantTypes(Set<String> authorizedGrantTypes) {
        this.authorizedGrantTypes = authorizedGrantTypes;
    }

    public void setRegisteredRedirectUri(Set<String> registeredRedirectUri) {
        this.registeredRedirectUri = registeredRedirectUri;
    }

    public void setAccessTokenValiditySeconds(Integer accessTokenValiditySeconds) {
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
    }

    public void setRefreshTokenValiditySeconds(Integer refreshTokenValiditySeconds) {
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
    }

    public void setAutoApprove(boolean autoApprove) {
        isAutoApprove = autoApprove;
    }

}
