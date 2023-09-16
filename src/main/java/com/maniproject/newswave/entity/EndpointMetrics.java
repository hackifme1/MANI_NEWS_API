package com.maniproject.newswave.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "endpoint_metrics")
public class EndpointMetrics {

    @Id
    @Column(name = "endpoint_name")
    private String endpointName;

    @Column(name = "avg_response_time")
    private Double avgResponseTime;

    @Column(name = "p99_time")
    private Long p99Time;

    @Column(name = "num_calls")
    private Long numCalls;

    public void setAvgResponseTime(double avgResponseTime) {
        this.avgResponseTime = avgResponseTime;
    }

    public void setP99Time(long p99Time) {
        this.p99Time = p99Time;
    }

    public void setNumCalls(long numCalls) {
        this.numCalls=numCalls;
    }

    public void setEndpointName(String endpointName) {
        this.endpointName= endpointName;
    }
}
