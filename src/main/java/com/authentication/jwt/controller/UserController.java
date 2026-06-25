package com.authentication.jwt.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authentication.jwt.util.JwtUtil;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private JwtUtil jwtUtil;


    public UserController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/profile")
    public ResponseEntity<Map<String, String>> profile(@RequestHeader("Authorization") String authHeader) {
        Map<String, String> response = new HashMap<>();
        
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.put("error", "Token missing");
            return ResponseEntity.badRequest().body(response);
        }

        String token = authHeader.substring(7);
        
        boolean isValid = jwtUtil.validateToken(token);

        if(!isValid) {
            response.put("error", "Invalid or Expired Token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        String username = jwtUtil.extractUsername(token);
        response.put("message", "Welcome " + username);
        response.put("username", username);
        return ResponseEntity.ok(response);
    }
    
}
