package com.valerius.namegenerator.controller;

import com.valerius.namegenerator.model.Ancestry;
import com.valerius.namegenerator.model.CharacterProfile;
import com.valerius.namegenerator.model.Faith;
import com.valerius.namegenerator.model.Gender;
import com.valerius.namegenerator.repository.AncestryRepository;
import com.valerius.namegenerator.repository.CountryRepository;
import com.valerius.namegenerator.repository.FaithRepository;
import com.valerius.namegenerator.web.OptionGroup;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.Comparator;
import java.util.List;

/**
 * MVC controller for the "design" (code-identity) step.
 *
 * <p>Loads the dictionary options required by {@code design.html}:
 * gender values, countries, and grouped ancestry/faith selections.
 * The HTML renders:
 * - gender as radio buttons (bound to {@link CharacterProfile#getGender()})
 * - country as a select (posts country id; converted via *ByIdConverter)
 * - ancestry + faith as grouped checkboxes (parent==null defines the primary group)</p>
 *
 * <p>After the form is submitted successfully, redirects to {@code /generate/current}
 * to call Gemini.</p>
 *
 */
@Controller
@RequestMapping("/design")
@SessionAttributes("characterProfile")
public class DesignCoreIdentityController {

    private final CountryRepository countryRepository;
    private final AncestryRepository ancestryRepository;
    private final FaithRepository faithRepository;

    public DesignCoreIdentityController(
            CountryRepository countryRepository,
            AncestryRepository ancestryRepository,
            FaithRepository faithRepository) {
        this.countryRepository = countryRepository;
        this.ancestryRepository = ancestryRepository;
        this.faithRepository = faithRepository;
    }

    @ModelAttribute("characterProfile")
    public CharacterProfile characterProfile() {
        return new CharacterProfile();
    }

    /**
     * Loads gender, country, ancestry, and faith options into the model for {@code design.html}.
     *
     * @param model the Spring MVC model
     */
    @ModelAttribute
    public void addCoreIdentityOptionsToModel(Model model) {
        // adds gender options to the model
        model.addAttribute("genders", Gender.values());

        // add countries to the model
        model.addAttribute(
                "countries",
                countryRepository.findAll().stream()
                        .sorted(Comparator.comparing(country -> country.getName(), String.CASE_INSENSITIVE_ORDER))
                        .toList());

        // gets all ancestries
        List<Ancestry> allAncestries = ancestryRepository.findAll();
        // gets all primary ancestries
        List<Ancestry> primaryAncestries = allAncestries.stream()
                .filter(ancestry -> ancestry.getParent() == null)
                .sorted(Comparator.comparing(Ancestry::getName, String.CASE_INSENSITIVE_ORDER))
                .toList();
        // adds ancestries grouped by primary ancestries to the model
        model.addAttribute(
                "ancestryGroups",
                primaryAncestries.stream()
                        .map(primary -> {
                            List<Ancestry> children = filterAncestriesByParent(allAncestries, primary);
                            if (children.isEmpty()) {
                                children = List.of(primary);
                            }
                            return new OptionGroup<>(primary.getCode(), primary.getName(), children);
                        })
                        .toList());

        // gets all faiths
        List<Faith> allFaiths = faithRepository.findAll();
        // gets all primary faiths
        List<Faith> primaryFaiths = allFaiths.stream()
                .filter(faith -> faith.getParent() == null)
                .sorted(Comparator.comparing(Faith::getName, String.CASE_INSENSITIVE_ORDER))
                .toList();
        // adds faiths grouped by primary faiths to the model
        model.addAttribute(
                "faithGroups",
                primaryFaiths.stream()
                        .map(primary -> {
                            List<Faith> children = filterFaithsByParent(allFaiths, primary);
                            if (children.isEmpty()) {
                                children = List.of(primary);
                            }
                            return new OptionGroup<>(primary.getCode(), primary.getName(), children);
                        })
                        .toList());
    }

    /**
     * Handles {@code GET /design}.
     *
     * <p>Renders the Thymeleaf template {@code design.html}, using the model populated by
     * {@link #addCoreIdentityOptionsToModel(Model)} (gender enum values, countries, and grouped
     * ancestry/faith options).</p>
     *
     * @return view name {@code "design"}
     */
    @GetMapping
    public String showDesignForm() {
        return "design";
    }

    /**
     * Handles {@code POST /design}.
     *
     * <p>Validates the submitted {@link CharacterProfile}. If validation fails, re-renders
     * {@code design.html} so the writer can correct inputs. On success, redirects to
     * {@code /generate/current} to invoke Gemini name generation.</p>
     *
     * @param characterProfile submitted identity inputs (stored in the session via
     *                           {@code @SessionAttributes("characterProfile")})
     * @param errors validation errors from Spring MVC
     * @return redirect to {@code /generate/current} on success, otherwise {@code "design"}
     */
    @PostMapping
    public String processDesign(
            @ModelAttribute("characterProfile") CharacterProfile characterProfile,
            Errors errors) {
        if (errors.hasErrors()) {
            return "design";
        }
        return "redirect:/generate/current";
    }

    private List<Ancestry> filterAncestriesByParent(List<Ancestry> ancestries, Ancestry parent) {
        return ancestries.stream()
                .filter(ancestry -> parent.equals(ancestry.getParent()))
                .sorted(Comparator.comparing(Ancestry::getName, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    private List<Faith> filterFaithsByParent(List<Faith> faiths, Faith parent) {
        return faiths.stream()
                .filter(faith -> parent.equals(faith.getParent()))
                .sorted(Comparator.comparing(Faith::getName, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }
}
