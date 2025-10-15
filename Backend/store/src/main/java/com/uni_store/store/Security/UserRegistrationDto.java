package com.uni_store.store.Security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRegistrationDto {
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String firstName;

    private String lastName;
}
