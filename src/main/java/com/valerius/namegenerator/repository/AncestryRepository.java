package com.valerius.namegenerator.repository;

import com.valerius.namegenerator.model.Ancestry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AncestryRepository extends JpaRepository<Ancestry, Long> {
    Optional<Ancestry> findByCode(String code);

    List<Ancestry> findByParentIsNull();

    List<Ancestry> findByParent_Code(String code);
}
