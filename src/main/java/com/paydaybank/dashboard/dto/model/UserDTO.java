package com.paydaybank.dashboard.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.paydaybank.dashboard.enums.UserRoles;
import com.paydaybank.dashboard.model.UserRole;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private UUID id;

    @NotBlank(message = "fullName field is required!")
    @Length(max = 50, min = 2, message = "Length of the fullName field should be between 2 and 50")
    private String fullName;

    @Email(message = "email field is not valid!")
    @NotBlank(message = "email field is required!")
    @Length(max = 75, min = 5, message = "Length of the email field should be between 5 and 75")
    private String email;

    @JsonProperty(access = Access.WRITE_ONLY)
    @ApiModelProperty(accessMode = ApiModelProperty.AccessMode.AUTO)
    @NotBlank(message = "password field is required!")
    @Length(max = 10, min = 6, message = "Length of the password field should be between 6 and 10")
    private String password;

    private String title;

    @JsonProperty(access = Access.READ_ONLY)
    @ApiModelProperty(accessMode = ApiModelProperty.AccessMode.READ_ONLY)
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
