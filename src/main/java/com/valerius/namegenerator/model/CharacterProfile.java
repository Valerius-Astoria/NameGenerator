package com.valerius.namegenerator.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * A writer's character input for name generation (identity layer).
 * Captures gender, age, country, ancestry, faith, and free-form background.
 * Dictionary fields ({@link Country}, {@link Ancestry}, {@link Faith}) are
 * references to seed data; notes and background hold writer-supplied detail.
 * See docs/dimensions.md.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class CharacterProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Integer age;

    @ManyToOne
    private Country country;

    @ManyToMany
    @ToString.Exclude
    private List<Ancestry> ancestries = new ArrayList<>();
    private String ancestryNote;

    @ManyToMany
    @ToString.Exclude
    private List<Faith> faiths = new ArrayList<>();
    private String faithNote;

    private String background;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
}
