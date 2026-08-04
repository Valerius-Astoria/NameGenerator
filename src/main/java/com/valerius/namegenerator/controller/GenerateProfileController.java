package com.valerius.namegenerator.controller;

import com.valerius.namegenerator.gemini.GeneratedName;
import com.valerius.namegenerator.gemini.GeminiNameGenerator;
import com.valerius.namegenerator.model.Ancestry;
import com.valerius.namegenerator.model.CharacterProfile;
import com.valerius.namegenerator.model.Faith;
import com.valerius.namegenerator.model.GeneratedResult;
import com.valerius.namegenerator.repository.CharacterProfileRepository;
import com.valerius.namegenerator.repository.GeneratedResultRepository;
import com.valerius.namegenerator.security.CurrentUserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import java.util.Optional;

/**
 * MVC controller for the "generate" step (name + explanation).
 *
 * <p>Flow:
 * <ul>
 *   <li>{@code GET /generate/current}: calls Gemini using the session {@link CharacterProfile}
 *       and renders {@code generate.html} with model attributes {@code name} and {@code annotation}.</li>
 *   <li>{@code POST /generate}: persists the submitted {@link CharacterProfile} and (if present)
 *       persists the generated {@code name} and {@code annotation} into {@link GeneratedResult}.
 *       Then the session is completed and the user is redirected to {@code /design}.</li>
 * </ul>
 *
 * <p>The generated values are submitted via hidden form fields (see {@code generate.html}):
 * {@code generatedName}, {@code generatedNativeName}, and {@code generatedExplanation}.</p>
 */
@Controller
@RequestMapping("/generate")
@SessionAttributes("characterProfile")
public class GenerateProfileController {

    private static final Logger log = LoggerFactory.getLogger(GenerateProfileController.class);

    private final CharacterProfileRepository characterProfileRepository;
    private final GeneratedResultRepository generatedResultRepository;
    private final GeminiNameGenerator geminiNameGenerator;
    private final CurrentUserService currentUserService;

    public GenerateProfileController(
            CharacterProfileRepository characterProfileRepository,
            GeneratedResultRepository generatedResultRepository,
            GeminiNameGenerator geminiNameGenerator,
            CurrentUserService currentUserService) {
        this.characterProfileRepository = characterProfileRepository;
        this.generatedResultRepository = generatedResultRepository;
        this.geminiNameGenerator = geminiNameGenerator;
        this.currentUserService = currentUserService;
    }

    @ModelAttribute("characterProfile")
    public CharacterProfile characterProfile() {
        return new CharacterProfile();
    }

    /**
     * Handles {@code GET /generate/current}: calls Gemini with the session profile,
     * adds {@code name} and {@code annotation} to the model, then shows the generate page.
     */
    @GetMapping("/current")
    public String generateForm(
            @ModelAttribute("characterProfile") CharacterProfile characterProfile,
            Model model) {
        try {
            GeneratedName generated = geminiNameGenerator.generate(characterProfile);
            model.addAttribute("name", generated.name());
            model.addAttribute("nativeName", generated.nativeName());
            model.addAttribute("annotation", generated.annotation());
            log.info("Generated name for profile: {} ({})", generated.nativeName(), generated.name());
        } catch (Exception e) {
            log.warn("Gemini name generation failed: {}", e.getMessage());
            model.addAttribute("name", null);
            model.addAttribute("nativeName", null);
            model.addAttribute("annotation", null);
            model.addAttribute("generationFailed", true);
        }
        return "generate";
    }

    /**
     * Handles {@code POST /generate}.
     *
     * <p>Validates the submitted {@link CharacterProfile} and saves it. The generated values are
     * provided via hidden form fields:
     * {@code generatedName}, {@code generatedNativeName}, and {@code generatedExplanation}.
     * When present/non-blank, the controller
     * upserts a {@link GeneratedResult} for the saved profile (strict one-to-one).
     * Finally, it completes the session ({@code SessionStatus#setComplete()}) and redirects back to
     * {@code /design} for a fresh start.</p>
     *
     * <p>If validation fails, it re-renders the {@code generate} view.</p>
     */
    @PostMapping
    public String processProfile(
            @Valid @ModelAttribute("characterProfile") CharacterProfile characterProfile,
            Errors errors,
            @RequestParam(name = "generatedName", required = false) String generatedName,
            @RequestParam(name = "generatedNativeName", required = false) String generatedNativeName,
            @RequestParam(name = "generatedExplanation", required = false) String generatedExplanation,
            Authentication authentication,
            SessionStatus sessionStatus) {
        if (errors.hasErrors()) {
            return "generate";
        }

        // Persist the input profile snapshot first (owned by the logged-in writer);
        // then persist the generated output (if present).
        characterProfile.setUser(currentUserService.require(authentication));
        CharacterProfile saved = characterProfileRepository.save(characterProfile);
        if (generatedName != null && !generatedName.isBlank()
                && generatedExplanation != null && !generatedExplanation.isBlank()) {
            Optional<GeneratedResult> existing = generatedResultRepository.findByProfile_Id(saved.getId());
            GeneratedResult result = existing.orElseGet(() -> {
                GeneratedResult r = new GeneratedResult();
                r.setProfile(saved);
                return r;
            });

            result.setGeneratedName(generatedName.trim());
            result.setNativeName(generatedNativeName != null && !generatedNativeName.isBlank()
                    ? generatedNativeName.trim()
                    : generatedName.trim());
            result.setExplanation(generatedExplanation.trim());

            GeneratedResult savedResult = generatedResultRepository.save(result);
            log.info("Saved generated result: id={}, profileId={}", savedResult.getId(), saved.getId());
        }
        log.info(
                "Profile submitted: id={}, gender={}, age={}, country={}, ancestries={}, faiths={}",
                saved.getId(),
                saved.getGender(),
                saved.getAge(),
                saved.getCountry() != null ? saved.getCountry().getName() : null,
                saved.getAncestries().stream().map(Ancestry::getCode).toList(),
                saved.getFaiths().stream().map(Faith::getCode).toList());
        sessionStatus.setComplete();
        return "redirect:/design";
    }
}
