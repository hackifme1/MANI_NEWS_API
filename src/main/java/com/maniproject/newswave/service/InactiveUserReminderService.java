package com.maniproject.newswave.service;

import com.maniproject.newswave.repository.UserRepository;
import com.maniproject.newswave.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class InactiveUserReminderService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private ITemplateEngine templateEngine;

    @Autowired
    private UserRepository userRepository;

    public void sendReminderMail(User user) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
        HashMap<String, Integer> topThreeCountries = getTopThreeCountries();
        HashMap<String, Integer> topThreeCategories = getTopThreeCategories();
        try {
            Context context = new Context();
            context.setVariable("topThreeCountries", topThreeCountries);
            context.setVariable("topThreeCategories", topThreeCategories);
            String htmlContent = templateEngine.process("reminder", context);
            helper.setTo(user.getEmail());
            helper.setSubject("[Reminder] WE MISS YOU!");
            helper.setText(htmlContent, true);

            javaMailSender.send(message);

        } catch (Exception e) {
            log.error("Could not send reminder email to user: " + user.getEmail());
            throw new RuntimeException("Could not send email to the customer, email service down.");
        }
    }

    private HashMap<String, Integer> getTopThreeCountries() {
        List<Object[]> topThreeCountries = userRepository.getTopThreeCountries();
        HashMap<String, Integer> topThreeCountriesMap = new HashMap<>();

        for (Object[] topThreeCountry : topThreeCountries) {
            topThreeCountriesMap.put((String) topThreeCountry[0], ((BigInteger) topThreeCountry[1]).intValue());
        }

        return topThreeCountriesMap;
    }

    private HashMap<String, Integer> getTopThreeCategories() {
        List<Object[]> topThreeCategories = userRepository.getTopThreeCategories();
        HashMap<String, Integer> topThreeCategoriesMap = new HashMap<>();

        for (Object[] topThreeCategory : topThreeCategories) {
            topThreeCategoriesMap.put((String) topThreeCategory[0], ((BigInteger) topThreeCategory[1]).intValue());
        }

        return topThreeCategoriesMap;
    }
}
