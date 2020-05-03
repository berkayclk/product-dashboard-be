package com.paydaybank.dashboard.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.paydaybank.dashboard.enums.UserRoles;
import com.paydaybank.dashboard.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private UUID id;
    private String fullName;
    private String email;
    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;
    private String title;
    @JsonProperty(access = Access.READ_ONLY)
    private Set<UserRole> roles = new HashSet<>();

    public UserDTO(String fullName, @NotNull String email, @NotNull String password, String title) {
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
