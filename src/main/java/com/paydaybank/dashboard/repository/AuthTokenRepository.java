package com.paydaybank.dashboard.repository;

import com.paydaybank.dashboard.enums.TokenStatuses;
import com.paydaybank.dashboard.model.AuthToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthTokenRepository extends CrudRepository<AuthToken, UUID> {
    Optional<AuthToken> findByTokenIdAndAndTokenStatus(UUID tokenId, TokenStatuses tokenStatus);
}
