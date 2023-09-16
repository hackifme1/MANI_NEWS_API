package com.maniproject.newswave.service;

import com.maniproject.newswave.entity.User;
import com.maniproject.newswave.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InactiveUserReminderServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private TemplateEngine templateEngine;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private InactiveUserReminderService inactiveUserReminderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendReminderMailFailure() throws MessagingException {
        User user = new User();
        user.setEmail("test@example.com");

        MimeMessage mimeMessage = mock(MimeMessage.class);
        MimeMessageHelper mimeMessageHelper = mock(MimeMessageHelper.class);

        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(userRepository.getTopThreeCountries()).thenReturn(new ArrayList<Object[]>());
        when(userRepository.getTopThreeCategories()).thenReturn(new ArrayList<Object[]>());

        assertThrows(RuntimeException.class,
                () -> inactiveUserReminderService.sendReminderMail(user));
    }
}