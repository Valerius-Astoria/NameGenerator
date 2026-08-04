package com.valerius.namegenerator.repository;

import com.valerius.namegenerator.model.GeneratedResult;
import com.valerius.namegenerator.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data repository for generated name results.
 */
public interface GeneratedResultRepository extends JpaRepository<GeneratedResult, Long> {

    /** Returns the stored generated result for a given profile, if it exists. */
    Optional<GeneratedResult> findByProfile_Id(Long profileId);

    /** All results belonging to a user's profiles, newest first (history cards). */
    List<GeneratedResult> findAllByProfile_UserOrderByCreatedAtDesc(User user);

    /** Ownership-scoped lookup for the history detail page. */
    Optional<GeneratedResult> findByIdAndProfile_User(Long id, User user);
}
