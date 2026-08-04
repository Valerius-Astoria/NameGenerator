package com.valerius.namegenerator.security;

import com.valerius.namegenerator.model.User;
import com.valerius.namegenerator.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * Resolves the logged-in {@link User} entity from the Spring Security principal
 * (the principal name is the account email).
 */
@Service
public class CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /** Returns the account for the given authentication, failing fast if it vanished. */
    public User require(Authentication authentication) {
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalStateException(
                        "Logged-in account not found: " + authentication.getName()));
    }
}
