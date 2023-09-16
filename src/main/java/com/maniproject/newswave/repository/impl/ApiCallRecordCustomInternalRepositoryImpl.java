package com.maniproject.newswave.repository.impl;
import com.maniproject.newswave.entity.ApiCallRecordInternal;
import com.maniproject.newswave.entity.User;
import com.maniproject.newswave.repository.ApiCallRecordCustomInternalRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public  class ApiCallRecordCustomInternalRepositoryImpl implements ApiCallRecordCustomInternalRepository {
    private final EntityManager entityManager;

    public ApiCallRecordCustomInternalRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Object[]> getUsersWithApiCallCount(LocalDateTime startDate, LocalDateTime endDate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
        Root<ApiCallRecordInternal> apiCallRoot = query.from(ApiCallRecordInternal.class);
        Join<ApiCallRecordInternal, User> userJoin = apiCallRoot.join("user");

        query.multiselect(
                userJoin.alias("user"),
                cb.count(apiCallRoot).alias("apiCallCount")
        );

        Predicate datePredicate = cb.between(apiCallRoot.get("callTime"), startDate, endDate);
        query.where(datePredicate);

        query.groupBy(userJoin);
        query.orderBy(cb.desc(cb.count(apiCallRoot)));

        List<Object[]> resultList = entityManager.createQuery(query)
                .setMaxResults(10)
                .getResultList();

        return resultList;
    }

}
