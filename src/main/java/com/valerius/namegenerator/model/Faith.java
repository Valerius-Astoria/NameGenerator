package com.valerius.namegenerator.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Reference dictionary entry for a character's faith / religion.
 * Supports a two-level hierarchy: primary traditions (e.g. Christianity) and
 * secondary branches (e.g. Roman Catholic). See docs/religions.md.
 */
@Getter
@Setter
@ToString // for clear log info
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Faith {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include // only id is used for equals/hashCode
    private Long id;

    private String code; // Stable business key for seed data and APIs, e.g. "CHRISTIANITY", "ROMAN_CATHOLIC".
    private String name;

    @ManyToOne
    @ToString.Exclude // only print the children
    private Faith parent;
}
