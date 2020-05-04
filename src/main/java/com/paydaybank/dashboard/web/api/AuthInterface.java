package com.paydaybank.dashboard.web.api;

import com.paydaybank.dashboard.dto.request.UserCredentials;
import com.paydaybank.dashboard.dto.response.Response;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping(value = "/logout", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(
            value = "Logout",
            notes = "This endpoint logs out users. You must provide an auth token."
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully logged out! Your token is invalid after that."),
            @ApiResponse(code = 401, message = "Unauthorized", response = Response.class )
    })
    default void logout() {
        throw new IllegalStateException("Add Spring Security to handle authentication");
    }
}
