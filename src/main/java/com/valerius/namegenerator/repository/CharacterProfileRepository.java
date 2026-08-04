package com.valerius.namegenerator.repository;

import com.valerius.namegenerator.model.CharacterProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

/**
 * Spring Data repository for writer-submitted {@link CharacterProfile} records.
 * Inherits CRUD from {@link JpaRepository}; Spring provides the implementation at runtime.
 * Profiles capture identity-layer inputs used for name generation.
 */
public interface CharacterProfileRepository extends JpaRepository<CharacterProfile, Long> {

    /**
     * Returns profiles created after the given instant
     * (e.g. recent submissions for admin or debugging).
     */
    List<CharacterProfile> findByCreatedAtAfter(Instant after);
}
