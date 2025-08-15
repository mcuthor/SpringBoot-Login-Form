package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Model.MyAppUser;
import com.example.demo.Model.MyAppUserRepository;
import com.example.demo.utils.JwtTokenUtil;

@RestController
public class VerificationController {

    @Autowired
    private MyAppUserRepository myAppUserRepository;

    @Autowired
    private JwtTokenUtil jwtUtil;

    @GetMapping("/req/signup/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        String emailString = jwtUtil.extractEmail(token);
        MyAppUser user = myAppUserRepository.findByEmail(emailString);

        // Check if user or token exists
        if (user == null || user.getVerificationToken() == null) { // Corrected typo here
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token is invalid or has expired!");
        }

        // Validate the token
        if (!jwtUtil.validateToken(token) || !user.getVerificationToken().equals(token)) { // Corrected typo here
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token is invalid or has expired!");
        }

        // Verify the user and save
        user.setVerificationToken(null);
        user.setVerified(true);
        myAppUserRepository.save(user);

        return ResponseEntity.ok("Email successfully verified!");
    }
}