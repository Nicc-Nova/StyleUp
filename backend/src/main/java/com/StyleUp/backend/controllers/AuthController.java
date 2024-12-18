package com.StyleUp.backend.controllers;

import com.StyleUp.backend.models.User;
import com.StyleUp.backend.models.UserPrincipal;
import com.StyleUp.backend.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController //makes it capable of handling HTTP requests and send responses in JSON format.
@RequestMapping("/api") //the login endpoint will be accessible at /api/login
public class AuthController {

    private final AuthService authService;

    //Constructor injection
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    //register endpoint
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        try {
            authService.registerUser(user.getFname(), user.getLname(), user.getEmail(), user.getPassword());
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Registration failed: " + e.getMessage());
        }
    }

    //login endpoint
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User user) {
        try {
            String response = authService.verifyUser(user);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}