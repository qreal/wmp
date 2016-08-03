package com.resources.auth.config.oauth2.outproviders;

import com.racquettrack.security.oauth.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.access.ExceptionTranslationFilter;

import javax.annotation.Resource;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class GithubConfig {

    @Configuration
    @PropertySource("classpath:oauthGithub.properties")
    protected static class OAuthAuthentication {

        @Bean(name = "propertiesGithub")
        public OAuth2ServiceProperties oAuth2ServicePropertiesGithub(
                @Value("${github.accessTokenUri}") String accessTokenUri,
                @Value("${github.userAuthorizationUri}") String userAuthorizationUri,
                @Value("${github.userInfoUri}") String userInfoUri,
                @Value("${github.clientId}") String clientId,
                @Value("${github.clientSecret}") String clientSecret,
                @Value("${github.redirectURI}") String redirectURI,
                @Value("${github.scope}") String scope
        ) throws URISyntaxException {
            OAuth2ServiceProperties details = new OAuth2ServiceProperties();
            details.setUserAuthorisationUri(userAuthorizationUri);
            details.setRedirectUri(redirectURI);
            details.setAccessTokenUri(accessTokenUri);
            details.setClientId(clientId);
            details.setClientSecret(clientSecret);
            details.setUserInfoUri(userInfoUri);
            details.setUserIdName("email");

            Map<String, String> additionalParams = new HashMap<String, String>();
            additionalParams.put("scope", scope);
            details.setAdditionalAuthParams(additionalParams);

            return details;
        }

        @Bean(name = "userDetailsServiceGithub")
        @Autowired
        public OAuth2UserDetailsService<User> oAuth2UserDetailsServiceGithub(
                @Qualifier("propertiesGithub") OAuth2ServiceProperties serviceProperties,
                OAuth2UserDetailsLoader detailsLoader)
                throws URISyntaxException {
            OAuth2UserDetailsService service = new OAuth2UserDetailsService();
            service.setoAuth2ServiceProperties(serviceProperties);
            service.setoAuth2UserDetailsLoader(detailsLoader);
            DefaultOAuth2UserInfoProvider provider = new DefaultOAuth2UserInfoProvider();
            provider.setoAuth2ServiceProperties(serviceProperties);
            service.setoAuth2UserInfoProvider(provider);
            return service;
        }

        @Bean(name = "providerGithub")
        @Autowired
        public OAuth2AuthenticationProvider oAuth2AuthenticationProviderGithub(
                @Qualifier("propertiesGithub") OAuth2ServiceProperties serviceProperties,
                @Qualifier("userDetailsServiceGithub") OAuth2UserDetailsService<User> details)
                throws URISyntaxException {
            OAuth2AuthenticationProvider authProv = new OAuth2AuthenticationProvider();
            authProv.setAuthenticatedUserDetailsService(details);
            authProv.setoAuth2ServiceProperties(serviceProperties);
            return authProv;
        }

        @Bean(name = "entryPointGithub")
        @Autowired
        public OAuth2AuthenticationEntryPoint oAuth2AuthenticationEntryPointGithub(
                @Qualifier("propertiesGithub") OAuth2ServiceProperties serviceProperties)
                throws URISyntaxException {
            OAuth2AuthenticationEntryPoint entry = new OAuth2AuthenticationEntryPoint();
            entry.setoAuth2ServiceProperties(serviceProperties);
            return entry;
        }
    }

    @Configuration("OAuthGithubAuth")
    @Order(98)
    protected static class OAuthGithubAuth extends WebSecurityConfigurerAdapter {

        @Resource(name = "filterGithub")
        OAuth2AuthenticationFilter filterGithub;

        @Resource(name = "entryPointGithub")
        OAuth2AuthenticationEntryPoint oAuthEntryPointGithub;

        @Bean(name = "filterGithub")
        @Autowired
        public OAuth2AuthenticationFilter oAuth2AuthenticationFilter(
                @Qualifier("propertiesGithub") OAuth2ServiceProperties serviceProperties,
                AuthenticationManager manager)
                throws Exception {
            OAuth2AuthenticationFilter filter = new OAuth2AuthenticationFilter("/oauth/callback/github");
            filter.setAuthenticationManager(manager);
            filter.setoAuth2ServiceProperties(serviceProperties);
            return filter;
        }

        @Override
        @DependsOn("filterGithub")
        protected void configure(HttpSecurity httpSecurity) throws Exception {

            httpSecurity.authorizeRequests().
                    and().
                    requestMatchers().antMatchers("/oauth/callback/github", "/oauth/github**").
                    and().
                    authorizeRequests().
                    antMatchers("/oauth/callback/github").permitAll().
                    antMatchers("/oauth/github**").authenticated().
                    and().
                    httpBasic().
                    authenticationEntryPoint(oAuthEntryPointGithub).
                    and().
                    addFilterAfter(filterGithub, ExceptionTranslationFilter.class).
                    csrf().disable();

        }
    }
}
