package com.maniproject.newswave.entity.impl;

import com.maniproject.newswave.entity.NewsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NewsIoProvider implements NewsProvider {

    @Value("${newsapi.apikey_2}")
    private String apiKey;

    @Value("${newsapi.baseurl_2}")
    private String baseUrl;

    @Value("${newsapi.cost_2}")
    private Double costPerRequest;

    @Override
    public String getNewsProviderName() {
        return "newsio";
    }
    @Override
    public String getNewsProviderApiKey() {
        return apiKey;
    }

    @Override
    public String getNewsProviderBaseUrl() {
        return baseUrl;
    }

    @Override
    public String getTopHeadlinesEndpoint(String country,String category, int pageSize) {
        return baseUrl + "?apiKey=" + apiKey + "&country=" + country + "&category=" + category + "&size=" + pageSize;
    }

    @Override
    public String getArticlesKey() {
        return "results";
    }

    @Override
    public Double getCostPerRequest() {
        return costPerRequest;
    }
}
