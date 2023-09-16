package com.maniproject.newswave.service;

import com.maniproject.newswave.repository.ApiCallRecordCustomInternalRepository;
import com.maniproject.newswave.repository.ApiCallRecordInternalRepository;
import com.maniproject.newswave.entity.ApiCallRecordInternal;
import com.maniproject.newswave.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
public class ApiCallRecordInternalService {

    private final ApiCallRecordInternalRepository apiCallRecordInternalRepository;
    private final ApiCallRecordCustomInternalRepository apiCallRecordCustomInternalRepository;

    public ApiCallRecordInternalService(ApiCallRecordInternalRepository apiCallRecordInternalRepository, ApiCallRecordCustomInternalRepository apiCallRecordCustomInternalRepository) {
        this.apiCallRecordInternalRepository = apiCallRecordInternalRepository;
        this.apiCallRecordCustomInternalRepository = apiCallRecordCustomInternalRepository;
    }

    public void recordApiCall(String endpointName, User user) {
        ApiCallRecordInternal apiCallRecordInternal = new ApiCallRecordInternal();
        apiCallRecordInternal.setEndpointName(endpointName);
        apiCallRecordInternal.setCallTime(LocalDateTime.now());
        apiCallRecordInternal.setUser(user);
        apiCallRecordInternalRepository.save(apiCallRecordInternal);
        log.info("Recorded API call for endpoint:{} for user:{}",endpointName, user.getEmail());
    }

    public List<Object[]> getUsersWithApiCallCount() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minus(7, ChronoUnit.DAYS);
        return apiCallRecordCustomInternalRepository.getUsersWithApiCallCount(startDate, endDate);
    }
}
