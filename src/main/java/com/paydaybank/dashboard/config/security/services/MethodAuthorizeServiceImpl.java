package com.paydaybank.dashboard.config.security.services;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("methodAuthorizeService")
public class MethodAuthorizeServiceImpl implements MethodAuthorizeService {

    @Override
    public boolean isAuthorizedUser(UUID userId, Authentication authentication ) {
        return userId != null && authentication != null && authentication.getPrincipal().equals(userId.toString());
    }
}
