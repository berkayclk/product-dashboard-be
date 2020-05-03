package com.paydaybank.dashboard.dto.model;

import com.paydaybank.dashboard.enums.UserRoles;
import com.paydaybank.dashboard.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private UUID id;
    private String fullName;
    private String email;
    private String password;
    private String title;
    private List<UserRole> roles = new ArrayList<>();

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
