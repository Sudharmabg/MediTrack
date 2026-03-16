package com.airtribe.meditrack.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private SpecializationConverter specializationConverter;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(specializationConverter);
    }
}