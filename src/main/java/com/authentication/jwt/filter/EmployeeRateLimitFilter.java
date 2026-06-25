package com.authentication.jwt.filter;

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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EmployeeRateLimitFilter implements Filter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private Bucket getBucket(String ip) {
        return buckets.computeIfAbsent(ip, k ->
            Bucket.builder()
                .addLimit(
                    Bandwidth.builder()
                        .capacity(20)
                        .refillIntervally(20, Duration.ofMinutes(1))
                        .build()
                )
                .build()
        );
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // Only Endpoint : /api/employees
        if (!req.getRequestURI().startsWith("/api/employees")) {
            chain.doFilter(request, response);
            return;
        }

        String ip = req.getRemoteAddr();
        Bucket bucket = getBucket(ip);

        System.out.println("URI: " + req.getRequestURI());
        System.out.println("IP: " + ip);
        System.out.println("Available Tokens Before: "+ bucket.getAvailableTokens());

        if (bucket.tryConsume(1)) {
            System.out.println("Available Tokens After: " + bucket.getAvailableTokens());

            chain.doFilter(request, response);

        } else {
            System.out.println("RATE LIMIT EXCEEDED");

            res.setStatus(429);
            res.setContentType("application/json");

            res.getWriter().write(""" 
                {
                    "success": false,
                    "message": "Too many employee requests. Please try again later."
                }
            """);
        }
    }
}