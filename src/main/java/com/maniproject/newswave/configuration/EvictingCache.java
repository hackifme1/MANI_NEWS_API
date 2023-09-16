package com.maniproject.newswave.configuration;

import com.maniproject.newswave.service.PrometheusMetricsService;
import org.springframework.cache.Cache;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;


public class EvictingCache implements Cache {

    private PrometheusMetricsService prometheusMetricsService;
    private final Cache delegate;
    private final long entryEvictionInterval; // in milliseconds

    private final Map<Object, Long> entryCreationTimes = new ConcurrentHashMap<>();

    public EvictingCache(Cache delegate, long entryEvictionInterval, PrometheusMetricsService prometheusMetricsService) {
        this.delegate = delegate;
        this.entryEvictionInterval = entryEvictionInterval;
        this.prometheusMetricsService = prometheusMetricsService;
    }

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public Object getNativeCache() {
        return delegate.getNativeCache();
    }

    @Override
    public ValueWrapper get(Object key) {
        evictIfNecessary(key);
        ValueWrapper valueWrapper = delegate.get(key);
        if (valueWrapper != null) {
            prometheusMetricsService.incrementCacheHitCount();
        } else {
            prometheusMetricsService.incrementCacheMissCount();
        }
        return delegate.get(key);
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        evictIfNecessary(key);
        ValueWrapper valueWrapper = delegate.get(key);
        if (valueWrapper != null) {
            prometheusMetricsService.incrementCacheHitCount();
        } else {
            prometheusMetricsService.incrementCacheMissCount();
        }
        return delegate.get(key, type);
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        evictIfNecessary(key);
        ValueWrapper valueWrapper = delegate.get(key);
        if (valueWrapper != null) {
            prometheusMetricsService.incrementCacheHitCount();
        } else {
            prometheusMetricsService.incrementCacheMissCount();
        }
        return delegate.get(key, valueLoader);
    }

    @Override
    public void put(Object key, Object value) {
        delegate.put(key, value);
        entryCreationTimes.put(key, System.currentTimeMillis());
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        ValueWrapper wrapper = delegate.putIfAbsent(key, value);
        if (wrapper == null) {
            entryCreationTimes.put(key, System.currentTimeMillis());
        }
        return wrapper;
    }

    @Override
    public void evict(Object key) {
        delegate.evict(key);
        entryCreationTimes.remove(key);
    }

    @Override
    public void clear() {
        delegate.clear();
        entryCreationTimes.clear();
    }

    private void evictIfNecessary(Object key) {
        Long creationTime = entryCreationTimes.get(key);
        if (creationTime != null) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - creationTime >= entryEvictionInterval) {
                delegate.evict(key);
                entryCreationTimes.remove(key);
            }
        }
    }
}
