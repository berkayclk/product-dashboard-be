package com.paydaybank.dashboard.config.security.handler;

import com.paydaybank.dashboard.service.AuthTokenService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

public class JwtLogoutHandler extends SecurityContextLogoutHandler {

    AuthTokenService authTokenService;

    public JwtLogoutHandler(AuthTokenService authTokenService) {
        this.authTokenService = authTokenService;
    }

    @Override
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {

            if( authentication == null ) {
                throw new AccessDeniedException("Attempted to log out without authentication!");
            }

            UUID tokeId = UUID.fromString( authentication.getCredentials().toString() );
            this.authTokenService.setInvalid(tokeId);

            super.logout(httpServletRequest,httpServletResponse,authentication);
    }
}
