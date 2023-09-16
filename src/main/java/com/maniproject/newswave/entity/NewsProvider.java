package com.maniproject.newswave.entity;

public interface NewsProvider {

    public String getNewsProviderName();
    public String getNewsProviderApiKey();
    public String getNewsProviderBaseUrl();
    public String getArticlesKey();

    public Double getCostPerRequest();

    public String getTopHeadlinesEndpoint(String country,String category, int pageSize);
}
