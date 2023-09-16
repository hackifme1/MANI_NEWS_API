package com.maniproject.newswave.controller;

import com.maniproject.newswave.entity.User;
import com.maniproject.newswave.service.ApiCallRecordInternalService;
import com.maniproject.newswave.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private ApiCallRecordInternalService apiCallRecordInternalService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        userController = new UserController(userService, apiCallRecordInternalService);
    }

    @Test
    public void testRegister() {
        String email = "test@example.com";
        String selectedCountry = "US";
        String selectedCategory = "General";

        Map<String, String> reqBody = new HashMap<>();
        reqBody.put("email", email);
        reqBody.put("selectedCountry", selectedCountry);
        reqBody.put("selectedCategory", selectedCategory);

        User user = new User();
        user.setEmail(email);

        when(userService.findByEmail(email)).thenReturn(user);

        Map<String, String> responseEntity = userController.register(reqBody);

        verify(userService).addUser(email, selectedCountry, selectedCategory);
        verify(apiCallRecordInternalService).recordApiCall("/register", user);

        assertEquals("Registration successful, Proceed to login", responseEntity.get("message"));
    }

    @Test
    public void testLogin() {
        String email = "test@example.com";
        String token = "test-token";

        User user = new User();
        user.setEmail(email);

        when(userService.findByEmail(email)).thenReturn(user);
        when(userService.loginUser(email)).thenReturn(token);

        Map<String, String> responseEntity = userController.login(email);

        verify(apiCallRecordInternalService).recordApiCall("/login", user);
    }

    @Test
    public void testSubscribe() {
        String token = "test-token";
        Map<String, String> expectedResult = new HashMap<>();
        expectedResult.put("message", "Subscribed successfully");

        when(userService.subscribeToEmail(token)).thenReturn(expectedResult);

        Map<String ,String> responseEntity = userController.subscribe(token);
    }

    @Test
    public void testUnsubscribe() {
        String token = "test-token";
        Map<String, String> expectedResult = new HashMap<>();
        expectedResult.put("message", "Unsubscribed successfully");

        when(userService.unsubscribeToEmail(token)).thenReturn(expectedResult);

        Map<String, String> responseEntity = userController.unsubscribe(token);
    }

}