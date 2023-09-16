package com.maniproject.newswave.service;

import com.maniproject.newswave.entity.User;
import com.maniproject.newswave.exception.*;
import com.maniproject.newswave.repository.UserRepository;
import com.maniproject.newswave.utils.JwtHandler;
import com.maniproject.newswave.utils.category.CategoryServiceProvider1;
import com.maniproject.newswave.utils.country.CountryServiceProvider1;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private CountryServiceProvider1 countryServiceProvider1;

    @Mock
    private CategoryServiceProvider1 categoryServiceProvider1;

    @Mock
    private UserRepository userRepository;

    @Mock
    EmailService emailService;

    @Mock
    private JwtHandler jwtHandler;

    @InjectMocks
    private UserService userService;

    @Test
    public void testAddUser_Success() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(countryServiceProvider1.getCountries()).thenReturn(Arrays.asList("us"));
        when(categoryServiceProvider1.getCategories()).thenReturn(Arrays.asList("business"));

        String email = "test@example.com";
        String selectedCountry = "us";
        String selectedCategory = "business";

        assertDoesNotThrow(() -> userService.addUser(email, selectedCountry, selectedCategory));
    }

    @Test
    public void testAddUser_InvalidEmail() {
        String email = "invalid-email";
        String selectedCountry = "us";
        String selectedCategory = "business";

        assertThrows(EmailIsInvalidException.class,
                () -> userService.addUser(email, selectedCountry, selectedCategory));
    }

    @Test
    public void testAddUser_DuplicateEmail() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        String email = "test@example.com";
        String selectedCountry = "us";
        String selectedCategory = "business";

        assertThrows(EmailAlreadyExistException.class,
                () -> userService.addUser(email, selectedCountry, selectedCategory));
    }

    @Test
    public void testAddUser_InvalidCountry() {
        when(countryServiceProvider1.getCountries()).thenReturn(Arrays.asList("us"));

        String email = "test@example.com";
        String selectedCountry = "invalid-country";
        String selectedCategory = "business";

        assertThrows(CountryIsInvalidException.class,
                () -> userService.addUser(email, selectedCountry, selectedCategory));
    }

    @Test
    public void testAddUser_InvalidCategory() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(countryServiceProvider1.getCountries()).thenReturn(Arrays.asList("us"));
        when(categoryServiceProvider1.getCategories()).thenReturn(Arrays.asList("business"));

        String email = "test@example.com";
        String selectedCountry = "us";
        String selectedCategory = "invalid-category";

        assertThrows(CategoryIsInvalidException.class,
                () -> userService.addUser(email, selectedCountry, selectedCategory));
    }

    @Test
    void testLoginUser_ValidEmail() {
        String email = "test@example.com";
        User user = new User();
        user.setVerified(true);

        when(userRepository.existsByEmail(email)).thenReturn(true);
        when(userRepository.findByEmail(email)).thenReturn(user);
        when(jwtHandler.generateVerificationTokenLong(email)).thenReturn("test-token");

        String token = userService.loginUser(email);

        verify(jwtHandler).generateVerificationTokenLong(email);

        assertNotNull(token);
        assertEquals("test-token", token);
    }

    @Test
    void testLoginUser_InvalidEmail() {
        String email = "invalid-email";

        assertThrows(IllegalArgumentException.class,
                () -> userService.loginUser(email));

        verifyNoInteractions(userRepository, jwtHandler);
    }

    @Test
    void testLoginUser_UserNotFound() {
        String email = "test@gmail.com";

        when(userRepository.existsByEmail(email)).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> userService.loginUser(email));

        verifyNoInteractions(jwtHandler);
    }

    @Test
    void testLoginUser_UserNotVerified() {
        String email = "test@gmail.com";
        User user = new User();
        user.setVerified(false);

        when(userRepository.existsByEmail(email)).thenReturn(true);
        when(userRepository.findByEmail(email)).thenReturn(user);

        assertThrows(IllegalArgumentException.class,
                () -> userService.loginUser(email));

        verifyNoInteractions(jwtHandler);
    }

    @Test
    void testLoginUser_UserVerified() {
        String email = "test@gmail.com";
        User user = new User();
        user.setVerified(true);

        when(userRepository.existsByEmail(email)).thenReturn(true);
        when(userRepository.findByEmail(email)).thenReturn(user);
        when(jwtHandler.generateVerificationTokenLong(email)).thenReturn("test-token");

        String token = userService.loginUser(email);

        verify(jwtHandler).generateVerificationTokenLong(email);

        assertNotNull(token);
        assertEquals("test-token", token);
    }

    @Test
    void testSubscribeToEmail_ValidToken() {
        String token = "valid-test-token";
        String email = "test@example.com";

        User user = new User();
        when(jwtHandler.parseJwt(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(user);

        Map<String, String> result = userService.subscribeToEmail(token);

        verify(userRepository).save(user);

        assertNotNull(result);
        assertEquals("User subscribed to email updates", result.get("message"));
    }

    @Test
    void testSubscribeToEmail_InvalidToken() {
        String token = "invalid-test-token";
        String email = "test@gmail.com";

        when(jwtHandler.parseJwt(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(null);

        assertThrows(EmailRecordNotFoundException.class, () -> userService.subscribeToEmail(token));
    }

    @Test
    void testUnsubscribeToEmail_ValidToken() {
        String token = "valid-test-token";
        String email = "test@example.com";

        User user = new User();
        when(jwtHandler.parseJwt(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(user);

        Map<String, String> result = userService.unsubscribeToEmail(token);

        verify(userRepository).save(user);
        assertNotNull(result);
        assertEquals("User unsubscribed from email updates", result.get("message"));
    }

    @Test
    void testUnsubscribeToEmail_InvalidToken() {
        String token = "invalid-test-token";
        String email = "test@gmail.com";

        when(jwtHandler.parseJwt(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(null);

        assertThrows(EmailRecordNotFoundException.class, () -> userService.unsubscribeToEmail(token));
    }

    @Test
    void testVerifyEmail_ValidToken() {
        String token = "test-token";
        String email = "test@example.com";
        User user = new User();
        when(jwtHandler.isTokenExpired(token)).thenReturn(false);
        when(jwtHandler.parseJwt(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(user);

        Map<String, String> result = userService.verifyEmail(token);

        verify(userRepository).save(user);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.containsKey("message"));
        assertEquals("User verified with email: " + email, result.get("message"));
    }

    @Test
    void testVerifyEmail_InvalidToken() {
        String token = "test-token";
        String email = "test@example.com";
        User user = new User();
        when(jwtHandler.isTokenExpired(token)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.verifyEmail(token));
    }

    @Test
    void testGetProfile() {
        String token = "test-token";
        String email = "test@example.com";
        User user = new User();
        user.setSelectedCountry("US");
        user.setSelectedCategory("Technology");
        user.setEmail(email);
        when(jwtHandler.parseJwt(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(user);

        Map<String, String> profile = userService.getProfile(token);

        // Verify the returned profile values
        assertEquals("test@example.com", profile.get("email"));
        assertEquals("US", profile.get("selectedCountry"));
        assertEquals("Technology", profile.get("selectedCategory"));
    }

    @Test
    void testUpdateProvider_ValidProvider() {
        String token = "test-token";
        String newsProvider = "newsapi";
        String email = "test@example.com";

        User user = new User();
        user.setSelectedCountry("us");
        user.setSelectedCategory("technology");

        when(jwtHandler.parseJwt(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(user);

        userService.updateProvider(token, newsProvider);

        // Verify that the user's news provider has been updated
        verify(userRepository).save(user);
    }

    @Test
    void testUpdateProvider_InvalidProvider() {
        String token = "test-token";
        String newsProvider = "invalid-provider";
        String email = "test@example.com";

        User user = new User();
        user.setSelectedCountry("us");
        user.setSelectedCategory("technology");

        assertThrows(InvalidNewsProvider.class, () -> userService.updateProvider(token, newsProvider));

        // Verify that the user's news provider has not been updated
        verify(userRepository, never()).save(user);
    }

    @Test
    void testGetUserByToken_ValidToken() {
        String token = "test-token";
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);

        when(jwtHandler.parseJwt(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(user);

        User result = userService.getUserByToken(token);

        assertEquals(user, result);
    }

    @Test
    void testGetUserByToken_InvalidToken() {
        String token = "invalid-token";
        String email = "test@example.com";

        when(jwtHandler.parseJwt(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(null);

        assertThrows(EmailRecordNotFoundException.class, () -> {
            userService.getUserByToken(token);
        });
    }

    @Test
    void testFindByEmail_ValidEmail() {
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(user);

        User result = userService.findByEmail(email);

        assertEquals(user, result);
    }

    @Test
    void testFindByEmail_InvalidEmail() {
        String email = "invalid@example.com";

        when(userRepository.findByEmail(email)).thenReturn(null);

        assertThrows(EmailRecordNotFoundException.class, () -> {
            userService.findByEmail(email);
        });
    }
}