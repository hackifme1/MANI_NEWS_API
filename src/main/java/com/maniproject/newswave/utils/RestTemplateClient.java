package com.maniproject.newswave.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maniproject.newswave.entity.User;
import com.maniproject.newswave.service.ApiCallRecordService;
import com.maniproject.newswave.service.PrometheusMetricsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Component
@Slf4j
public class RestTemplateClient {
    private final RestTemplate restTemplate;
    private final ApiCallRecordService apiCallRecordService;
   private final PrometheusMetricsService prometheusMetricsService;
    @Autowired
    public RestTemplateClient(RestTemplate restTemplate, ApiCallRecordService apiCallRecordService, PrometheusMetricsService prometheusMetricsService) {
        this.restTemplate = restTemplate;
        this.apiCallRecordService = apiCallRecordService;
        this.prometheusMetricsService = prometheusMetricsService;
    }

    public <T> List<T> getJsonKeyArray(String url, String key, Class<T> elementType, User user) throws JsonProcessingException, RuntimeException {
        ResponseEntity<String> response;
        long startTime, endTime;
        String endpointName = getEndpointNameFromUrl(url);
        try {
            startTime = System.currentTimeMillis();
            response = restTemplate.getForEntity(url, String.class);
            endTime = System.currentTimeMillis();
        } catch (Exception e) {
            log.error("Error while fetching data from external service " + e);
            throw new RuntimeException("Error while fetching data from external service " + e);
        }
        log.info("NewsAPI successfully responded for endpoint " + endpointName);
        String responseBody = response.getBody();
        apiCallRecordService.recordApiCall(endpointName, url, responseBody, endTime - startTime, user);
        prometheusMetricsService.incrementExternalApiCallsTotal(endpointName,response.getStatusCode().toString());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        JsonNode listNode = jsonNode.get(key);
        if (listNode != null && listNode.isArray()) {
            List<T> jsonArray = objectMapper.convertValue(listNode, objectMapper.getTypeFactory().constructCollectionType(List.class, elementType));
            return jsonArray;
        }
        return List.of();
    }

    private String getEndpointNameFromUrl(String url) {
        try {
            URI uri = new URI(url);
            String path = uri.getPath();

            path = path.replaceAll("/$", "");

            return path;
        } catch (URISyntaxException e) {
            return "InvalidURL";
        }
    }

}
