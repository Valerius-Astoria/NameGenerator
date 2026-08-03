package com.valerius.namegenerator.repository;

import com.valerius.namegenerator.model.CharacterProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface CharacterProfileRepository extends JpaRepository<CharacterProfile, Long> {

    List<CharacterProfile> findByCreatedAtAfter(Instant after);
}
