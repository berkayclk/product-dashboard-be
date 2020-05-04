package com.paydaybank.dashboard.web.api;

import com.paydaybank.dashboard.config.security.model.UserCredentials;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Authentication API specification for Swagger documentation and Code Generation.
 * Implemented by Spring Security.
 */
public interface AuthInterface {

    /**
     * Implemented by Spring Security
     */
    @ApiOperation(value = "Login", notes = "Login with the given credentials.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully logged in! Get the token from authorization header.", responseHeaders = {@ResponseHeader( name = "Authorization")}),
            @ApiResponse(code = 401, message = "Attempted to log-in with wrong credentials.")
    })
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    default void login( @RequestBody UserCredentials credentials ) {
        throw new IllegalStateException("Add Spring Security to handle authentication");
    }
}
