package com.qreal.wmp.dashboard.common.auth;

import com.racquettrack.security.oauth.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;

import java.net.URISyntaxException;

@Configuration
public class OAuthBeans {
    private String accessTokenUri = "http://localhost:8080/oauth/token";

    private String userAuthorizationUri = "http://localhost:8080/oauth/authorize";

    private String userInfoUri = "http://localhost:8080/oauth/userInfo";

    @Bean(name = "properties")
    public OAuth2ServiceProperties oAuth2ServiceProperties() throws URISyntaxException {
        OAuth2ServiceProperties details = new OAuth2ServiceProperties();
        details.setUserAuthorisationUri(userAuthorizationUri);
        details.setRedirectUri("http://localhost:9080/dashboard-service/oauth/callback");
        details.setAccessTokenUri(accessTokenUri);
        details.setClientId("dashboardService");
        details.setClientSecret("secret");
        details.setUserInfoUri(userInfoUri);
        return details;
    }

    @Bean
    @Autowired
    public OAuth2UserDetailsService<User> oAuth2UserDetailsService(OAuth2ServiceProperties serviceProperties,
                                                                   OAuth2UserDetailsLoader detailsLoader) throws
            URISyntaxException {
        OAuth2UserDetailsService service = new OAuth2UserDetailsService();
        service.setoAuth2ServiceProperties(serviceProperties);
        service.setoAuth2UserDetailsLoader(detailsLoader);
        DefaultOAuth2UserInfoProvider provider = new DefaultOAuth2UserInfoProvider();
        provider.setoAuth2ServiceProperties(serviceProperties);
        service.setoAuth2UserInfoProvider(provider);
        return service;
    }

    @Bean(name = "provider")
    @Autowired
    public OAuth2AuthenticationProvider oAuth2AuthenticationProvider(OAuth2ServiceProperties serviceProperties,
                                                                     OAuth2UserDetailsService<User> details) throws
            URISyntaxException {
        OAuth2AuthenticationProvider authProv = new OAuth2AuthenticationProvider();
        authProv.setAuthenticatedUserDetailsService(details);
        authProv.setoAuth2ServiceProperties(serviceProperties);
        return authProv;
    }

    @Bean
    @Autowired
    public OAuth2AuthenticationEntryPoint oAuth2AuthenticationEntryPoint(OAuth2ServiceProperties serviceProperties)
            throws URISyntaxException {
        OAuth2AuthenticationEntryPoint entry = new OAuth2AuthenticationEntryPoint();
        entry.setoAuth2ServiceProperties(serviceProperties);
        return entry;
    }

}
