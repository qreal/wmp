package com.qreal.wmp.editor.common.config;

import com.qreal.wmp.editor.common.utils.PropertyLoader;
import com.racquettrack.security.oauth.OAuth2AuthenticationEntryPoint;
import com.racquettrack.security.oauth.OAuth2AuthenticationFilter;
import com.racquettrack.security.oauth.OAuth2AuthenticationProvider;
import com.racquettrack.security.oauth.OAuth2ServiceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.ExceptionTranslationFilter;

/** Represents security configuration of web application based on Spring Framework. */
@Configuration
@EnableWebSecurity
@PropertySource("classpath:oauthEndpoint.properties")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private OAuth2AuthenticationProvider oauthProv;

    @Autowired
    private OAuth2AuthenticationEntryPoint oauthAuthenticationEntryPoint;

    @Autowired
    private OAuth2AuthenticationFilter filter;

    /**
     * Setting up OAuth authentication provider as one of providers for authentication manager of Spring Framework
     * security.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(oauthProv);
    }

    @Bean(name = "manager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * Authentication filter that will be built into filter chain of Spring Framework Security, will catch all
     * unauthorized sessions and will pass them to OAuth authentication provider.
     */
    @Bean
    @DependsOn("propertyPlaceholder")
    @Autowired
    public OAuth2AuthenticationFilter oAuth2AuthenticationFilter(OAuth2ServiceProperties serviceProperties,
                                                                 AuthenticationManager manager,
                                                                 @Value("${oauth.redirectURIShort}")
                                                                         String redirectURIShort)
            throws Exception {
        OAuth2AuthenticationFilter filter = new OAuth2AuthenticationFilter(redirectURIShort);
        filter.setAuthenticationManager(manager);
        filter.setoAuth2ServiceProperties(serviceProperties);
        return filter;
    }

    /** Main configuration of Spring Framework security.*/
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests().
                antMatchers(PropertyLoader.load("service.properties", "path.editor.service") + "/**").permitAll().
                antMatchers(PropertyLoader.load("service.properties",
                        "path.editor.palette.service") + "/**").permitAll().
                antMatchers("/resources/**").permitAll().
                antMatchers("/register").permitAll().
                antMatchers("/oauth/**").permitAll().
                antMatchers("/**").authenticated().
                and().
                httpBasic().
                authenticationEntryPoint(oauthAuthenticationEntryPoint).
                and().
                addFilterAfter(filter, ExceptionTranslationFilter.class).
                formLogin().
                loginProcessingUrl("/").
                loginPage("/").
                and().
                logout().logoutSuccessUrl("/").
                logoutUrl("/j_spring_security_logout").
                and().
                csrf().disable();
    }

}