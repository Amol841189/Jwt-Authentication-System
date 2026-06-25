package com.authentication.jwt.controller;

import com.authentication.jwt.dto.LoginRequest;
import com.authentication.jwt.dto.LoginResponse;
import com.authentication.jwt.dto.RegisterRequest;
import com.authentication.jwt.entity.User;
import com.authentication.jwt.repository.UserRepository;
import com.authentication.jwt.service.AuthService;
import com.authentication.jwt.util.JwtUtil;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthController(AuthService authService, 
                       UserRepository userRepository,
                    JwtUtil jwtUtil){
        this.authService = authService;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){

        // Integrate With Mysql
        boolean valid = authService.validateUser(request.getUsername(), request.getPassword());

        if(!valid){
          return ResponseEntity.status(401).body(new LoginResponse("failed", null));
        }

        String token = jwtUtil.generateToken(request.getUsername());

        return ResponseEntity.ok(new LoginResponse("success", token));
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterRequest request) {
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        
        userRepository.save(user);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "User Registered");
        response.put("username", request.getUsername());
        response.put("status", "201");
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }



// [ ------------------------------------------------------------------------------------------------------
//     // Simple Login
//     @PostMapping("/login")
//     public Map<String, Object> login(@RequestBody LoginRequest request) {

//         boolean success = authService.login(request.getUsername(), request.getPassword());
        
//         LocalDateTime datetime = LocalDateTime.now();

        
//         Map<String, Object> response = new HashMap<>();

//         if (success) {
//             response.put("status", "success");
//             response.put("message", "Login successful");
//             response.put("DateTime", datetime);
//         } else {
//             response.put("status", "failed");
//             response.put("message", "Invalid credentials");
//         }

//         return response;
//     }
// ]--------------------------------------------------------------------------------------------------------------
    @PostMapping("/logout")
    public Map<String, Object> logout(HttpSession session){
        String message = authService.logout();

        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        
        return response; 
    }
}