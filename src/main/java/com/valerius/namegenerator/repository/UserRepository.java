package com.valerius.namegenerator.repository;

import com.valerius.namegenerator.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data repository for registered writer accounts.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /** Looks up an account by its (lowercased) email address. */
    Optional<User> findByEmail(String email);
}
