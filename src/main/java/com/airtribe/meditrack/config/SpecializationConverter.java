package com.airtribe.meditrack.config;

import com.airtribe.meditrack.enums.Specialization;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SpecializationConverter implements Converter<String, Specialization> {
    
    @Override
    public Specialization convert(String source) {
        return Specialization.fromString(source);
    }
}