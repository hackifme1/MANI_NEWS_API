package com.maniproject.newswave.service;

import com.maniproject.newswave.entity.Headline;
import com.maniproject.newswave.entity.JavaMailReceivedEvent;
import com.maniproject.newswave.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class EmailListenerService implements ApplicationListener<JavaMailReceivedEvent> {

    private final EmailService emailService;
    private final UserService userService;
    private final NewsService newsService;

    @Autowired
    public EmailListenerService(EmailService emailService, UserService userService, NewsService newsService) {
        this.emailService = emailService;
        this.userService = userService;
        this.newsService = newsService;
    }

    @Override
    public void onApplicationEvent(JavaMailReceivedEvent event) {
        // Extract information from the received email (event)
        String sender = event.getSender();
        try {
            User user = userService.findByEmail(sender);
            log.info("User: " + user.getEmail());
            if(user.getIsSubscribed() && user.getisVerified()) {
                List<Headline> fetchedHeadlines = newsService.getHeadlines(newsService.getLimitedHeadlines(user.getSelectedCountry(), user.getSelectedCategory(), user));
                emailService.sendAutomatedResponse(user, fetchedHeadlines);
            }
        } catch (Exception e) {
            log.error("Could not send automated response to user: {}", sender);
        }
    }
}
