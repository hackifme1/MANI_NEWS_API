package com.maniproject.newswave.utils.country;

import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


class CountryServiceProvider1Test {
 private final CountryServiceProvider1 countryServiceProvider1 = new CountryServiceProvider1();
    @Test
    void getCountries() {
        assertEquals(0, countryServiceProvider1.getCountries().stream().filter(country -> country.length() != 2).collect(Collectors.toList()).size());
        assertTrue(!countryServiceProvider1.getCountries().isEmpty());
    }

    @Test
    void getCountriesFromCsv() {
       assertEquals(0, countryServiceProvider1.getCountries().stream().filter(country -> country.length() != 2).collect(Collectors.toList()).size());
       assertTrue(!countryServiceProvider1.getCountries().isEmpty());
    }

    @Test
    void getCountriesFromCsvRuntime() {
       assertEquals(0, countryServiceProvider1.getCountries().stream().filter(country -> country.length() != 2).collect(Collectors.toList()).size());
       assertTrue(!countryServiceProvider1.getCountries().isEmpty());
       Pattern digit = Pattern.compile("[0-9]");
       Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
       assertEquals(0, countryServiceProvider1.getCountries().stream().filter(country -> digit.matcher(country).find() || special.matcher(country).find()).collect(Collectors.toList()).size());

    }
}