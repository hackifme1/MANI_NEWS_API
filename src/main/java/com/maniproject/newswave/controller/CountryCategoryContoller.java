package com.maniproject.newswave.controller;


import lombok.extern.slf4j.Slf4j;
import com.maniproject.newswave.utils.category.CategoryServiceProvider1;
import com.maniproject.newswave.utils.country.CountryServiceProvider1;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class CountryCategoryContoller {

    private final CountryServiceProvider1 countryServiceProvider1;
    private final CategoryServiceProvider1 categoryServiceProvider1;

    public CountryCategoryContoller(CountryServiceProvider1 countryServiceProvider1, CategoryServiceProvider1 categoryServiceProvider1) {
        this.countryServiceProvider1 = countryServiceProvider1;
        this.categoryServiceProvider1 = categoryServiceProvider1;
    }
    @GetMapping("/countries-and-categories")
    public Map<String, List<String>> getCountryCategory() {
        HashMap<String, List<String>> responseObject = new HashMap<>();
        responseObject.put("countries", countryServiceProvider1.getCountries());
        responseObject.put("categories", categoryServiceProvider1.getCategories());
        return responseObject;
    }


}
