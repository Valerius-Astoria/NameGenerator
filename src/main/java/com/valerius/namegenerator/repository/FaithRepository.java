package com.valerius.namegenerator.repository;

import com.valerius.namegenerator.model.Ancestry;
import com.valerius.namegenerator.model.Faith;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FaithRepository extends JpaRepository<Faith, Long> {

    Optional<Ancestry> findByCode(String code);

    List<Ancestry> findByParentIsNull();

    List<Ancestry> findByParent_Code(String code);
}
