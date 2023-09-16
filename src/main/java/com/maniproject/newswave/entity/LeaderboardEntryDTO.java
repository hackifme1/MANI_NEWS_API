package com.maniproject.newswave.entity;

import lombok.Data;

@Data
public class LeaderboardEntryDTO {
    private User user;
    private Long totalCalls;

    public void setUser(User user) {
        this.user = user;
    }

    public void setTotalCalls(Long totalCalls) {
        this.totalCalls = totalCalls;
    }
}
