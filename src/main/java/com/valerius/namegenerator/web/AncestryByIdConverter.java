package com.valerius.namegenerator.web;

import com.valerius.namegenerator.model.Ancestry;
import com.valerius.namegenerator.repository.AncestryRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts a form ancestry id (string) into an {@link Ancestry} entity for Spring MVC binding.
 */
@Component
public class AncestryByIdConverter implements Converter<String, Ancestry> {

    private final AncestryRepository ancestryRepository;

    public AncestryByIdConverter(AncestryRepository ancestryRepository) {
        this.ancestryRepository = ancestryRepository;
    }

    @Override
    public Ancestry convert(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }
        return ancestryRepository.findById(Long.valueOf(source)).orElse(null);
    }
}
