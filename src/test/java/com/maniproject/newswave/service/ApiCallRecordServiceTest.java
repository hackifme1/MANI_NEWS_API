package com.maniproject.newswave.service;

import com.maniproject.newswave.entity.ApiCallRecord;
import com.maniproject.newswave.entity.EndpointMetrics;
import com.maniproject.newswave.factory.NewsProviderFactory;
import com.maniproject.newswave.repository.ApiCallRecordRepository;
import com.maniproject.newswave.repository.EndpointMetricsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApiCallRecordServiceTest {

    @Mock
    ApiCallRecordRepository apiCallRecordRepository;

    @Mock
    EndpointMetricsRepository endpointMetricsRepository;

    @Mock
    NewsProviderFactory newsProviderFactory;

    @InjectMocks
    ApiCallRecordService apiCallRecordService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testUpdateEndpointMetrics() {
        String endpointName = "/v2/top-headlines";
        List<ApiCallRecord> records = new ArrayList<>();
        records.add(new ApiCallRecord(100L));
        records.add(new ApiCallRecord(200L));
        records.add(new ApiCallRecord(300L));
        records.add(new ApiCallRecord(400L));

        // Mock the repository to return the above records
        when(apiCallRecordRepository.findByEndpointName(endpointName)).thenReturn(records);

        // Call the method under test
        apiCallRecordService.updateEndpointMetrics(endpointName);

        // Verify that save method was called on endpointMetricsRepository
        verify(endpointMetricsRepository).save(any(EndpointMetrics.class));
        // Additional assertions can be done based on your requirements
    }
    @Test
    void calculateP99Time() {
        List<ApiCallRecord> records = new ArrayList<>();
        records.add(new ApiCallRecord(100L));
        records.add(new ApiCallRecord(200L));
        records.add(new ApiCallRecord(300L));
        records.add(new ApiCallRecord(400L));
        records.add(new ApiCallRecord(500L));

        long p99Time = apiCallRecordService.calculateP99Time(records);

        assertEquals(500L, p99Time);
    }

    @Test
    public void testCalculateAverageResponseTime() {
        // Given
        List<ApiCallRecord> records = Arrays.asList(
                new ApiCallRecord(100L),
                new ApiCallRecord(200L),
                new ApiCallRecord(300L)
        );
        List<ApiCallRecord> emptyRecords = Arrays.asList();

        double avgResponseTime = apiCallRecordService.calculateAverageResponseTime(records);
        double avgResponseTimeForEmptyRecords = apiCallRecordService.calculateAverageResponseTime(emptyRecords);
        assertEquals(200.0, avgResponseTime, 0.01); // Delta for floating point comparison
        assertEquals(0.0, avgResponseTimeForEmptyRecords, 0.01);
    }

    @Test
    public void testGetNumberOfCallsForEndpoint() {
        String endpointName = "/test";
        when(apiCallRecordRepository.countByEndpointName(endpointName)).thenReturn(5L);
        long numCalls = apiCallRecordService.getNumberOfCallsForEndpoint(endpointName);
        assertEquals(5, numCalls);
    }

    @Test
    void testGetEndpointMetrics() {
        // Create some mock endpoint metrics
        EndpointMetrics metrics1 = new EndpointMetrics();
        metrics1.setEndpointName("/v2/top-headlines");
        metrics1.setAvgResponseTime(100);
        metrics1.setP99Time(150);
        metrics1.setNumCalls(50);

        EndpointMetrics metrics2 = new EndpointMetrics();
        metrics2.setEndpointName("/api/1/news");
        metrics2.setAvgResponseTime(120);
        metrics2.setP99Time(180);
        metrics2.setNumCalls(40);

        // Mock the repository to return the above metrics
        when(endpointMetricsRepository.findAll()).thenReturn(Arrays.asList(metrics1, metrics2));

        List<EndpointMetrics> endpointMetrics = apiCallRecordService.getEndpointMetrics();

        assertEquals(2, endpointMetrics.size());
        assertEquals("/v2/top-headlines", endpointMetrics.get(0).getEndpointName());
        assertEquals("/api/1/news", endpointMetrics.get(1).getEndpointName());
    }

}