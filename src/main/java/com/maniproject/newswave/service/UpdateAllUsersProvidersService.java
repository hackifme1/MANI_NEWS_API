package com.maniproject.newswave.service;

import com.maniproject.newswave.exception.InvalidNewsProvider;
import com.maniproject.newswave.repository.UserRepository;
import com.maniproject.newswave.entity.NewsProvider;
import com.maniproject.newswave.entity.User;
import com.maniproject.newswave.factory.NewsProviderFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UpdateAllUsersProvidersService {

    private final UserRepository userRepository;
    private final NewsProviderFactory newsProviderFactory;

    public UpdateAllUsersProvidersService(UserRepository userRepository, NewsProviderFactory newsProviderFactory) {
        this.userRepository = userRepository;
        this.newsProviderFactory = newsProviderFactory;
    }

    public Map<String, String> updateAllUsersProviders(String providerName) {
        log.info("Getting all users from database");
        NewsProvider newsProvider = newsProviderFactory.getNewsProvider(providerName);

        if (newsProvider == null) {
           throw new InvalidNewsProvider("Invalid provider name");
        }

        Map<String, String> response = new HashMap<>();
        List<User> users = userRepository.findAll();

        log.info("Updating all users with provider: {}", providerName);

        users.forEach(user -> {
            user.setNewsProvider(providerName);
            userRepository.save(user);
        });

        response.put("message", "Updated all users with provider: " + providerName);
        log.info("Updated all users with provider: {}", providerName);

        return response;
    }
}
