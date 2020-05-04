package com.paydaybank.dashboard.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class UserCredentials {

    @Email(message = "email field is not valid!")
    @NotBlank(message = "email field is required!")
    @Length(max = 75, min = 5, message = "Length of the email field should be between 5 and 75")
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "password field is required!")
    @Length(max = 10, min = 6, message = "Length of the password field should be between 6 and 10")
    private String password;
}
