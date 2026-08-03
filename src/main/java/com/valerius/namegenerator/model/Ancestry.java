package com.valerius.namegenerator.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Reference dictionary entry for a character's ancestry / heritage.
 * Supports a two-level hierarchy: primary regions (e.g. East Asian) and
 * secondary groups (e.g. Han Chinese). Used alongside {@link Country}:
 * country indicates nationality or residence; ancestry indicates naming
 * tradition and heritage—especially for diaspora and mixed-heritage characters.
 * Seed data aligns with docs/races.md.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Ancestry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String code;
    private String name;

    @ManyToOne
    @ToString.Exclude
    private Ancestry parent;
}
