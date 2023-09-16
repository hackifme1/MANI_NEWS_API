package com.maniproject.newswave.service;

import com.maniproject.newswave.exception.*;
import com.maniproject.newswave.repository.UserRepository;
import com.maniproject.newswave.entity.User;
import com.maniproject.newswave.utils.EmailValidator;
import com.maniproject.newswave.utils.JwtHandler;
import com.maniproject.newswave.utils.category.Category;
import com.maniproject.newswave.utils.category.CategoryServiceProvider1;
import com.maniproject.newswave.utils.category.CategoryServiceProvider2;
import com.maniproject.newswave.utils.country.Country;
import com.maniproject.newswave.utils.country.CountryServiceProvider1;
import com.maniproject.newswave.utils.country.CountryServiceProvider2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class UserService {
    private final CountryServiceProvider1 countryServiceProvider1;
    private final CategoryServiceProvider1 categoryServiceProvider1;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final JwtHandler jwtHandler;

    public UserService(CountryServiceProvider1 countryServiceProvider1, CategoryServiceProvider1 categoryServiceProvider1, UserRepository userRepository, EmailService emailService, JwtHandler jwtHandler) {
        this.countryServiceProvider1 = countryServiceProvider1;
        this.categoryServiceProvider1 = categoryServiceProvider1;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.jwtHandler = jwtHandler;
    }

    //todo talk in terms of response code
    @Transactional
    public void addUser(String email, String selectedCountry, String selectedCategory) throws IllegalArgumentException {
        if (!EmailValidator.isValid(email)) {
            throw new EmailIsInvalidException("User tried to register with invalid email: " + email);
        }
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistException("Cannot register. User already exists with email: " + email);
        }

        if (!countryServiceProvider1.getCountries().contains(selectedCountry)) {
            throw new CountryIsInvalidException("User tried to register with Invalid country: " + selectedCountry);
        }
        if (!categoryServiceProvider1.getCategories().contains(selectedCategory)) {
            throw new CategoryIsInvalidException("User tried to register with Invalid category: " + selectedCategory);
        }

        UUID uuid = UUID.randomUUID();
        User user = new User(uuid, email, selectedCountry, selectedCategory);
        userRepository.save(user);
        emailService.sendWelcomeEmail(email);

    }

    public String loginUser(String email) {
        if (!EmailValidator.isValid(email)) {
            throw new IllegalArgumentException("User tried to login with invalid email: " + email);
        }
        if (!userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Cannot login. User does not exist with email: " + email);
        }
        User user = userRepository.findByEmail(email);
        if (!user.getisVerified()) {
            emailService.sendVerificationMail(user);
            throw new IllegalArgumentException("Cannot login. User not verified with email: " + email);
        }
        return jwtHandler.generateVerificationTokenLong(email);
    }


    public Map<String, String> subscribeToEmail(String token) {
        String email=jwtHandler.parseJwt(token);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new EmailRecordNotFoundException("User email record not found with email: " + email);
        }
        user.setSubscribed(true);
        userRepository.save(user);
        return Map.of("message", "User subscribed to email updates");
    }

    public Map<String, String> unsubscribeToEmail(String token) {
        String email=jwtHandler.parseJwt(token);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new EmailRecordNotFoundException("User email record not found with email: " + email);
        }
        user.setSubscribed(false);
        userRepository.save(user);
        return Map.of("message", "User unsubscribed from email updates");
    }

    public Map<String,String> verifyEmail(String token){
        boolean isExpired=jwtHandler.isTokenExpired(token);
        if (isExpired){
            throw new IllegalArgumentException("Link expired, cannot verify user, please try again");
        }
        String email=jwtHandler.parseJwt(token);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User email record not found with email: " + email);
        }
        user.setVerified(true);
        userRepository.save(user);
        return Map.of("message", "User verified with email: "+email);
    }

    public Map<String,String> getProfile(String token){
        String email=jwtHandler.parseJwt(token);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new EmailRecordNotFoundException("User email record not found with email: " + email);
        }
        return Map.of("email",user.getEmail(),"selectedCountry",user.getSelectedCountry(),"selectedCategory",user.getSelectedCategory());
    }

    public User findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new EmailRecordNotFoundException("User email record not found for email: " + email);
        }
        return user;
    }

    public void updateProvider(String token, String newsProvider) {
        checkValidNewsProvider(newsProvider);
        String email = jwtHandler.parseJwt(token);
        User user = userRepository.findByEmail(email);
        checkIfCountryCodeIsValid(user, newsProvider);
        checkIfCategoryCodeIsValid(user, newsProvider);
        user.setNewsProvider(newsProvider);
        userRepository.save(user);
        log.info(String.format("User with email: %s updated news provider.", email));
    }

    private void checkValidNewsProvider(String newsProvider) {
        if (!newsProvider.equals("newsapi") && !newsProvider.equals("newsio")) {
            throw new InvalidNewsProvider("Invalid news provider");
        }
    }

    private void checkIfCategoryCodeIsValid(User user, String newsProvider) {
        Category category = new CategoryServiceProvider1();
        if (newsProvider.equals("newsapi")) {
            category = new CategoryServiceProvider1();
        }
        else if (newsProvider.equals("newsio")){
            category = new CategoryServiceProvider2();
        }

        List<String> categories = category.getCategories();
        if (!categories.contains(user.getSelectedCategory())) {
            throw new InvalidCategoryCode("Invalid category code");
        }
    }

    private void checkIfCountryCodeIsValid(User user, String newsProvider) {
        Country country = new CountryServiceProvider1();
        if (newsProvider.equals("newsapi")) {
            country = new CountryServiceProvider1();
        }
        else if (newsProvider.equals("newsio")){
            country = new CountryServiceProvider2();
        }

        List<String> countries = country.getCountries();
        if (!countries.contains(user.getSelectedCountry())) {
            throw new InvalidCountryCode("Invalid country code");
        }
    }

    public User getUserByToken(String token) {
        String email = jwtHandler.parseJwt(token);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new EmailRecordNotFoundException("User email record not found for email: " + email);
        }
        return user;
    }
}
