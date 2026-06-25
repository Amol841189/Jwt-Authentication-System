package com.authentication.jwt.service;


import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import com.authentication.jwt.entity.User;
import com.authentication.jwt.repository.UserRepository;
import java.util.Optional;

@Service 
public class AuthService{

    @Autowired
    private UserRepository userRepository;
    
    public boolean validateUser( String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);

        return user.isPresent() && user.get().getPassword().equals(password);
    }
    
    
    
    
    
    
    // public boolean login( String username, String password)
    // {
    //     return username.equals("admin") && password.equals("1234");
    // }

    public String logout(){
        return "Session Destroyed Successfully";
    }
}