package com.uni_store.store.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateDto {
    @Email(message = "Email should be valid")
    private String email;

    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @Size(min = 2, message = "First name must be at least 2 characters long")
    private String firstName;

    @Size(min = 2, message = "Last name must be at least 2 characters long")
    private String lastName;
}
