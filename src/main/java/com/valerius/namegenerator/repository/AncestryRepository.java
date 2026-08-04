package com.valerius.namegenerator.repository;

import com.valerius.namegenerator.model.Ancestry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data repository for the {@link Ancestry} dictionary (two-level hierarchy).
 * Inherits CRUD from {@link JpaRepository}; Spring provides the implementation at runtime.
 * Seed data aligns with docs/races.md.
 */
public interface AncestryRepository extends JpaRepository<Ancestry, Long> {

    /** Looks up an ancestry entry by its stable business code (e.g. "EAST_ASIAN", "HAN_CHINESE"). */
    Optional<Ancestry> findByCode(String code);

    /**
     * Returns top-level ancestry categories (parent is null),
     * e.g. East Asian, South Asian.
     */
    List<Ancestry> findByParentIsNull();

    /**
     * Returns secondary ancestry entries whose parent's code matches,
     * e.g. findByParent_Code("EAST_ASIAN") → Han Chinese, Japanese, Korean, …
     */
    List<Ancestry> findByParent_Code(String code);
}
