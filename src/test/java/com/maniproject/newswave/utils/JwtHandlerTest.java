package com.maniproject.newswave.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtHandlerTest {

    @InjectMocks
    private JwtHandler jwtHandler;

    @Mock
    private Jws<Claims> jwsMock;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(jwtHandler, "jwtSecret", "RANDOM_SECRET_KEY");
    }

    @Test
    public void testGenerateVerificationToken() {
        String email = "test@example.com";
        long numMinutes = 30;
        String token = jwtHandler.generateVerificationToken(email, numMinutes);

        assertNotNull(token);
    }

    @Test
    public void testGenerateVerificationTokenLong() {
        String email = "test@example.com";
        String token = jwtHandler.generateVerificationTokenLong(email);

        assertNotNull(token);
    }

    @Test
    public void testParseJwt() {
        String email = "test@example.com";
        String token = jwtHandler.generateVerificationToken(email, 30);

        String parsedEmail = jwtHandler.parseJwt(token);

        assertEquals(email, parsedEmail);
    }
}
