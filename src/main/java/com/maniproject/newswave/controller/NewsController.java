package com.maniproject.newswave.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.maniproject.newswave.entity.Headline;
import com.maniproject.newswave.entity.User;
import com.maniproject.newswave.service.*;
import com.maniproject.newswave.utils.JwtHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class NewsController {
    private final NewsService newsService;
    private final UserService userService;
    private final EmailService emailService;
    private final PrometheusMetricsService prometheusMetricsService;
    private final ApiCallRecordInternalService apiCallRecordInternalService;
    private final InactiveUserReminderService inactiveUserReminderService;

    private final JwtHandler jwtHandler;
    public NewsController(NewsService newsService, UserService userService, EmailService emailService,
                          PrometheusMetricsService prometheusMetricsService, ApiCallRecordInternalService apiCallRecordInternalService,
                          InactiveUserReminderService inactiveUserReminderService, JwtHandler jwtHandler) {
        this.newsService = newsService;
        this.userService = userService;
        this.emailService = emailService;
        this.prometheusMetricsService = prometheusMetricsService;
        this.apiCallRecordInternalService = apiCallRecordInternalService;
        this.inactiveUserReminderService = inactiveUserReminderService;
        this.jwtHandler = jwtHandler;
    }

    @GetMapping("/headlines")
    public List<Headline> fetchHeadlines(String token) throws JsonProcessingException {
        User user = userService.findByEmail(jwtHandler.parseJwt(token));
        prometheusMetricsService.incrementHeadlinesCallsTotal(user.getSelectedCountry(), user.getSelectedCategory());
        List<Headline> fetchedHeadlines= newsService.getLimitedHeadlines(user.getSelectedCountry(), user.getSelectedCategory(), user);
        apiCallRecordInternalService.recordApiCall("/headlines", user);
        return fetchedHeadlines;
    }

    // For internal use
    @GetMapping("/headlines-with-email")
    public List<Headline> sendEmailWithHeadlines(String token) throws JsonProcessingException {
        User user = userService.findByEmail(jwtHandler.parseJwt(token));
        prometheusMetricsService.incrementHeadlinesCallsTotal(user.getSelectedCountry(), user.getSelectedCategory());
        List<Headline> fetchedHeadlines= newsService.getLimitedHeadlines(user.getSelectedCountry(), user.getSelectedCategory(), user);
        emailService.sendHeadlinesMail(user,fetchedHeadlines);
        return fetchedHeadlines;
    }

    // For internal use
    @GetMapping("/headlines-with-articles")
    public ResponseEntity<?> fetchHeadlinesWithArticles(String token) throws JsonProcessingException {
        User user = userService.findByEmail(jwtHandler.parseJwt(token));
        prometheusMetricsService.incrementHeadlinesCallsTotal(user.getSelectedCountry(), user.getSelectedCategory());
        Map<String, Object> responseData = new HashMap<>();
        List<Headline> fetchedHeadlines= newsService.getLimitedHeadlines(user.getSelectedCountry(), user.getSelectedCategory(), user);
        responseData.put("articles", fetchedHeadlines);
        apiCallRecordInternalService.recordApiCall("/headlines", user);
        return ResponseEntity.ok(responseData);
    }
}
