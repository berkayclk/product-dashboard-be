package com.paydaybank.dashboard.model;

import com.paydaybank.dashboard.enums.UserRoles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.CascadeType;
import javax.validation.constraints.NotNull;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(updatable = false, nullable = false)
    private UUID id;

    private String fullName;

    @NotNull
    @Column(unique = true, nullable = false)
    private String email;

    @NotNull
    @Column(nullable = false)
    private String password;

    @Column(length = 50)
    private String title;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private Set<UserRole> roles = new HashSet<>();

    public User(String fullName, @NotNull String email, @NotNull String password, String title) {
        this.id = UUID.randomUUID();
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.title = title;
    }

    public void addRole(UserRoles role) {
        this.roles.add( new UserRole(role));
    }
}
