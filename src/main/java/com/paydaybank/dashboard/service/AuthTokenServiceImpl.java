package com.paydaybank.dashboard.service;

import com.paydaybank.dashboard.enums.TokenStatuses;
import com.paydaybank.dashboard.model.AuthToken;
import com.paydaybank.dashboard.model.User;
import com.paydaybank.dashboard.repository.AuthTokenRepository;
import com.paydaybank.dashboard.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthTokenServiceImpl implements AuthTokenService {

    Logger logegr = LoggerFactory.getLogger(AuthTokenServiceImpl.class);

    @Autowired
    private AuthTokenRepository authTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean create(UUID tokenId, UUID ownerId, Long expiration) {
        try{
            AuthToken token = new AuthToken(tokenId, expiration);

            if( ownerId != null ){
                Optional<User> owner = userRepository.findById(ownerId);
                owner.ifPresent(token::setOwner);
            }

            authTokenRepository.save(token);
        } catch ( Exception ex ){
            logegr.error("An error has been occurred while creating new token. Error : {}", ex.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean setInvalid(UUID tokenId) {
        try{
            Optional<AuthToken> token = authTokenRepository.findByTokenIdAndAndTokenStatus(tokenId, TokenStatuses.ACTIVE);
            if( !token.isPresent() ) {
                throw new EntityNotFoundException();
            }

            AuthToken foundToken = token.get();
            foundToken.setTokenStatus(TokenStatuses.INACTIVE);

            authTokenRepository.save(foundToken);

        } catch ( Exception ex ){
            logegr.error("An error has been occurred while setting {} token as invalid. Error : {}", tokenId, ex.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean isValid(UUID tokenId) {
        try{
            Optional<AuthToken> token = authTokenRepository.findByTokenIdAndAndTokenStatus(tokenId, TokenStatuses.ACTIVE);
            return token.isPresent();
        } catch ( Exception ex ){
            logegr.error("An error has been occurred while checking if {} token is valid. Error : {}", tokenId, ex.getMessage());
            return false;
        }
    }
}
