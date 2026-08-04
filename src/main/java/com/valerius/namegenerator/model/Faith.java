package com.valerius.namegenerator.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Reference dictionary entry for a character's faith
 *
 * <p>Supports a two-level hierarchy: primary traditions (e.g. Christianity) and
 * secondary branches (e.g. Roman Catholic). Characters may hold multiple faiths
 * via {@link CharacterProfile#getFaiths()}</p>
 *
 */
@Getter
@Setter
@ToString // for clear log info
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Faith {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    private String name;

    @ManyToOne
    @ToString.Exclude
    private Faith parent;
}
