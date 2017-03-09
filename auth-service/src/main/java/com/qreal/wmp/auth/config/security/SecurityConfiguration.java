package com.qreal.wmp.auth.config.security;

import com.qreal.wmp.auth.database.users.UserDAOSec;
import com.racquettrack.security.oauth.OAuth2AuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.annotation.Resource;

/** Represents security configuration of web application based on Spring Framework.*/
@Configuration
@EnableWebSecurity
@Order(2) //EnableResourceServer annotation create WebSecurityConfigurerAdapter with Order(3)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Resource(name = "userServiceSec")
    private UserDAOSec userServiceSec;

    @Resource(name = "passwordEncoder")
    private PasswordEncoder encoder;

    @Resource(name = "providerGoogle")
    private OAuth2AuthenticationProvider oAuthProvGoogle;

    @Resource(name = "providerGithub")
    private OAuth2AuthenticationProvider oAuthProvGithub;

    /**
     * Setting up OAuth authentication providers and local db user provider as providers for authentication
     * manager of Spring Framework security.
     */
    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(oAuthProvGoogle);
        auth.authenticationProvider(oAuthProvGithub);
        auth.userDetailsService(userServiceSec).passwordEncoder(encoder);
    }

    /** Setting security to ignore some of urls.*/
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/webjars/**", "/images/**", "/resources/**", "/oauth/uncache_approvals",
                "/oauth/cache_approvals");
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /** Main configuration of Spring Framework security.*/
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                requestMatchers().
                antMatchers("/*", "/usersPanel/**", "/clientsPanel/**", "/register/**", "/resources/**",
                "/oauth/authorize").
                and().

                exceptionHandling().
                accessDeniedPage("/logErr").
                and().

                authorizeRequests().
                antMatchers("/").
                authenticated().
                and().

                authorizeRequests().
                antMatchers("/oauth/authorize", "/home").
                hasRole("USER").
                and().

                authorizeRequests().
                antMatchers("/usersPanel/**", "/clientsPanel/**").
                hasRole("ADMIN").
                and().

                authorizeRequests().
                antMatchers("/log*", "/register**").
                permitAll().
                and().

                csrf().
                requireCsrfProtectionMatcher(new AntPathRequestMatcher("/oauth/authorize")).
                disable().
                logout().
                logoutUrl("/logout").
                logoutSuccessUrl("/").
                and().

                formLogin().
                loginPage("/log").
                failureUrl("/log?error").
                usernameParameter("username").
                passwordParameter("password").
                defaultSuccessUrl("/").
                loginProcessingUrl("/login");
    }

    /** Store for access tokens for clients.*/
    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

    //Needed for possible use of hasRole in jsp
    @Bean(name = "webSecurityExpressionHandler")
    public SecurityExpressionHandler<FilterInvocation> createWebExpressionHandlerOAuth() {
        return new OAuth2WebSecurityExpressionHandler();
    }

}