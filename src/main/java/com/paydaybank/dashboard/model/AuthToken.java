package com.paydaybank.dashboard.model;

import com.paydaybank.dashboard.enums.TokenStatuses;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "auth_tokens")
public class AuthToken {
    @Id
    UUID tokenId;

    @Column(nullable = false)
    TokenStatuses tokenStatus = TokenStatuses.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    User owner;

    LocalDateTime createdAt;
    LocalDateTime invalidatedAt;
    LocalDateTime expiredAt;

    public AuthToken(UUID tokenId){
        this.tokenId = tokenId;
        this.createdAt = LocalDateTime.now();
    }

    public AuthToken(UUID tokenId, Long expireTime){
        this.tokenId = tokenId;
        this.expiredAt = LocalDateTime.now().plus(expireTime, ChronoUnit.SECONDS);
        this.createdAt = LocalDateTime.now();
    }

    public void setTokenStatus(TokenStatuses status ){
        if( status == TokenStatuses.INACTIVE) {
            invalidatedAt = LocalDateTime.now();
        }
        this.tokenStatus = status;
    }
}
