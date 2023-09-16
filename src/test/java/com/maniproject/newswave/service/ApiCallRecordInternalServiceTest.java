package com.maniproject.newswave.service;

import static org.junit.jupiter.api.Assertions.*;

import com.maniproject.newswave.entity.ApiCallRecordInternal;
import com.maniproject.newswave.entity.User;
import com.maniproject.newswave.repository.ApiCallRecordCustomInternalRepository;
import com.maniproject.newswave.repository.ApiCallRecordInternalRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.Mockito.*;

class ApiCallRecordInternalServiceTest {

    @Mock
    private ApiCallRecordInternalRepository apiCallRecordInternalRepository;

    @Mock
    private ApiCallRecordCustomInternalRepository apiCallRecordCustomInternalRepository;

    @InjectMocks
    private ApiCallRecordInternalService apiCallRecordInternalService;

    public ApiCallRecordInternalServiceTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testRecordApiCall() {
        User user = new User();
        user.setEmail("user@example.com");
        String endpointName = "example-endpoint";

        apiCallRecordInternalService.recordApiCall(endpointName, user);

        verify(apiCallRecordInternalRepository, times(1)).save(any(ApiCallRecordInternal.class));
    }

    @Test
    void testGetUsersWithApiCallCount() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sevenDaysAgo = now.minusDays(7);

        when(apiCallRecordCustomInternalRepository.getUsersWithApiCallCount(any(), any()))
                .thenReturn(List.<Object[]>of(new Object[] {"user@example.com", 5L}));

        List<Object[]> result = apiCallRecordInternalService.getUsersWithApiCallCount();

        verify(apiCallRecordCustomInternalRepository, times(1)).getUsersWithApiCallCount(any(), any());
        assertEquals(1, result.size());
        assertArrayEquals(new Object[] {"user@example.com", 5L}, result.get(0));
    }
}