package com.maniproject.newswave.entity.impl;

import com.maniproject.newswave.entity.NewsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NewsApiProvider implements NewsProvider {
    @Value("${newsapi.apikey_1}")
    private String apiKey;

    @Value("${newsapi.baseurl_1}")
    private String baseUrl;

    @Value("${newsapi.cost_1}")
    private Double costPerRequest;

    @Override
    public String getNewsProviderName() {
        return "newsapi";
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
    public String getTopHeadlinesEndpoint(String country, String category, int pageSize) {
        return baseUrl + "?apiKey=" + apiKey + "&country=" + country + "&category=" + category + "&pageSize=" + pageSize;
    }

    @Override
    public String getArticlesKey() {
        return "articles";
    }

    @Override
    public Double getCostPerRequest() {
        return costPerRequest;
    }
}
