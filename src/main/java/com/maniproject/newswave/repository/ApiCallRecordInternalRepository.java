package com.maniproject.newswave.repository;

import com.maniproject.newswave.entity.ApiCallRecordInternal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApiCallRecordInternalRepository extends JpaRepository<ApiCallRecordInternal, Long> {

    @Query(value = "SELECT u.email\n" +
            "FROM api_call_records_internal a, users u " +
            "WHERE a.user_id = u.id and u.is_subscribed = true and u.is_verified = true\n" +
            "GROUP BY u.email " +
            "HAVING MAX(call_time) < NOW() - INTERVAL '7 days'",
            nativeQuery = true)
    List<Object[]> getInactiveUsers();

    @Query(value = "SELECT u.email\n" +
            "FROM api_call_records_internal a, users u\n" +
            "WHERE a.user_id = u.id and u.is_subscribed = true and u.is_verified = true and a.endpoint_name = '/login'\n" +
            "GROUP BY u.email\n" +
            "HAVING MAX(call_time) <= NOW() - INTERVAL '30 MINUTE' AND MAX(call_time) >= NOW() - INTERVAL '31 MINUTE'\n",
            nativeQuery = true)
    List<Object[]> getLoggedInUsers();

}
