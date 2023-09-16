package com.maniproject.newswave.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.maniproject.newswave.utils.RestTemplateClient;
import com.maniproject.newswave.factory.NewsProviderFactory;
import com.maniproject.newswave.entity.Headline;
import com.maniproject.newswave.entity.NewsProvider;
import com.maniproject.newswave.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class NewsService {

    @Autowired
    private NewsProviderFactory newsProviderFactory;

    private final RestTemplateClient restTemplateClient;

    public NewsService(RestTemplateClient restTemplateClient) {
        this.restTemplateClient = restTemplateClient;
    }

    public List<Headline> getHeadlinesWithPagination(String country, String category, int pageSize, int pageNum, User user) throws JsonProcessingException {
        String newsProviderName = user.getNewsProvider();
        NewsProvider newsProvider = newsProviderFactory.getNewsProvider(newsProviderName);
        String url= newsProvider.getTopHeadlinesEndpoint(country,category,pageSize);
        return restTemplateClient.getJsonKeyArray(url, newsProvider.getArticlesKey() , Headline.class, user);
    }
    @Cacheable(value = "apiResponse", key = "{#country, #category, #user.newsProvider}")
    public List<Headline> getLimitedHeadlines(String country, String category, User user) throws JsonProcessingException {
        final int MAX_HEADLINES = 10;
        return getHeadlinesWithPagination(country, category, MAX_HEADLINES, 1, user);
    }

    public List<Headline> getHeadlines(List<Headline> headlines)  {
        final int NUMBER_OF_ARTICLES = 3;
        int maxSize = Math.min(NUMBER_OF_ARTICLES, headlines.size());
        List<Headline> fetchedHeadlines = headlines.subList(0, maxSize);
        return fetchedHeadlines;
    }

    public List<Headline> getHeadlinesForKeepReading(List<Headline> headlines)  {
        final int NUMBER_OF_ARTICLES = 5;
        int maxSize = Math.min(NUMBER_OF_ARTICLES, headlines.size());
        List<Headline> fetchedHeadlines = headlines.subList(0, maxSize);
        return fetchedHeadlines;
    }

}
