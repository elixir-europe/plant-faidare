package fr.inra.urgi.faidare.config;


import java.util.Arrays;
import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

/**
 * Configuration class for i18n. It uses the locale from the accept-language header, but defaults to English,
 * and only supports a fixed set of locales.
 * It implements WebMvcConfigurer in order to be automatically included in MVC tests.
 * @author JB Nizet
 */
@Configuration
public class I18nConfig implements WebMvcConfigurer {
    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        resolver.setSupportedLocales(Arrays.asList(Locale.FRENCH, Locale.ENGLISH));
        return resolver;
    }
}
