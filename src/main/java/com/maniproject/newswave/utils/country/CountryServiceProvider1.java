package com.maniproject.newswave.utils.country;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CountryServiceProvider1 implements Country {

    public List<String> getCountries() {
        return List.of("ae", "ar", "at", "au", "be", "bg", "br", "ca", "ch", "cn", "co", "cu", "cz", "de", "eg", "fr", "gb", "gr", "hk", "hu", "id", "ie", "il", "in", "it", "jp", "kr", "lt", "lv", "ma", "mx", "my", "ng", "nl", "no", "nz", "ph", "pl", "pt", "ro", "rs", "ru", "sa", "se", "sg", "si", "sk", "th", "tr", "tw", "ua", "us", "ve", "za");
    }

}


