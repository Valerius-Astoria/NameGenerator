package com.valerius.namegenerator.controller;

import com.valerius.namegenerator.model.User;
import com.valerius.namegenerator.repository.UserRepository;
import com.valerius.namegenerator.web.RegistrationForm;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Locale;

/**
 * MVC controller for account pages: the login view (authentication itself is handled
 * by Spring Security form login) and email/password registration.
 */
@Controller
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /** Renders the login page; Spring Security handles the POST. */
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    /** Renders the registration form. */
    @GetMapping("/register")
    public String registerForm(@ModelAttribute("registrationForm") RegistrationForm form) {
        return "register";
    }

    /**
     * Handles {@code POST /register}: validates the form, rejects mismatched passwords and
     * duplicate emails, then stores the account (email lowercased, password BCrypt-hashed)
     * and redirects to the login page.
     */
    @PostMapping("/register")
    public String processRegistration(
            @Valid @ModelAttribute("registrationForm") RegistrationForm form,
            Errors errors) {
        if (!errors.hasFieldErrors("password") && !errors.hasFieldErrors("confirmPassword")
                && !form.getPassword().equals(form.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "mismatch", "Passwords do not match.");
        }

        String email = form.getEmail() == null
                ? ""
                : form.getEmail().trim().toLowerCase(Locale.ROOT);
        if (!errors.hasFieldErrors("email") && userRepository.findByEmail(email).isPresent()) {
            errors.rejectValue("email", "duplicate", "An account with this email already exists.");
        }

        if (errors.hasErrors()) {
            return "register";
        }

        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(form.getPassword()));
        userRepository.save(user);
        log.info("Registered account: {}", email);
        return "redirect:/login?registered";
    }
}
