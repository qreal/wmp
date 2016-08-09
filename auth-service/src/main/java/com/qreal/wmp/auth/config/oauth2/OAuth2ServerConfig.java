package com.qreal.wmp.auth.config.oauth2;

import com.qreal.wmp.auth.config.oauth2.outproviders.GithubConfig;
import com.qreal.wmp.auth.config.oauth2.outproviders.GoogleConfig;
import com.qreal.wmp.auth.security.oauth.ClientsApprovalHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.annotation.Resource;

/**
 * Configuration for OAuth authentication endpoint. It includes configuration for resource server with userInfo
 * endpoint and configuration for authorization.
 */
@Configuration
@Import({GoogleConfig.class, GithubConfig.class})
public class OAuth2ServerConfig {
    /** Processor for @Value annotations linked to property files.*/
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    /** Configuration for OAuth resource server. This server owns userInfo endpoint.*/
    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.requestMatchers().antMatchers("/oauth/userInfo").
                    and().
                    authorizeRequests().
                    antMatchers("/oauth/userInfo").
                    access("#oauth2.hasScope('read') or (!#oauth2.isOAuth() and hasRole('ROLE_USER'))");
        }

    }

    /** Configuration for authorization of resources, such as UserInfo.*/
    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
        @Autowired
        private TokenStore tokenStore;

        @Autowired
        private org.springframework.security.oauth2.provider.approval.UserApprovalHandler clientsApprovalHandler;

        @Resource(name = "clientServiceSec")
        private ClientDetailsService clientServiceSec;

        @Autowired
        @Qualifier("authenticationManagerBean")
        private AuthenticationManager authenticationManager;

        /** Adding clientsServiceSec as repository for Clients search.*/
        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.withClientDetails(clientServiceSec);
        }

        /** Adding tokenStore, clientsApprovalHandler for clients and configured authenticationManager.*/
        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints.tokenStore(tokenStore).userApprovalHandler(clientsApprovalHandler).
                    authenticationManager(authenticationManager);
        }

        /** Disabling redundant http-basic authentication for clients.*/
        @Override
        public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
            //Needed to disable http-basic authentication in all oauth requests
            oauthServer.allowFormAuthenticationForClients();
        }

        @Bean
        public ApprovalStore approvalStore() throws Exception {
            TokenApprovalStore store = new TokenApprovalStore();
            store.setTokenStore(tokenStore);
            return store;
        }

        /** Creating ClientsApprovalHandler with link to clientsService.*/
        @Bean
        @Lazy
        @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
        public ClientsApprovalHandler userApprovalHandler() throws Exception {
            ClientsApprovalHandler handler = new ClientsApprovalHandler();
            handler.setApprovalStore(approvalStore());
            handler.setRequestFactory(new DefaultOAuth2RequestFactory(clientServiceSec));
            handler.setClientDetailsService(clientServiceSec);
            handler.setUseApprovalStore(true);
            return handler;
        }

    }
}