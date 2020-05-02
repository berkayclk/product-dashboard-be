package com.paydaybank.dashboard.model;

import com.paydaybank.dashboard.enums.UserRoles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "user_roles")
public class UserRole {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRoles role;

    public UserRole(UserRoles role) {
        this.id = UUID.randomUUID();
        this.role = role;
    }
}
