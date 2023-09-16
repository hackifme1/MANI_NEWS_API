package com.maniproject.newswave.service;

import com.maniproject.newswave.entity.NewsProvider;
import com.maniproject.newswave.entity.User;
import com.maniproject.newswave.exception.InvalidNewsProvider;
import com.maniproject.newswave.factory.NewsProviderFactory;
import com.maniproject.newswave.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateAllUsersProvidersServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private NewsProviderFactory newsProviderFactory;

    @InjectMocks
    private UpdateAllUsersProvidersService updateAllUsersProvidersService;

    @Test
    public void testUpdateAllUsersProviders_ValidProvider() {
        // Mock NewsProviderFactory
        NewsProvider newsProvider = mock(NewsProvider.class);
        when(newsProviderFactory.getNewsProvider(anyString())).thenReturn(newsProvider);

        // Mock UserRepository
        User user1 = new User();
        user1.setNewsProvider("oldProvider");
        User user2 = new User();
        user2.setNewsProvider("oldProvider");
        List<User> users = List.of(user1, user2);
        when(userRepository.findAll()).thenReturn(users);

        // Test
        Map<String, String> response = updateAllUsersProvidersService.updateAllUsersProviders("newProvider");

        verify(userRepository, times(2)).save(any(User.class));

        // Verify response
        assertEquals("Updated all users with provider: newProvider", response.get("message"));
    }

    @Test
    public void testUpdateAllUsersProviders_InvalidProvider() {
        // Mock NewsProviderFactory
        when(newsProviderFactory.getNewsProvider(anyString())).thenReturn(null);

        assertThrows(InvalidNewsProvider.class,
                () -> updateAllUsersProvidersService.updateAllUsersProviders("invalidProvider"));
    }

}
