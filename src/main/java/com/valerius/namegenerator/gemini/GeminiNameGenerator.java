package com.valerius.namegenerator.gemini;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.GoogleSearch;
import com.google.genai.types.Part;
import com.google.genai.types.ThinkingConfig;
import com.google.genai.types.Tool;
import com.valerius.namegenerator.model.Ancestry;
import com.valerius.namegenerator.model.CharacterProfile;
import com.valerius.namegenerator.model.Faith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Calls the Gemini API to invent a character name and annotation from a {@link CharacterProfile}.
 *
 * <p>Every call is guided by the "name generation skill" — a methodology prompt bundled at
 * {@code prompts/name-generation-skill.md} and sent as the system instruction. Google Search
 * grounding is enabled so the model can verify naming conventions and historical background,
 * while a small thinking budget keeps latency low. Because the search tool cannot be combined
 * with JSON response mode, the skill asks for JSON-only output and {@link #parseResponse}
 * tolerates Markdown code fences.</p>
 */
@Component
public class GeminiNameGenerator {

    /** Classpath location of the name-generation skill (system instruction). */
    private static final String SKILL_PATH = "prompts/name-generation-skill.md";

    /** Cap on internal reasoning tokens; the skill favors quick, decisive generation. */
    private static final int THINKING_BUDGET_TOKENS = 512;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String apiKey;
    private final String model;
    private final String skillInstruction;

    public GeminiNameGenerator(
            @Value("${gemini.api-key}") String apiKey,
            @Value("${gemini.model}") String model) {
        this.apiKey = apiKey;
        this.model = model;
        this.skillInstruction = loadSkill();
    }

    /**
     * Generates a character name (native script + English form) and a brief annotation.
     *
     * @param profile the writer's identity-layer inputs from the design step
     * @return suggested {@code nativeName}, {@code name}, and explanatory {@code annotation}
     */
    public GeneratedName generate(CharacterProfile profile) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "Gemini API key is missing. Set the GOOGLE_API_KEY environment variable "
                            + "or gemini.api-key in application properties.");
        }

        try (Client client = Client.builder().apiKey(apiKey).build()) {
            GenerateContentConfig config = GenerateContentConfig.builder()
                    .systemInstruction(Content.fromParts(Part.fromText(skillInstruction)))
                    .tools(List.of(Tool.builder()
                            .googleSearch(GoogleSearch.builder().build())
                            .build()))
                    .thinkingConfig(ThinkingConfig.builder()
                            .thinkingBudget(THINKING_BUDGET_TOKENS)
                            .build())
                    .candidateCount(1)
                    .build();

            GenerateContentResponse response =
                    client.models.generateContent(model, buildPrompt(profile), config);

            return parseResponse(response.text());
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to generate a name with Gemini: " + e.getMessage(), e);
        }
    }

    private String buildPrompt(CharacterProfile profile) {
        String country = profile.getCountry() != null ? profile.getCountry().getName() : "unspecified";
        String ancestries = profile.getAncestries() == null || profile.getAncestries().isEmpty()
                ? "unspecified"
                : profile.getAncestries().stream()
                        .map(Ancestry::getName)
                        .collect(Collectors.joining(", "));
        String faiths = profile.getFaiths() == null || profile.getFaiths().isEmpty()
                ? "unspecified"
                : profile.getFaiths().stream()
                        .map(Faith::getName)
                        .collect(Collectors.joining(", "));

        return """
                Generate one name for this character profile, following your skill instructions.

                Character profile:
                - Gender: %s
                - Age: %s
                - Country: %s
                - Ancestry: %s
                - Ancestry note: %s
                - Faith: %s
                - Faith note: %s
                - Background: %s
                """.formatted(
                profile.getGender() != null ? profile.getGender() : "unspecified",
                profile.getAge() != null ? profile.getAge() : "unspecified",
                country,
                ancestries,
                blankToUnspecified(profile.getAncestryNote()),
                faiths,
                blankToUnspecified(profile.getFaithNote()),
                blankToUnspecified(profile.getBackground()));
    }

    private GeneratedName parseResponse(String raw) throws Exception {
        String json = stripCodeFences(raw);
        if (json == null || json.isBlank()) {
            throw new IllegalStateException("Gemini returned an empty response.");
        }
        JsonNode node = objectMapper.readTree(json);
        String nativeName = textOrNull(node, "nativeName");
        String name = textOrNull(node, "name");
        String annotation = textOrNull(node, "annotation");
        if (name == null || name.isBlank() || annotation == null || annotation.isBlank()) {
            throw new IllegalStateException("Gemini response missing name or annotation: " + json);
        }
        // Latin-script cultures may omit a distinct native form; fall back to the English name.
        if (nativeName == null || nativeName.isBlank()) {
            nativeName = name;
        }
        return new GeneratedName(nativeName.trim(), name.trim(), annotation.trim());
    }

    /**
     * Removes a surrounding Markdown code fence (e.g. ```json ... ```) if present.
     * Needed because JSON response mode cannot be used together with the search tool.
     */
    private static String stripCodeFences(String text) {
        if (text == null) {
            return null;
        }
        String trimmed = text.trim();
        if (trimmed.startsWith("```")) {
            int firstNewline = trimmed.indexOf('\n');
            int lastFence = trimmed.lastIndexOf("```");
            if (firstNewline >= 0 && lastFence > firstNewline) {
                return trimmed.substring(firstNewline + 1, lastFence).trim();
            }
        }
        return trimmed;
    }

    private static String textOrNull(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value == null || value.isNull() ? null : value.asText();
    }

    private static String blankToUnspecified(String value) {
        return value == null || value.isBlank() ? "unspecified" : value;
    }

    private static String loadSkill() {
        try {
            return new ClassPathResource(SKILL_PATH)
                    .getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load name-generation skill: " + SKILL_PATH, e);
        }
    }
}
