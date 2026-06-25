package com.authentication.jwt.filter;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LoginRateLimitFilterTest {

    private LoginRateLimitFilter filter;
    private FilterChain filterChain;

    @BeforeEach
    void setup() {
        filter = new LoginRateLimitFilter();
        filterChain = mock(FilterChain.class);
    }

    @Test
    void shouldAllowFirstThreeRequests() throws Exception {

        for (int i = 0; i < 3; i++) {

            MockHttpServletRequest request =
                    new MockHttpServletRequest("POST", "/api/login");

            request.setRemoteAddr("127.0.0.1");

            MockHttpServletResponse response =
                    new MockHttpServletResponse();

            filter.doFilter(request, response, filterChain);

            assertNotEquals(429, response.getStatus());
        }
    }

    @Test
    void shouldBlockFourthRequest() throws Exception {

        // Consume all 3 tokens
        for (int i = 0; i < 3; i++) {

            MockHttpServletRequest request =
                    new MockHttpServletRequest("POST", "/api/login");

            request.setRemoteAddr("127.0.0.1");

            filter.doFilter(
                    request,
                    new MockHttpServletResponse(),
                    filterChain);
        }

        // Fourth request should fail
        MockHttpServletRequest request =
                new MockHttpServletRequest("POST", "/api/login");

        request.setRemoteAddr("127.0.0.1");

        MockHttpServletResponse response =
                new MockHttpServletResponse();

        filter.doFilter(request, response, filterChain);

        assertEquals(429, response.getStatus());
    }

    @Test
    void shouldAllowDifferentIps() throws Exception {

        System.out.println("Ip 1 :--->");
        MockHttpServletRequest request1 =
                new MockHttpServletRequest("POST", "/api/login");

        request1.setRemoteAddr("127.0.0.1");

        filter.doFilter(
                request1,
                new MockHttpServletResponse(),
                filterChain);

        System.out.println("Ip 2 :--->");
        MockHttpServletRequest request2 =
                new MockHttpServletRequest("POST", "/api/login");

        request2.setRemoteAddr("192.168.1.100");
        
        MockHttpServletResponse response =
                new MockHttpServletResponse();

        filter.doFilter(request2, response, filterChain);

        assertNotEquals(429, response.getStatus());
    }

    @Test
    void shouldIgnoreNonLoginEndpoints() throws Exception {

        MockHttpServletRequest request =
                new MockHttpServletRequest("GET", "/api/users");

        MockHttpServletResponse response =
                new MockHttpServletResponse();

        filter.doFilter(request, response, filterChain);

        verify(filterChain, times(1))
                .doFilter(any(), any());
    }

    @Test
    void shouldIgnoreGetLoginRequest() throws Exception {

        MockHttpServletRequest request =
                new MockHttpServletRequest("GET", "/api/login");

        MockHttpServletResponse response =
                new MockHttpServletResponse();

        filter.doFilter(request, response, filterChain);

        verify(filterChain, times(1))
                .doFilter(any(), any());
    }
}