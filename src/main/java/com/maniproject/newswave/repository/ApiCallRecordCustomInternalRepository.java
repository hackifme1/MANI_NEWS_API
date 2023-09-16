package com.maniproject.newswave.repository;

import java.time.LocalDateTime;
import java.util.List;

public interface ApiCallRecordCustomInternalRepository {
    List<Object[]> getUsersWithApiCallCount(LocalDateTime startDate, LocalDateTime endDate);

}
