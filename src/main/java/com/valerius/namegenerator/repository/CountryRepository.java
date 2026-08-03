package com.valerius.namegenerator.repository;

import com.valerius.namegenerator.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Long> {

    Optional<Country> findByCode(String code);

    Optional<Country> findByNameIgnoreCase(String name);

    boolean existsByCode(String code);
}
