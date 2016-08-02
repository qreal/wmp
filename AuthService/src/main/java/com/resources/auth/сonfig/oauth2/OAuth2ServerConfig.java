package com.resources.auth.сonfig.oauth2;

import com.racquettrack.security.oauth.*;
import com.resources.auth.сonfig.oauth2.outproviders.GithubConfig;
import com.resources.auth.сonfig.oauth2.outproviders.GoogleConfig;
import com.resources.auth.security.oauth.OAuth2UserDetailsLoaderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import com.resources.auth.security.oauth.MyUserApprovalHandler;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
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
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.annotation.Resource;


@Configuration
@Import({GoogleConfig.class, GithubConfig.class})
public class OAuth2ServerConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

        @Override
        public void configure(HttpSecurity http) throws Exception {
            // @formatter:off
            http
                    // Since we want the protected resources to be accessible in the UI as well we need
                    // session creation to be allowed (it's disabled by default in 2.0.6)

                    .requestMatchers().antMatchers("/oauth/userInfo")
                    .and()
                    .authorizeRequests()
                    .antMatchers("/oauth/userInfo")
                    .access("#oauth2.hasScope('read') or (!#oauth2.isOAuth() and hasRole('ROLE_USER'))");

            // @formatter:on
        }

    }

    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

        @Autowired
        private TokenStore tokenStore;

        @Autowired
        private UserApprovalHandler userApprovalHandler;

        @Resource(name = "clientServiceSec")
        private ClientDetailsService clientServiceSec;

        @Autowired
        @Qualifier("authenticationManagerBean")
        private AuthenticationManager authenticationManager;


        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.withClientDetails(clientServiceSec);
        }


        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints.tokenStore(tokenStore).userApprovalHandler(userApprovalHandler)
                    .authenticationManager(authenticationManager);
        }


        @Override
        public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
            oauthServer.realm("authServer/robotsDiagram");

            //Needed to disable http-basic authentication in all oauth requests
            oauthServer.allowFormAuthenticationForClients();

        }

    }

    @Configuration
    protected static class Stuff {

        @Resource(name = "clientServiceSec")
        private ClientDetailsService clientServiceSec;

        @Autowired
        private TokenStore tokenStore;

        @Bean
        public ApprovalStore approvalStore() throws Exception {
            TokenApprovalStore store = new TokenApprovalStore();
            store.setTokenStore(tokenStore);
            return store;
        }

        @Bean
        @Lazy
        @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
        public MyUserApprovalHandler userApprovalHandler() throws Exception {
            MyUserApprovalHandler handler = new MyUserApprovalHandler();
            handler.setApprovalStore(approvalStore());
            handler.setRequestFactory(new DefaultOAuth2RequestFactory(clientServiceSec));
            handler.setClientDetailsService(clientServiceSec);
            handler.setUseApprovalStore(true);
            return handler;
        }

        @Bean
        public OAuth2UserDetailsLoader<User, SimpleGrantedAuthority> oAuth2UserDetailsLoader() {
            return new OAuth2UserDetailsLoaderImpl();
        }
    }



}