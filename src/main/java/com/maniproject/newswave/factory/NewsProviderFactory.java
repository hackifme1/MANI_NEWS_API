package com.maniproject.newswave.factory;

import com.maniproject.newswave.entity.NewsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class NewsProviderFactory {

    @Autowired
    private List<NewsProvider> newsProviders;

    HashMap<String,NewsProvider> newsProviderHashMap = new HashMap<>();

    public NewsProviderFactory(List<NewsProvider> newsProviders) {
        this.newsProviders = newsProviders;
        for (NewsProvider newsProvider : newsProviders) {
            newsProviderHashMap.put(newsProvider.getNewsProviderName(),newsProvider);
        }

    }
    public NewsProvider getNewsProvider(String newsProviderName) {
        return newsProviderHashMap.get(newsProviderName);
    }

}
