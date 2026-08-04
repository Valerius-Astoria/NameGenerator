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
 * A writer's character input for name generation.
 *
 * <p>Holds structured dictionary choices {@link Gender}, {@link Country},
 * {@link Ancestry}, {@link Faith}) plus free-text notes and background.
 * Built on the design form, kept in the HTTP session, passed to Gemini for name generation, then persisted on submit.</p>
 *
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

    /** Owner; set by the generate controller from the logged-in account before saving. */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

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

    @OneToOne(mappedBy = "profile", fetch = FetchType.LAZY)
    @ToString.Exclude
    private GeneratedResult generatedResult;

    private String background;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
}
