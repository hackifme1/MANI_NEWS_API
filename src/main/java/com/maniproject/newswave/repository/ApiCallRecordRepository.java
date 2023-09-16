package com.maniproject.newswave.repository;


import com.maniproject.newswave.entity.ApiCallRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApiCallRecordRepository extends JpaRepository<ApiCallRecord, Long> {
    List<ApiCallRecord> findByEndpointName(String endpointName);
    long countByEndpointName(String endpointName);
}