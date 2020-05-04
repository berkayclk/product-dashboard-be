package com.paydaybank.dashboard.service;

import java.util.UUID;

public interface AuthTokenService {

    boolean create(UUID tokenId, UUID ownerId, Long expiration);

    boolean setInvalid(UUID tokenId);

    boolean isValid(UUID tokenId);
}
