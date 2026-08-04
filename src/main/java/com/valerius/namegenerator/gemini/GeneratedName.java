package com.valerius.namegenerator.gemini;

/**
 * Result of a Gemini name-generation call: the name in its native script, its
 * romanized/English form, and a short annotation explaining why it fits the profile.
 */
public record GeneratedName(String nativeName, String name, String annotation) {
}
