package com.valerius.namegenerator.web;

import java.util.List;

/**
 * A primary dictionary category plus its secondary options, for grouped form rendering.
 *
 * @param code     stable parent code (e.g. EAST_ASIAN)
 * @param name     display name of the primary category
 * @param children secondary options under this primary
 * @param <T>      option entity type ({@code Ancestry} or {@code Faith})
 */
public record OptionGroup<T>(String code, String name, List<T> children) {
}
