package com.paydaybank.dashboard.config.security.filters;

import com.paydaybank.dashboard.config.security.components.JwtConfig;
import com.paydaybank.dashboard.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    @Autowired
    UserService userService;

    JwtConfig jwtConfig;

    public JwtAuthorizationFilter(AuthenticationManager authManager, JwtConfig jwtConfig) {
        super(authManager);
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(jwtConfig.getHeader());

        if (header == null || !header.startsWith(jwtConfig.getPrefix())) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(jwtConfig.getHeader());
        if (token == null) {
            return null;
        }

        Claims tokenClaims = Jwts.parser()
                .setSigningKey(jwtConfig.getSecret().getBytes())
                .parseClaimsJws(token.replace(jwtConfig.getPrefix(), ""))
                .getBody();

        String userId = tokenClaims.getSubject();
        if (userId == null) {
            return null;
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        List<String> rolesAsString = (List) tokenClaims.get(jwtConfig.getRolesClaimKey());
        if( rolesAsString != null ) {
            authorities = rolesAsString.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        }

        return new UsernamePasswordAuthenticationToken(userId, null, authorities);
    }
}
