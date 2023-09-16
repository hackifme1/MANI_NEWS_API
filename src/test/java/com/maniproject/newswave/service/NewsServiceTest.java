package com.maniproject.newswave.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.maniproject.newswave.utils.RestTemplateClient;
import com.maniproject.newswave.entity.Headline;
import com.maniproject.newswave.entity.NewsProvider;
import com.maniproject.newswave.entity.User;
import com.maniproject.newswave.factory.NewsProviderFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NewsServiceTest {

    @Mock
    private NewsProviderFactory newsProviderFactory;

    @Mock
    private RestTemplateClient restTemplateClient;

    @InjectMocks
    private NewsService newsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetHeadlinesWithPagination() throws JsonProcessingException {
        String country = "us";
        String category = "technology";
        int pageSize = 10;
        int pageNum = 1;

        User user = new User();
        user.setNewsProvider("newsProvider1");
        user.setSelectedCountry("us");
        user.setSelectedCategory("technology");

        NewsProvider newsProvider = mock(NewsProvider.class);
        when(newsProviderFactory.getNewsProvider(anyString())).thenReturn(newsProvider);
        when(newsProvider.getTopHeadlinesEndpoint(anyString(), anyString(), anyInt())).thenReturn("sampleUrl");

        List<Headline> headlines = List.of(); // Create a list of Headline objects

        List<Headline> result = newsService.getHeadlinesWithPagination(country, category, pageSize, pageNum, user);

        assertEquals(headlines, result);
        verify(newsProviderFactory).getNewsProvider("newsProvider1");
        verify(newsProvider).getTopHeadlinesEndpoint("us", "technology", 10);
    }

    @Test
    public void testGetLimitedHeadlines() throws JsonProcessingException {
        User user = new User();
        user.setNewsProvider("newsProvider1");
        user.setSelectedCountry("us");
        user.setSelectedCategory("technology");

        NewsProvider newsProvider = mock(NewsProvider.class);
        when(newsProviderFactory.getNewsProvider(anyString())).thenReturn(newsProvider);
        when(newsProvider.getTopHeadlinesEndpoint(anyString(), anyString(), anyInt())).thenReturn("sampleUrl");

        List<Headline> headlines = newsService.getLimitedHeadlines("us", "technology", user);
        assert (headlines.size() <= 10);
    }


    @Test
    public void testGetHeadlines() {

        List<Headline> headlines = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            headlines.add(new Headline());
        }

        List<Headline> result = newsService.getHeadlines(headlines);

        assertEquals(3, result.size());
    }

    @Test
    public void testGetHeadlinesForKeepReading() {

        List<Headline> headlines = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            headlines.add(new Headline());
        }

        List<Headline> result = newsService.getHeadlinesForKeepReading(headlines);

        assertEquals(5, result.size());
    }




}