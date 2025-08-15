package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Model.MyAppUser;
import com.example.demo.Model.MyAppUserRepository;
import com.example.demo.service.EmailService;
import com.example.demo.utils.JwtTokenUtil;

@RestController
public class RegistrationController {

    @Autowired
    private MyAppUserRepository myAppUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    // You were missing this Autowired annotation
    @Autowired
    private JwtTokenUtil jwtUtil;


    @PostMapping(value = "/req/signup", consumes = "application/json")
    public ResponseEntity<String> createUser(@RequestBody MyAppUser user){

        MyAppUser existingAppUser = myAppUserRepository.findByEmail(user.getEmail());

        if(existingAppUser != null){
            if(existingAppUser.isVerified()){
                return new ResponseEntity<>("User already exists and is verified.", HttpStatus.BAD_REQUEST);
            } else {
                // Use the autowired jwtUtil bean to call the method
                String verificationToken = jwtUtil.generateToken(existingAppUser.getEmail());
                // Correct the typo here
                existingAppUser.setVerificationToken(verificationToken);
                myAppUserRepository.save(existingAppUser);

                emailService.sendVerificationEmail(existingAppUser.getEmail(), verificationToken);
                return new ResponseEntity<>("Verification email resent. Please check your inbox.", HttpStatus.OK);
            }
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Use the autowired jwtUtil bean here as well
        String verificationToken = jwtUtil.generateToken(user.getEmail());
        // Correct the typo here
        user.setVerificationToken(verificationToken);
        myAppUserRepository.save(user);

        emailService.sendVerificationEmail(user.getEmail(), verificationToken);

        return new ResponseEntity<>("Registration successful! Please verify your email.", HttpStatus.OK);
    }
}