package com.qreal.robots.common.config;

import com.racquettrack.security.oauth.OAuth2AuthenticationEntryPoint;
import com.racquettrack.security.oauth.OAuth2AuthenticationFilter;
import com.racquettrack.security.oauth.OAuth2AuthenticationProvider;
import com.racquettrack.security.oauth.OAuth2ServiceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.ExceptionTranslationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private OAuth2AuthenticationProvider oauthProv;

    @Autowired
    private OAuth2AuthenticationEntryPoint oauthAuthenticationEntryPoint;

    @Autowired
    private OAuth2AuthenticationFilter filter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(oauthProv);
    }

    @Bean(name = "manager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    @Autowired
    public OAuth2AuthenticationFilter oAuth2AuthenticationFilter(OAuth2ServiceProperties serviceProperties,
                                                                 AuthenticationManager manager) throws Exception {
        OAuth2AuthenticationFilter filter = new OAuth2AuthenticationFilter("/oauth/callback");
        filter.setAuthenticationManager(manager);
        filter.setoAuth2ServiceProperties(serviceProperties);
        return filter;
    }

    //.csrf() is optional, enabled by default, if using WebSecurityConfigurerAdapter constructor
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeRequests().
                antMatchers("/RobotRest/**").permitAll().
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