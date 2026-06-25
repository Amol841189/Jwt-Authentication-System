package com.authentication.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LoginRateLimitFilter implements Filter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Create Bucket
    private Bucket createLoginBucket() {

        Bandwidth limit = Bandwidth.builder()
                            .capacity(3)
                            .refillIntervally(3, Duration.ofMinutes(1))
                            .build();
                            

        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    private Bucket getLoginBucket(String ip) {
        return buckets.computeIfAbsent(ip, k -> createLoginBucket());
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // Apply only to login endpoint
        if ("/api/login".equals(req.getRequestURI())
                && "POST".equalsIgnoreCase(req.getMethod())) {

            String ip = req.getRemoteAddr();
            Bucket bucket = getLoginBucket(ip);

            System.out.println("IP: " + ip);
            System.out.println("Available Tokens: " + bucket.getAvailableTokens());

            if (!bucket.tryConsume(1)) {

                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("statusCode", 429);
                error.put("message", "Too Many Login Attempts. Please try again later.");

                res.setStatus(429);
                res.setContentType("application/json");
                res.setCharacterEncoding("UTF-8");

                objectMapper.writeValue(res.getWriter(), error);
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
