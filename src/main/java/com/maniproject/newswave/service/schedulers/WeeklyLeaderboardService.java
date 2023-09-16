package com.maniproject.newswave.service.schedulers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.maniproject.newswave.repository.UserRepository;
import com.maniproject.newswave.entity.LeaderboardEntryDTO;
import com.maniproject.newswave.entity.User;
import com.maniproject.newswave.service.ApiCallRecordInternalService;
import com.maniproject.newswave.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class WeeklyLeaderboardService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ApiCallRecordInternalService apiCallRecordInternalService;

    @Autowired
    public WeeklyLeaderboardService(UserRepository userRepository, EmailService emailService, ApiCallRecordInternalService apiCallRecordInternalService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.apiCallRecordInternalService = apiCallRecordInternalService;
    }

    @Scheduled(cron = "0 0 0 */7 * ?") // Runs every 7 days at midnight (00:00:00)
    public void sendWeeklyLeaderboard() throws JsonProcessingException {
        List<User> users = userRepository.findByIsSubscribedTrueAndIsVerifiedTrue();
        List<Object[]> leaderBoardEntries= apiCallRecordInternalService.getUsersWithApiCallCount();
        List<LeaderboardEntryDTO> leaderboardEntryDTOList= leaderBoardEntries.stream().map(entry -> {
            LeaderboardEntryDTO dto = new LeaderboardEntryDTO();
            dto.setUser((User) entry[0]);
            dto.setTotalCalls((Long) entry[1]);
            return dto;
        }).collect(Collectors.toList());

        for (User user : users) {
            emailService.sendLeaderboardMail(user.getEmail(), leaderboardEntryDTOList);
            log.info("Weekly leaderboard email sent to " + user.getEmail());
        }
    }


}
