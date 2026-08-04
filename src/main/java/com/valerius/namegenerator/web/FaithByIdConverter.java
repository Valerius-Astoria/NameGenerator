package com.valerius.namegenerator.web;

import com.valerius.namegenerator.model.Faith;
import com.valerius.namegenerator.repository.FaithRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts a form faith id (string) into a {@link Faith} entity for Spring MVC binding.
 */
@Component
public class FaithByIdConverter implements Converter<String, Faith> {

    private final FaithRepository faithRepository;

    public FaithByIdConverter(FaithRepository faithRepository) {
        this.faithRepository = faithRepository;
    }

    @Override
    public Faith convert(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }
        return faithRepository.findById(Long.valueOf(source)).orElse(null);
    }
}
