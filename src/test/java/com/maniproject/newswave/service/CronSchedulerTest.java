package com.maniproject.newswave.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.maniproject.newswave.entity.User;
import com.maniproject.newswave.repository.ApiCallRecordInternalRepository;
import com.maniproject.newswave.repository.UserRepository;
import com.maniproject.newswave.service.schedulers.CronScheduler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static java.util.List.of;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@TestPropertySource(properties = "scheduling.enabled=false")
@ExtendWith(MockitoExtension.class)
class CronSchedulerTest {
    @InjectMocks
    private CronScheduler cronScheduler;
    @Mock
    private EmailService emailService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private NewsService newsService;

    @Mock
    private ApiCallRecordInternalRepository apiCallRecordInternalRepository;

    @Mock
    private InactiveUserReminderService inactiveUserReminderService;

    @Mock
    private ApiCallRecordInternalService apiCallRecordInternalService;

    @Mock
    private UserService userService;

    @Test
    public void sendKeepReadingEmailToLoggedInUsers() throws JsonProcessingException {

        User user = new User();
        user.setEmail("test@example.com");
        user.setSelectedCountry("in");
        user.setSelectedCategory("general");
        user.setNewsProvider("newsapi");

        List<Object[]> dataList = new ArrayList<>();
        dataList.add(new Object[]{"test@gmail.com","in","general"});
        when(apiCallRecordInternalRepository.getLoggedInUsers()).thenReturn(dataList);
        when(userRepository.findByEmail(any())).thenReturn(user);

        cronScheduler.sendKeepReadingEmailToLoggedInUsers();

        verify(emailService, Mockito.times(1)).sendKeepReadingHeadlinesMail(any(), any());
    }

    @Test
    public void sendReminderToInactiveUsers() throws JsonProcessingException {
        User user = new User();
        user.setEmail("test@example.com");
        user.setSelectedCountry("in");
        user.setSelectedCategory("general");
        user.setNewsProvider("newsapi");

        List<Object[]> dataList = new ArrayList<>();
        dataList.add(new Object[]{"test@gmail.com","in","general"});
        when(apiCallRecordInternalRepository.getInactiveUsers()).thenReturn(dataList);
        when(userRepository.findByEmail(any())).thenReturn(user);
        doNothing().when(apiCallRecordInternalService).recordApiCall(anyString(),any());


        cronScheduler.sendReminderToInactiveUsers();

        verify(inactiveUserReminderService, Mockito.times(1)).sendReminderMail(any());

    }

}