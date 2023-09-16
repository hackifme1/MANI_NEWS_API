package com.maniproject.newswave.service;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import org.springframework.stereotype.Service;

@Service
public class PrometheusMetricsService {

    private final CollectorRegistry collectorRegistry;
    private Counter headlinesCallsTotal;
    private Counter externalApiCallsTotal;

    private Counter cacheHitCount;

    private Counter cacheMissCount;

    public PrometheusMetricsService(CollectorRegistry collectorRegistry) {
        this.collectorRegistry = collectorRegistry;
        this.headlinesCallsTotal = Counter.build()
                .name("headlines_calls_total")
                .help("Total number of calls to headlines endpoint")
                .labelNames("country", "category")
                .register(collectorRegistry);

        this.externalApiCallsTotal= Counter.build()
                .name("external_api_calls_total")
                .help("Total number of calls to external api")
                .labelNames("api_name", "status_code")
                .register(collectorRegistry);

        this.cacheHitCount = Counter.build()
                .name("cache_hit_count")
                .help("Total number of cache hits")
                .register(collectorRegistry);

        this.cacheMissCount = Counter.build()
                .name("cache_miss_count")
                .help("Total number of cache misses")
                .register(collectorRegistry);


    }

    public void incrementHeadlinesCallsTotal(String country, String category) {
        headlinesCallsTotal.labels(country, category).inc();
    }

    public void incrementExternalApiCallsTotal(String apiName, String statusCode) {
        externalApiCallsTotal.labels(apiName, statusCode).inc();
    }

    public void incrementCacheHitCount() {
        cacheHitCount.inc();
    }
    public void incrementCacheMissCount() {
        cacheMissCount.inc();
    }

}