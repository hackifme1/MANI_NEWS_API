package com.maniproject.newswave.service;

import com.maniproject.newswave.repository.ApiCallRecordRepository;
import com.maniproject.newswave.repository.EndpointMetricsRepository;
import com.maniproject.newswave.entity.ApiCallRecord;
import com.maniproject.newswave.entity.EndpointMetrics;
import com.maniproject.newswave.entity.User;
import com.maniproject.newswave.factory.NewsProviderFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ApiCallRecordService {

    private final ApiCallRecordRepository apiCallRecordRepository;
    private final EndpointMetricsRepository endpointMetricsRepository;

    private final NewsProviderFactory newsProviderFactory;

    public ApiCallRecordService(ApiCallRecordRepository apiCallRecordRepository, EndpointMetricsRepository endpointMetricsRepository, @Value("${billing.everything}") Double everythingCost, @Value("${billing.topheadlines}") Double topheadlinesCost, @Value("${billing.sources}") Double sourcesCost, NewsProviderFactory newsProviderFactory) {
        this.apiCallRecordRepository = apiCallRecordRepository;
        this.endpointMetricsRepository = endpointMetricsRepository;
        this.newsProviderFactory = newsProviderFactory;
    }

    public void recordApiCall(String endpointName, String url, String response, long responseTime, User user) {
        ApiCallRecord apiCallRecord = new ApiCallRecord();
        apiCallRecord.setEndpointName(endpointName);
        apiCallRecord.setRequest(url);
        apiCallRecord.setResponse(response);
        apiCallRecord.setCallTime(LocalDateTime.now());
        apiCallRecord.setResponseTime(responseTime);
        apiCallRecord.setUser(user);
        apiCallRecordRepository.save(apiCallRecord);
        log.info("Recorded API call for endpoint:{} for user:{}",endpointName, user.getEmail());
        updateEndpointMetrics(endpointName);

    }

    public void updateEndpointMetrics(String endpointName) {
        List<ApiCallRecord> records = apiCallRecordRepository.findByEndpointName(endpointName);

        double avgResponseTime = calculateAverageResponseTime(records);
        long p99Time = calculateP99Time(records);
        long numCalls = records.size();

        // Check if an existing metrics record exists
        Optional<EndpointMetrics> existingMetrics = endpointMetricsRepository.findById(endpointName);

        if (existingMetrics.isPresent()) {
            EndpointMetrics metrics = existingMetrics.get();
            setMetrics(metrics,avgResponseTime,p99Time,numCalls);
            endpointMetricsRepository.save(metrics);
        } else {
            EndpointMetrics newMetrics = new EndpointMetrics();
            newMetrics.setEndpointName(endpointName);
            setMetrics(newMetrics,avgResponseTime,p99Time,numCalls);
            endpointMetricsRepository.save(newMetrics);
        }
    }
    public void setMetrics(EndpointMetrics metrics,double avgResponseTime,long p99Time, long numCalls)
    {
        metrics.setAvgResponseTime(avgResponseTime);
        metrics.setP99Time(p99Time);
        metrics.setNumCalls(numCalls);
    }


    public long calculateP99Time(List<ApiCallRecord> records) {
        List<Long> responseTimes = records.stream()
                .map(ApiCallRecord::getResponseTime)
                .collect(Collectors.toList());

        responseTimes.sort(Long::compare);

        double p99Index = (99.0 / 100) * responseTimes.size();
        int roundedP99Index = (int) Math.round(p99Index); // Round to nearest integer

        return responseTimes.get(roundedP99Index - 1);

    }

    public long getNumberOfCallsForEndpoint(String endpointName) {
        return apiCallRecordRepository.countByEndpointName(endpointName);
    }

    public double calculateAverageResponseTime(List<ApiCallRecord> records) {
        double totalResponseTime = records.stream()
                .mapToLong(ApiCallRecord::getResponseTime)
                .sum();

        if(records.size() == 0)
            return 0;
        return totalResponseTime / records.size();
    }

    public Map<String, Double> totalCostOfApi() {
        Map<String, Double> totalCosts = new HashMap<>();
        totalCosts.put("newsapi", newsProviderFactory.getNewsProvider("newsapi").getCostPerRequest() * getNumberOfCallsForEndpoint("/v2/top-headlines"));
        totalCosts.put("newsio", newsProviderFactory.getNewsProvider("newsio").getCostPerRequest() * getNumberOfCallsForEndpoint("/api/1/news"));

        return totalCosts;
    }


    public List<EndpointMetrics> getEndpointMetrics() {
        return endpointMetricsRepository.findAll();
    }
}
