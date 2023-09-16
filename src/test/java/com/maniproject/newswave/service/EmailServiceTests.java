package com.maniproject.newswave.service;

import com.maniproject.newswave.entity.Headline;
import com.maniproject.newswave.entity.LeaderboardEntryDTO;
import com.maniproject.newswave.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.ITemplateEngine;

import javax.mail.internet.MimeMessage;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTests {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private ITemplateEngine templateEngine;

    @Mock
    private MimeMessage mimeMessage;


    @Test
    public void testSendWelcomeEmailFailure() {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        MimeMessageHelper mimeMessageHelper = mock(MimeMessageHelper.class);

        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        assertThrows(RuntimeException.class,
                () -> emailService.sendWelcomeEmail("user@example.com"));
    }

    @Test
    public void testSendVerificationEmailFailure() {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        MimeMessageHelper mimeMessageHelper = mock(MimeMessageHelper.class);

        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        User user = new User();

        assertThrows(RuntimeException.class,
                () -> emailService.sendVerificationMail(user));
    }

    @Test
    public void testSendHeadlinesEmailFailure() {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        MimeMessageHelper mimeMessageHelper = mock(MimeMessageHelper.class);

        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        User user = new User();
        List<Headline> headlines = List.of(new Headline());


        assertThrows(RuntimeException.class,
                () -> emailService.sendHeadlinesMail(user, headlines));
    }

    @Test
    public void testSendAutomatedResponseFailure() {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        MimeMessageHelper mimeMessageHelper = mock(MimeMessageHelper.class);

        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        User user = new User();
        List<Headline> headlines = List.of(new Headline());


        assertThrows(RuntimeException.class,
                () -> emailService.sendAutomatedResponse(user, headlines));
    }

    @Test
    public void testSendLeaderboardMailFailure() {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        MimeMessageHelper mimeMessageHelper = mock(MimeMessageHelper.class);

        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        List<LeaderboardEntryDTO> leaderboard = List.of(new LeaderboardEntryDTO());


        assertThrows(RuntimeException.class,
                () -> emailService.sendLeaderboardMail("example@gmail.com", leaderboard));
    }

    @Test
    public void testSendKeepReadingEmailFailure() {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        MimeMessageHelper mimeMessageHelper = mock(MimeMessageHelper.class);

        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        User user = new User();
        List<Headline> headlines = List.of(new Headline());

        assertThrows(RuntimeException.class,
                () -> emailService.sendKeepReadingHeadlinesMail(user, headlines));
    }







}