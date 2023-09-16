package com.maniproject.newswave.controller;

import com.maniproject.newswave.entity.EndpointMetrics;
import com.maniproject.newswave.entity.LeaderboardEntryDTO;
import com.maniproject.newswave.entity.User;
import com.maniproject.newswave.service.ApiCallRecordInternalService;
import com.maniproject.newswave.service.ApiCallRecordService;
import com.maniproject.newswave.service.UpdateAllUsersProvidersService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class MetricsController {

    private final ApiCallRecordInternalService apiCallRecordInternalService;
    private final ApiCallRecordService apiCallRecordService;
    private final UpdateAllUsersProvidersService updateAllUsersProvidersService;

    public MetricsController(ApiCallRecordInternalService apiCallRecordInternalService, ApiCallRecordService apiCallRecordService, UpdateAllUsersProvidersService updateAllUsersProvidersService) {
        this.apiCallRecordInternalService = apiCallRecordInternalService;
        this.apiCallRecordService = apiCallRecordService;
        this.updateAllUsersProvidersService = updateAllUsersProvidersService;
    }

    @GetMapping("/metrics")
    public List<EndpointMetrics> fetchMetrics() {
        return apiCallRecordService.getEndpointMetrics();
    }

    @GetMapping("/cost")
    public Map<String,Double> fetchCost() {
        return apiCallRecordService.totalCostOfApi();
    }

    @GetMapping("/leaderboard")
    public List<LeaderboardEntryDTO> fetchLeaderboard() {
        List<Object[]> leaderBoardEntries= apiCallRecordInternalService.getUsersWithApiCallCount();
       List<LeaderboardEntryDTO> leaderboardEntryDTOList= leaderBoardEntries.stream().map(entry -> {
            LeaderboardEntryDTO dto = new LeaderboardEntryDTO();
            dto.setUser((User) entry[0]);
            dto.setTotalCalls((Long) entry[1]);
            return dto;
        }).collect(Collectors.toList());
         return leaderboardEntryDTOList;
    }

    @GetMapping("/update-all-users-provider")
    public Map<String, String> updateProviderAllUsers(@RequestParam String provider) {
        return updateAllUsersProvidersService.updateAllUsersProviders(provider);
    }
}
