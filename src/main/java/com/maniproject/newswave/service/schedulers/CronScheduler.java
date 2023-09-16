package com.maniproject.newswave.service.schedulers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.maniproject.newswave.repository.ApiCallRecordInternalRepository;
import com.maniproject.newswave.repository.UserRepository;
import com.maniproject.newswave.entity.Headline;
import com.maniproject.newswave.entity.User;
import com.maniproject.newswave.service.ApiCallRecordInternalService;
import com.maniproject.newswave.service.EmailService;
import com.maniproject.newswave.service.InactiveUserReminderService;
import com.maniproject.newswave.service.NewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class CronScheduler {

    private final UserRepository userRepository;
    private final NewsService newsService;
    private final EmailService emailService;
    private final InactiveUserReminderService inactiveUserReminderService;
    private final ApiCallRecordInternalRepository apiCallRecordInternalRepository;

    private final ApiCallRecordInternalService apiCallRecordInternalService;
    @Autowired
    public CronScheduler(UserRepository userRepository, NewsService newsService, EmailService emailService,
                         InactiveUserReminderService inactiveUserReminderService,
                         ApiCallRecordInternalRepository apiCallRecordInternalRepository,
                         ApiCallRecordInternalService apiCallRecordInternalService) {
        this.userRepository = userRepository;
        this.newsService = newsService;
        this.emailService = emailService;
        this.inactiveUserReminderService = inactiveUserReminderService;
        this.apiCallRecordInternalRepository = apiCallRecordInternalRepository;
        this.apiCallRecordInternalService = apiCallRecordInternalService;
    }

    @Scheduled(cron = "0 */1 * * * *") // Runs every 1 minutes
    public void sendKeepReadingEmailToLoggedInUsers() throws JsonProcessingException {
        log.info("Sending keep reading email to logged in users");
        List<User> users = getLoggedInUsers();

        for (User user : users) {
            List<Headline> headlinesToSend = newsService.getHeadlinesForKeepReading(newsService.getLimitedHeadlines(user.getSelectedCountry(), user.getSelectedCategory(), user)) ;
            log.info("Keep Reading email sent to " + user.getEmail() + " with " + headlinesToSend.size() + " headlines of country: " + user.getSelectedCountry() + " and category: " + user.getSelectedCategory());
            emailService.sendKeepReadingHeadlinesMail(user, headlinesToSend);
        }
    }

    @Scheduled(cron = "0 59 8 * * *") // Runs every day at 8:59 AM
    public void sendReminderToInactiveUsers() throws JsonProcessingException {
        List<User> users = getInactiveUsers();
        for (User user : users) {
            log.info("Reminder sent to " + user.getEmail());
            inactiveUserReminderService.sendReminderMail(user);
            apiCallRecordInternalService.recordApiCall("/reminderSent", user);
        }
    }

    private List<User> getInactiveUsers() {
        List<Object[]> inactiveUsers = apiCallRecordInternalRepository.getInactiveUsers();
        List<User> inactiveUsersList = new ArrayList<>();
        for (Object[] inactiveUser : inactiveUsers) {
            String email = (String) inactiveUser[0];
            User user = userRepository.findByEmail(email);
            inactiveUsersList.add(user);
        }
        return inactiveUsersList;
    }

    private List<User> getLoggedInUsers() {
        List<Object[]> loggedInUsers = apiCallRecordInternalRepository.getLoggedInUsers();
        log.info("Number of logged in users: " + loggedInUsers.size());
        List<User> loggedInUsersList = new ArrayList<>();
        for (Object[] inactiveUser : loggedInUsers) {
            String email = (String) inactiveUser[0];
            User user = userRepository.findByEmail(email);
            loggedInUsersList.add(user);
        }

        return loggedInUsersList;
    }


}
