package com.maniproject.newswave.service.schedulers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.maniproject.newswave.entity.Headline;
import com.maniproject.newswave.entity.User;
import com.maniproject.newswave.repository.UserRepository;
import com.maniproject.newswave.service.EmailService;
import com.maniproject.newswave.service.NewsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DailyEmailServiceTest {

    @InjectMocks
    private DailyEmailService dailyEmailService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NewsService newsService;

    @Mock
    private EmailService emailService;

    @Test
    public void testSendDailyEmail() throws JsonProcessingException {
        when(userRepository.findByIsSubscribedTrue()).thenReturn(of(new User(UUID.randomUUID(),"omkar@gmail.com","in","general"),new User(UUID.randomUUID(),"omkar@gmail.com","in","general")));
        when(newsService.getLimitedHeadlines(anyString(),anyString(),any())).thenReturn(of(new Headline()));
        when(newsService.getHeadlines(any())).thenReturn(of(new Headline()));
        dailyEmailService.sendDailyEmail();
        verify(emailService, Mockito.times(2)).sendHeadlinesMail(any(), any());
        assertDoesNotThrow(() -> dailyEmailService.sendDailyEmail());
    }



}