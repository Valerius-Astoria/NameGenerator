package com.valerius.namegenerator.repository;

import com.valerius.namegenerator.model.Faith;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data repository for the {@link Faith} dictionary (two-level hierarchy).
 * Inherits CRUD from {@link JpaRepository}; Spring provides the implementation at runtime.
 * Seed data aligns with docs/religions.md.
 */
public interface FaithRepository extends JpaRepository<Faith, Long> {

    /** Looks up a faith entry by its stable business code (e.g. "CHRISTIANITY", "ROMAN_CATHOLIC"). */
    Optional<Faith> findByCode(String code);

    /**
     * Returns top-level faith traditions (parent is null),
     * e.g. Christianity, Islam, Buddhism.
     */
    List<Faith> findByParentIsNull();

    /**
     * Returns secondary faith branches whose parent's code matches,
     * e.g. findByParent_Code("CHRISTIANITY") → Roman Catholic, Eastern Orthodox, …
     */
    List<Faith> findByParent_Code(String code);
}
