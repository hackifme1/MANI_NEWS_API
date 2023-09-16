package com.maniproject.newswave.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    private UUID id;
    private String email;
    private String username;
    private String selectedCountry;
    private String selectedCategory;

    @Column(name = "is_subscribed", nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private boolean isSubscribed;

    @Column(name = "news_provider",columnDefinition = "VARCHAR(255) DEFAULT 'newsapi'")
    private String newsProvider;

    @ManyToMany
    @JoinTable(
            name = "user_news_sources",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "news_source_id")
    )    private List<NewsSource> newsSourceList;


    @Column(name = "is_verified", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isVerified;

    public User(UUID uuid, String email, String selectedCountry, String selectedCategory) {
        this.id = uuid;
        this.email = email;
        this.selectedCountry = selectedCountry;
        this.selectedCategory = selectedCategory;
        this.newsProvider= "newsapi";
        this.isSubscribed = true;
    }

    public User() {
    }

    public void setSubscribed(boolean subscribed) {
        isSubscribed = subscribed;
    }

    public boolean getIsSubscribed() {
        return this.isSubscribed;
    }
    public boolean getisVerified() {
        return this.isVerified;
    }
    public void setNewsProvider(String newsProvider) {
        this.newsProvider = newsProvider;
    }
}
