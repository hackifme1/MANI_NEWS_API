package com.maniproject.newswave.controller;

import com.maniproject.newswave.entity.Headline;
import com.maniproject.newswave.entity.User;
import com.maniproject.newswave.service.*;
import com.navibootcamp.newswave.service.*;
import com.maniproject.newswave.utils.JwtHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class NewsControllerTest {
    @Mock
    private NewsService newsService;
    @Mock
    private UserService userService;
    @Mock
    private EmailService emailService;
    @Mock
    private PrometheusMetricsService prometheusMetricsService;
    @Mock
    private ApiCallRecordInternalService apiCallRecordInternalService;
    @Mock
    private InactiveUserReminderService inactiveUserReminderService;
    @Mock
    private JwtHandler jwtHandler;
    @InjectMocks
    private NewsController newsController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        newsController = new NewsController(newsService, userService, emailService,
                prometheusMetricsService, apiCallRecordInternalService,
                inactiveUserReminderService, jwtHandler);
    }

    @Test
    public void testFetchHeadlines() throws Exception {
        // Mocking user and user service
        User user = new User();
        user.setSelectedCountry("US");
        user.setSelectedCategory("sports");
        when(userService.findByEmail(any())).thenReturn(user);

        // Mocking news service
        List<Headline> mockHeadlines = new ArrayList<>(); // Create a mock list of headlines
        when(newsService.getLimitedHeadlines(any(), any(), any())).thenReturn(mockHeadlines);

        // Call the method
        List<Headline> result = newsController.fetchHeadlines("mocked_token");

        // Verify interactions and assertions
        verify(prometheusMetricsService).incrementHeadlinesCallsTotal("US", "sports");
        verify(apiCallRecordInternalService).recordApiCall("/headlines", user);
        assertEquals(mockHeadlines, result);
    }

    @Test
    public void testSendEmailWithHeadlines() throws Exception {
        // Mocking user and user service
        User user = new User();
        user.setSelectedCountry("US");
        user.setSelectedCategory("sports");
        when(userService.findByEmail(any())).thenReturn(user);

        // Mocking news service
        List<Headline> mockHeadlines = new ArrayList<>(); // Create a mock list of headlines
        when(newsService.getLimitedHeadlines(any(), any(), any())).thenReturn(mockHeadlines);

        // Call the method
        List<Headline> result = newsController.sendEmailWithHeadlines("mocked_token");

        // Verify interactions and assertions
        verify(prometheusMetricsService).incrementHeadlinesCallsTotal("US", "sports");
        verify(emailService).sendHeadlinesMail(user, mockHeadlines);
        assertEquals(mockHeadlines, result);
    }

}