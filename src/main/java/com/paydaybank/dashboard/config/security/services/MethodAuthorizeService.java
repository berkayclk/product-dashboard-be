package com.paydaybank.dashboard.config.security.services;

import org.springframework.security.core.Authentication;

import java.util.UUID;

/**
 * This bean will be used to authorize users for sensitive data.
 * It can be invoked like "MethodAuthorizeService.isAuthorizedUser()" from annotations such as preAuthorize etc.
 */
public interface MethodAuthorizeService {

    /**
     * Checks If given userId is equal to userId of the authenticated user
     * @param userId it is provided from the user request. It should be matched with authenticated.
     * @param authentication It is provided from SecurityContextHolder. It can be pass to this function
     *                        (from preAuthorized etc.) like "authentication".
     * @return isAuthorized
     */
    boolean isAuthorizedUser(UUID userId, Authentication authentication );
}
