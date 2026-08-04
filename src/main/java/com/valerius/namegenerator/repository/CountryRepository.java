package com.valerius.namegenerator.repository;

import com.valerius.namegenerator.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data repository for the {@link Country} dictionary.
 * Inherits CRUD from {@link JpaRepository}; Spring provides the implementation at runtime.
 * Seed data aligns with docs/countries.md.
 */
public interface CountryRepository extends JpaRepository<Country, Long> {

    /** Looks up a country by its stable business code (e.g. ISO alpha-2). */
    Optional<Country> findByCode(String code);

    /** Looks up a country by English name, case-insensitive. */
    Optional<Country> findByNameIgnoreCase(String name);

    /** Returns whether a country with the given code already exists (useful when seeding). */
    boolean existsByCode(String code);
}
