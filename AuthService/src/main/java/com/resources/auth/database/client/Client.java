package com.resources.auth.database.client;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "Clients")
public class Client implements ClientDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "ClientId")
    private String clientId;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection
    @CollectionTable(name = "ResourceIds", joinColumns = @JoinColumn(name = "ID"))
    @Column(name = "ResourceId")
    private Set<String> resourceIds;

    @Column(name = "SecretRequired")
    boolean isSecretRequired;

    @Column(name = "ClientSecret")
    private String clientSecret;

    @Column(name = "Scoped")
    boolean isScoped;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection
    @CollectionTable(name = "Scopes", joinColumns = @JoinColumn(name = "ID"))
    @Column(name = "Scope")
    private Set<String> scope;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection
    @CollectionTable(name = "AuthorizedGrantTypes", joinColumns = @JoinColumn(name = "ID"))
    @Column(name = "AuthorizedGrantType")
    private Set<String> authorizedGrantTypes;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection
    @CollectionTable(name = "RegisteredRedirectUris", joinColumns = @JoinColumn(name = "ID"))
    @Column(name = "RegisteredRedirectUri")
    private Set<String> registeredRedirectUri;

    @Column(name = "AccessTokenValiditySeconds")
    private Integer accessTokenValiditySeconds;

    @Column(name = "RefreshTokenValiditySeconds")
    private Integer refreshTokenValiditySeconds;

    @Column(name = "AutoApprove")
    private boolean isAutoApprove;

    public Client() {
    }

    /**
     * Full Client constructor.
     */
    public Client(String clientId, boolean isSecretRequired, String clientSecret,
                  boolean isScoped, Set<String> scope, Set<String> authorizedGrantTypes,
                  Integer accessTokenValiditySeconds, Integer refreshTokenValiditySeconds,
                  boolean isAutoApprove)
    {
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

    public int getId() {
        return id;
    }
}
