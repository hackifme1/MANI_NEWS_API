package com.maniproject.newswave.controller;

import com.maniproject.newswave.entity.User;
import com.maniproject.newswave.service.ApiCallRecordInternalService;
import com.maniproject.newswave.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final ApiCallRecordInternalService apiCallRecordInternalService;

    public UserController(UserService userService, ApiCallRecordInternalService apiCallRecordInternalService) {
        this.userService = userService;
        this.apiCallRecordInternalService = apiCallRecordInternalService;
    }

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody Map<String, String> reqBody) {
        String email = reqBody.get("email");
        String selectedCountry = reqBody.get("selectedCountry");
        String selectedCategory = reqBody.get("selectedCategory");
        Map<String, String> response = new HashMap<>();
        userService.addUser(email, selectedCountry, selectedCategory);
        User user = userService.findByEmail(email);
        response.put("message", "Registration successful, Proceed to login");
        apiCallRecordInternalService.recordApiCall("/register", user);
        return response;
    }

    @GetMapping("/login")
    public Map<String, String> login(@RequestParam String email) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Login successful");
        response.put("token", userService.loginUser(email));
        User user = userService.findByEmail(email);
        apiCallRecordInternalService.recordApiCall("/login", user);
        return response;
    }

    @GetMapping("/subscribe")
    public Map<String, String> subscribe(@RequestParam String token) {
        return userService.subscribeToEmail(token);
    }

    @GetMapping("/unsubscribe")
    public Map<String, String> unsubscribe(@RequestParam String token) {
        return userService.unsubscribeToEmail(token);
    }

    @GetMapping("/verify")
    public Map<String, String> verify(@RequestParam String token) {
        return userService.verifyEmail(token);
    }

    @PostMapping("/update-provider")
    public Map<String, String> updateProvider(@RequestParam String token, @RequestParam String newsProvider) {
        Map<String, String> response = new HashMap<>();
        userService.updateProvider(token, newsProvider);
        response.put("message", "Provider updated successfully");
        User user = userService.getUserByToken(token);
        apiCallRecordInternalService.recordApiCall("/update-provider", user);
        return response;
    }
    @GetMapping("/get-profile")
    public Map<String, String> getProfile(@RequestParam String token) {
        User user = userService.getUserByToken(token);
        apiCallRecordInternalService.recordApiCall("/getProfile", user);
        return userService.getProfile(token);
    }
}
