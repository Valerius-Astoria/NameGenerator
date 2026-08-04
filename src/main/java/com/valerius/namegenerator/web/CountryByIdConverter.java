package com.valerius.namegenerator.web;

import com.valerius.namegenerator.model.Country;
import com.valerius.namegenerator.repository.CountryRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts a form country id (string) into a {@link Country} entity for Spring MVC binding.
 */
@Component
public class CountryByIdConverter implements Converter<String, Country> {

    private final CountryRepository countryRepository;

    public CountryByIdConverter(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public Country convert(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }
        return countryRepository.findById(Long.valueOf(source)).orElse(null);
    }
}
