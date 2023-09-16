package com.maniproject.newswave.service;

import com.maniproject.newswave.exception.EmailServiceException;
import com.maniproject.newswave.entity.Headline;
import com.maniproject.newswave.entity.LeaderboardEntryDTO;
import com.maniproject.newswave.entity.User;
import com.maniproject.newswave.utils.JwtHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.util.List;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private ITemplateEngine templateEngine;

    @Autowired
    private JwtHandler jwtHandler;

    @Value("${spring.thymeleaf.baseurl}")
    private String baseUrl;


    @Async
    public void sendWelcomeEmail(String to) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        try {
            Context context = new Context();
            context.setVariable("email", to);
            context.setVariable("verifyToken", jwtHandler.generateVerificationToken(to, 15));
            context.setVariable("subscribeToken", jwtHandler.generateVerificationTokenLong(to));
            context.setVariable("unsubscribeToken", jwtHandler.generateVerificationTokenLong(to));
            context.setVariable("baseUrl", baseUrl);
            String htmlContent = templateEngine.process("welcome", context);
            helper.setTo(to);
            helper.setSubject("Welcome to NewsWave");
            helper.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);
            log.info("Welcome email sent to user: " + to);
        } catch (Exception e) {
            log.error("Could not send welcome email to user: " + to);
            throw new EmailServiceException("Could not send email to the customer, email service down.", e);
        }
    }

    @Async
    public void sendVerificationMail(User user) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
        try {
            Context context = new Context();
            context.setVariable("verifyToken", jwtHandler.generateVerificationToken(user.getEmail(), 15));
            context.setVariable("unsubscribeToken", jwtHandler.generateVerificationTokenLong(user.getEmail()));
            context.setVariable("baseUrl", baseUrl);
            String htmlContent = templateEngine.process("emailverify", context);
            helper.setTo(user.getEmail());
            helper.setSubject("Verify your NewsWave account");
            helper.setText(htmlContent, true);

            javaMailSender.send(message);

        } catch (Exception e) {
            log.error("Could not send verification email to user: " + user.getEmail());
            throw new RuntimeException("Could not send email to the customer, email service down.");
        }
    }

    @Async
    public void sendHeadlinesMail(User user, List<Headline> headlines) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
        try {
            Context context = new Context();
            context.setVariable("email", user.getEmail());
            context.setVariable("category", user.getSelectedCategory());
            context.setVariable("country", user.getSelectedCountry().toUpperCase());
            context.setVariable("headlines", headlines);
            context.setVariable("baseUrl", baseUrl);
            context.setVariable("unsubscribeToken", jwtHandler.generateVerificationTokenLong(user.getEmail()));
            String htmlContent = templateEngine.process("headlines", context);
            helper.setTo(user.getEmail());
            helper.setSubject("Your daily NewsWave digest is here");
            helper.setText(htmlContent, true);

            javaMailSender.send(message);

        } catch (Exception e) {
            log.error("Could not send headlines email to user: " + user.getEmail());
            throw new EmailServiceException("Could not send email to the customer, email service down.", e);
        }
    }

    @Async
    public void sendAutomatedResponse(User user, List<Headline> headlines) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
        try {
            Context context = new Context();
            context.setVariable("email", user.getEmail());
            context.setVariable("headlines", headlines);
            context.setVariable("baseUrl", baseUrl);
            context.setVariable("unsubscribeToken", jwtHandler.generateVerificationTokenLong(user.getEmail()));
            String htmlContent = templateEngine.process("automatedres", context);
            helper.setTo(user.getEmail());
            helper.setSubject("Thank you for contacting NewsWave");
            helper.setText(htmlContent, true);

            javaMailSender.send(message);

        } catch (Exception e) {
            log.error("Could not send headlines email to user: " + user.getEmail());
            throw new EmailServiceException("Could not send email to the customer, email service down.", e);
        }
    }

    public void sendLeaderboardMail(String to, List<LeaderboardEntryDTO> leaderboard) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
        try {
            Context context = new Context();
            context.setVariable("leaders", leaderboard);
            context.setVariable("baseUrl", baseUrl);
            context.setVariable("unsubscribeToken", jwtHandler.generateVerificationTokenLong(to));
            String htmlContent = templateEngine.process("topusers", context);
            helper.setTo(to);
            helper.setSubject("Meet top users of the week");
            helper.setText(htmlContent, true);

            javaMailSender.send(message);

        } catch (Exception e) {
            log.error("Could not send leaderboard email to user: " + to);
            throw new RuntimeException("Could not send email to the customer, email service down.");
        }
    }

    public void sendKeepReadingHeadlinesMail(User user, List<Headline> headlines) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
        try {
            Context context = new Context();
            context.setVariable("email", user.getEmail());
            context.setVariable("category", user.getSelectedCategory());
            context.setVariable("country", user.getSelectedCountry().toUpperCase());
            context.setVariable("headlines", headlines);
            context.setVariable("baseUrl", baseUrl);
            context.setVariable("unsubscribeToken", jwtHandler.generateVerificationTokenLong(user.getEmail()));
            String htmlContent = templateEngine.process("keepreading", context);
            helper.setTo(user.getEmail());
            helper.setSubject("Keep reading with NewsWave today");
            helper.setText(htmlContent, true);

            javaMailSender.send(message);

        } catch (Exception e) {
            log.error("Could not send keep reading email to user: " + user.getEmail());
            throw new RuntimeException("Could not send email to the customer, email service down.");
        }
    }
}
