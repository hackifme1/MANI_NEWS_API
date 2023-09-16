package com.maniproject.newswave.repository;

import com.maniproject.newswave.entity.EndpointMetrics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EndpointMetricsRepository extends JpaRepository<EndpointMetrics, String> {
}
