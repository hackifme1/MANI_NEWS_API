package com.maniproject.newswave.configuration;

import com.maniproject.newswave.service.PrometheusMetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig extends CachingConfigurerSupport {
    private static final long EVICTION_INTERVAL = TimeUnit.HOURS.toMillis(4);
    @Autowired
    private PrometheusMetricsService prometheusMetricsService;
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("apiResponse") {
            @Override
            protected Cache createConcurrentMapCache(String name) {
                return new EvictingCache(new ConcurrentMapCache(name), EVICTION_INTERVAL, prometheusMetricsService);
            }
        };
    }
}