package com.maniproject.newswave.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "api_call_records")
@Data
public class ApiCallRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String endpointName;
    private String request;
    private String response;
    private Long responseTime; // Time taken for response in milliseconds
    private LocalDateTime callTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public ApiCallRecord(Long i) {
        this.responseTime = i;
    }

    public ApiCallRecord() {
    }

}
