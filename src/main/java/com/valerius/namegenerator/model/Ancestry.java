package com.valerius.namegenerator.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Reference dictionary entry for a character's ancestry.
 *
 * <p>Supports a two-level hierarchy: primary regions (e.g. East Asian) and
 * secondary groups (e.g. Han Chinese). Characters may hold at multiple ancestries
 * via {@link CharacterProfile#getFaiths()}</p>
 *
 */
@Getter
@Setter
@ToString // Lombok: generates toString() for logging and debugging
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Lombok: equals() and hashCode() use only fields marked with @EqualsAndHashCode.Include
@Entity
public class Ancestry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // JPA: database auto-increments the primary key
    @EqualsAndHashCode.Include // include his field in equals() and hashCode()
    private Long id; // primary key (surrogate; may change across DB resets)

    @Column(nullable = false, unique = true)
    private String code; // stable business key for seeds and lookups, e.g. "EAST_ASIAN", "HAN_CHINESE"
    private String name; // human-readable label shown in the UI, e.g. "Han Chinese"

    @ManyToOne
    @ToString.Exclude // Lombok: omit parent from toString() to avoid recursive parent chains
    private Ancestry parent; // null for primary entries; secondary entries point to their primary
}
