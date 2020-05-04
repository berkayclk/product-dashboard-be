package com.paydaybank.dashboard.config.security;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.paydaybank.dashboard.config.security.components.JwtConfig;
import com.paydaybank.dashboard.config.security.filters.JwtAuthorizationFilter;
import com.paydaybank.dashboard.config.security.filters.JwtAuthenticationFilter;
import com.paydaybank.dashboard.config.security.handler.JwtLogoutHandler;
import com.paydaybank.dashboard.config.security.handler.LogoutSuccessHandler;
import com.paydaybank.dashboard.service.AuthTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@Component
@EnableWebSecurity
@EnableWebMvc
@EnableAutoConfiguration
@EnableGlobalMethodSecurity( prePostEnabled = true)
@ConditionalOnBean({ JwtConfig.class, AuthTokenService.class })
public class CustomWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    @Value("${security.cors.domains:http://localhost:3000}")
    String corsWhiteList;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthTokenService authTokenService;

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Gson gson;

    private static final String[] AUTH_WHITELIST = {
            //h2
            "/h2-console/**",
            // -- swagger ui
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/webjars/**"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors()
        .and()
            .csrf().ignoringAntMatchers("/h2-console/**").disable()
            .headers().frameOptions().disable()
        .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .addFilterBefore(new JwtAuthorizationFilter(authenticationManager(), jwtConfig, authTokenService), LogoutFilter.class)
            .addFilterAfter(new JwtAuthenticationFilter(authenticationManager(), jwtConfig, authTokenService, gson), LogoutFilter.class)
            .exceptionHandling().authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
        .and()
            .authorizeRequests()
            .antMatchers(AUTH_WHITELIST).permitAll() // tools
            .antMatchers(HttpMethod.POST, jwtConfig.getUri(), "/auth/signup").permitAll() //auth
            .antMatchers(HttpMethod.GET, "/auth/logout").authenticated()
            .anyRequest().fullyAuthenticated();

        http
            .logout()
            .clearAuthentication(false)
            .addLogoutHandler(new JwtLogoutHandler(authTokenService))
            .logoutUrl("/auth/logout")
            .logoutSuccessHandler(new LogoutSuccessHandler());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource()
    {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(corsWhiteList.split(",")));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*","Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    Gson gson(){
        return new Gson();
    }
}