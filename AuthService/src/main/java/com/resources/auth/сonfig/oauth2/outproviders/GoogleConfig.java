package com.resources.auth.сonfig.oauth2.outproviders;

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
public class GoogleConfig {
    @Configuration
    @PropertySource("classpath:oauthGoogle.properties")
    protected static class OAuthAuthentication {


        @Bean(name = "propertiesGoogle")
        public OAuth2ServiceProperties oAuth2ServicePropertiesGoogle(
                @Value("${google.accessTokenUri}") String accessTokenUri,
                @Value("${google.userAuthorizationUri}") String userAuthorizationUri,
                @Value("${google.userInfoUri}") String userInfoUri,
                @Value("${google.clientId}") String clientId,
                @Value("${google.clientSecret}") String clientSecret,
                @Value("${google.redirectURI}") String redirectURI,
                @Value("${google.scope}") String scope
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



        @Bean(name = "userDetailsServiceGoogle")
        @Autowired
        public OAuth2UserDetailsService<User> oAuth2UserDetailsServiceGoogle(@Qualifier("propertiesGoogle") OAuth2ServiceProperties serviceProperties,
                                                                       OAuth2UserDetailsLoader detailsLoader) throws URISyntaxException {
            OAuth2UserDetailsService service = new OAuth2UserDetailsService();
            service.setoAuth2ServiceProperties(serviceProperties);
            service.setoAuth2UserDetailsLoader(detailsLoader);
            DefaultOAuth2UserInfoProvider provider = new DefaultOAuth2UserInfoProvider();
            provider.setoAuth2ServiceProperties(serviceProperties);
            service.setoAuth2UserInfoProvider(provider);
            return service;
        }

        @Bean(name = "providerGoogle")
        @Autowired
        public OAuth2AuthenticationProvider oAuth2AuthenticationProviderGoogle(@Qualifier("propertiesGoogle") OAuth2ServiceProperties serviceProperties,
                                                                               @Qualifier("userDetailsServiceGoogle")OAuth2UserDetailsService<User> details)
                                                                               throws URISyntaxException {
            OAuth2AuthenticationProvider authProv = new OAuth2AuthenticationProvider();
            authProv.setAuthenticatedUserDetailsService(details);
            authProv.setoAuth2ServiceProperties(serviceProperties);
            return authProv;
        }

        @Bean(name = "entryPointGoogle")
        @Autowired
        public OAuth2AuthenticationEntryPoint oAuth2AuthenticationEntryPointGoogle(@Qualifier("propertiesGoogle") OAuth2ServiceProperties serviceProperties)
                                                                                   throws URISyntaxException {
            OAuth2AuthenticationEntryPoint entry = new OAuth2AuthenticationEntryPoint();
            entry.setoAuth2ServiceProperties(serviceProperties);
            return entry;
        }
    }

    @Configuration("OAuthGoogleAuth")
    @Order(99)
    protected static class OAuthGoogleAuth extends WebSecurityConfigurerAdapter {

        @Resource(name = "filterGoogle")
        OAuth2AuthenticationFilter filterGoogle;

        @Resource(name = "entryPointGoogle")
        OAuth2AuthenticationEntryPoint oAuthEntryPointGoogle;

        @Bean(name = "filterGoogle")
        @Autowired
        public OAuth2AuthenticationFilter oAuth2AuthenticationFilter(@Qualifier("propertiesGoogle") OAuth2ServiceProperties serviceProperties,
                                                                     AuthenticationManager manager) throws Exception {
            OAuth2AuthenticationFilter filter = new OAuth2AuthenticationFilter("/oauth/callback/google");
            filter.setAuthenticationManager(manager);
            filter.setoAuth2ServiceProperties(serviceProperties);
            return filter;
        }


        //.csrf() is optional, enabled by default, if using WebSecurityConfigurerAdapter constructor
        @Override
        @DependsOn("filterGoogle")
        protected void configure(HttpSecurity httpSecurity) throws Exception {

            httpSecurity.authorizeRequests()
                    .and()
                    .requestMatchers().antMatchers("/oauth/callback/google", "/oauth/google**")

                    .and()
                    .authorizeRequests()
                    .antMatchers("/oauth/callback/google").permitAll()
                    .antMatchers("/oauth/google**").authenticated()

                    .and()
                    .httpBasic()
                    .authenticationEntryPoint(oAuthEntryPointGoogle)
                    .and()
                    .addFilterAfter(filterGoogle, ExceptionTranslationFilter.class)
                    .csrf().disable();

        }
    }
}
