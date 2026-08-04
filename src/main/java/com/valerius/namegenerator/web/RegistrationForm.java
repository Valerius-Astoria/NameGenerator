package com.valerius.namegenerator.web;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Form backing bean for account registration (email + password with confirmation).
 * Cross-field checks (password match, duplicate email) are done in the controller.
 */
@Getter
@Setter
public class RegistrationForm {

    @NotBlank(message = "Email is required.")
    @Email(message = "Enter a valid email address.")
    private String email;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, max = 72, message = "Password must be 8–72 characters.")
    private String password;

    @NotBlank(message = "Repeat the password.")
    private String confirmPassword;
}
