package com.qreal.wmp.dashboard.common.auth;

import com.racquettrack.security.oauth.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.security.core.userdetails.User;

import java.net.URISyntaxException;

/** Package of beans for OAuth authentication point support.*/
@Configuration
@PropertySource("classpath:oauthEndpoint.properties")
public class OAuthBeans {

    /** Processor for @Value annotations linked to property files.*/
    @Bean(name = "propertyPlaceholder")
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    /**
     * Properties is a catalogue of REST authentication interface of OAuth authentication point.
     * Properties will be loaded from file from resources dir.
     */
    @Bean(name = "properties")
    @DependsOn("propertyPlaceholder")
    public OAuth2ServiceProperties oAuth2ServiceProperties(
            @Value("${oauth.accessTokenUri}") String accessTokenUri,
            @Value("${oauth.userAuthorizationUri}") String userAuthorizationUri,
            @Value("${oauth.userInfoUri}") String userInfoUri,
            @Value("${oauth.clientId}") String clientId,
            @Value("${oauth.clientSecret}") String clientSecret,
            @Value("${oauth.redirectURI}") String redirectURI
    ) throws URISyntaxException {
        OAuth2ServiceProperties details = new OAuth2ServiceProperties();
        details.setUserAuthorisationUri(userAuthorizationUri);
        details.setRedirectUri(redirectURI);
        details.setAccessTokenUri(accessTokenUri);
        details.setClientId(clientId);
        details.setClientSecret(clientSecret);
        details.setUserInfoUri(userInfoUri);
        return details;
    }

    /** An adapter from Spring Framework AuthenticationUserDetailsService interface to OAuth2UserDetailsLoader.*/
    @Bean
    @Autowired
    public OAuth2UserDetailsService<User> oAuth2UserDetailsService(OAuth2ServiceProperties serviceProperties,
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

    /** AuthenticationProvider implementing OAuth authorization_code strategy.*/
    @Bean(name = "provider")
    @Autowired
    public OAuth2AuthenticationProvider oAuth2AuthenticationProvider(OAuth2ServiceProperties serviceProperties,
                                                                     OAuth2UserDetailsService<User> details)
            throws URISyntaxException {
        OAuth2AuthenticationProvider authProv = new OAuth2AuthenticationProvider();
        authProv.setAuthenticatedUserDetailsService(details);
        authProv.setoAuth2ServiceProperties(serviceProperties);
        return authProv;
    }

    /** OAuth entry point will redirect user to OAuth authentication point with correct parameters.*/
    @Bean
    @Autowired
    public OAuth2AuthenticationEntryPoint oAuth2AuthenticationEntryPoint(OAuth2ServiceProperties serviceProperties)
            throws URISyntaxException {
        OAuth2AuthenticationEntryPoint entry = new OAuth2AuthenticationEntryPoint();
        entry.setoAuth2ServiceProperties(serviceProperties);
        return entry;
    }

}
