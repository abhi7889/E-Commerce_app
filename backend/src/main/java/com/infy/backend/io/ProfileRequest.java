package com.infy.backend.io;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileRequest {
    @NotBlank(message = "Name should not be empty")
    private String name;
    @Email(message = "Enter a valid email address")
    @NotNull(message = "Email should not be empty")
    private String email;
    @Size(min = 8, message = "Password should be at least 8 characters long")
    private String password;
}