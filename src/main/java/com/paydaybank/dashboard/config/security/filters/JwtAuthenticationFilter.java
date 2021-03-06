package com.paydaybank.dashboard.config.security.filters;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.Collections;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.paydaybank.dashboard.config.security.components.JwtConfig;
import com.paydaybank.dashboard.dto.request.UserCredentials;
import com.paydaybank.dashboard.dto.response.Response;
import com.paydaybank.dashboard.helper.ResponseHelper;
import com.paydaybank.dashboard.service.AuthTokenService;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter   {

    private AuthTokenService authTokenService;
    private JwtConfig jwtConfig;
    private AuthenticationManager authManager;
    private Gson gson;

    public JwtAuthenticationFilter(AuthenticationManager authManager, JwtConfig jwtConfig, AuthTokenService authTokenService, Gson gson) {
        this.authManager = authManager;
        this.jwtConfig = jwtConfig;
        this.authTokenService = authTokenService;
        this.gson = gson;

        //set login path with prefix that is value of jwtConfig.getUri()
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(jwtConfig.getUri(), "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        try {

            UserCredentials creds = new ObjectMapper().readValue(request.getInputStream(), UserCredentials.class);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    creds.getEmail(), creds.getPassword(), Collections.emptyList());

            return authManager.authenticate(authToken);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        UUID tokenId = UUID.randomUUID();
        long now = System.currentTimeMillis();
        String token = Jwts.builder()
                .setId(tokenId.toString())
                .setSubject(auth.getName())
                .claim(jwtConfig.getRolesClaimKey(), auth.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .claim("id", tokenId.toString())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + jwtConfig.getExpiration() * 1000))  // in milliseconds
                .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret().getBytes())
                .compact();

        if( authTokenService != null ) {
            authTokenService.create(tokenId, UUID.fromString(auth.getName()) , (long) jwtConfig.getExpiration());
        }

        response.addHeader(jwtConfig.getHeader(), jwtConfig.getPrefix() + token);

        Response responseObj = ResponseHelper.ok(jwtConfig.getPrefix() + token);
        String responseJson = gson.toJson(responseObj);

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(responseJson);
        out.flush();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        Response responseObj = ResponseHelper.unauthorized();
        String responseJson = gson.toJson(responseObj);

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(401);
        out.print(responseJson);
        out.flush();
    }
}