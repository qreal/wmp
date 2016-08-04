package com.resources.auth.config.security;

import com.racquettrack.security.oauth.OAuth2AuthenticationProvider;
import com.resources.auth.database.users.UserDAOSec;
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

    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(oAuthProvGoogle);
        auth.authenticationProvider(oAuthProvGithub);
        auth.userDetailsService(userServiceSec).passwordEncoder(encoder);
    }

    //Security should ignore some urls (for static files permited to all)
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
                antMatchers("/log", "/logErr", "/register**").
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
                failureUrl("/logErr").
                usernameParameter("username").
                passwordParameter("password").
                defaultSuccessUrl("/").
                loginProcessingUrl("/login");
    }

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