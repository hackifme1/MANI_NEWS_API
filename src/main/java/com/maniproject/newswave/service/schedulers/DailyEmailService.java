package com.maniproject.newswave.service.schedulers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.maniproject.newswave.repository.UserRepository;
import com.maniproject.newswave.entity.Headline;
import com.maniproject.newswave.entity.User;
import com.maniproject.newswave.service.EmailService;
import com.maniproject.newswave.service.NewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class DailyEmailService {

    private final UserRepository userRepository;
    private final NewsService newsService;
    private final EmailService emailService;

    @Autowired
    public DailyEmailService(UserRepository userRepository, NewsService newsService, EmailService emailService) {
        this.userRepository = userRepository;
        this.newsService = newsService;
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 59 8 * * *") // Runs every day at 8:59 AM
    public void sendDailyEmail() throws JsonProcessingException {
        List<User> users = userRepository.findByIsSubscribedTrue();
        for (User user : users) {
            List<Headline> headlinesToSend =  newsService.getHeadlines(newsService.getLimitedHeadlines(user.getSelectedCountry(), user.getSelectedCategory(), user));
            log.info("Daily email sent to " + user.getEmail() + " with " + headlinesToSend.size() + " headlines of country: " + user.getSelectedCountry() + " and category: " + user.getSelectedCategory());
            emailService.sendHeadlinesMail(user, headlinesToSend);
        }
    }


}
