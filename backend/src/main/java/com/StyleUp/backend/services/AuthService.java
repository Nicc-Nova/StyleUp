package com.StyleUp.backend.services;

import com.StyleUp.backend.models.Decoration;
import com.StyleUp.backend.models.Room;
import com.StyleUp.backend.repositories.DecorationRepository;
import com.StyleUp.backend.repositories.RoomRepository;
import com.StyleUp.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import com.StyleUp.backend.models.User;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

@Service
public class AuthService  {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final DecorationRepository decorationRepository;
    private final JWTService jwtService;
    private final AuthenticationManager authManager;

    public AuthService(UserRepository userRepository, RoomRepository roomRepository,
                       DecorationRepository decorationRepository, JWTService jwtService,
                       AuthenticationManager authManager) {
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.decorationRepository = decorationRepository;
        this.jwtService = jwtService;
        this.authManager = authManager;
    }

    // Register a new user
    public User registerUser(String fname, String lname, String email, String password) {
        //Check if user already exists
        if (userRepository.findByEmail(email) != null) {
            throw new RuntimeException("User already exists with this email");
        }
        List<Room> rooms= new ArrayList<>();
        List<Decoration> decorations= new ArrayList<>();
        User user = new User(fname, lname, email, password, rooms, decorations);
        return userRepository.save(user);
    }

    // Authenticate user
    public String verifyUser(User user) {
        // Check if the user exists
        User foundUser = userRepository.findByEmail(user.getEmail());
        if (foundUser == null) {
            System.out.println("User not found: " + user.getEmail());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        try {
            Authentication authentication =
                    authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

            if(authentication.isAuthenticated()) {
                System.out.println("User authenticated successfully: " + user.getEmail());
                return jwtService.generateToken(user.getEmail());
            } else {
                System.out.println("Authentication failed for user: " + user.getEmail());
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect password");
            }
        } catch (BadCredentialsException e) {
            System.out.println("Bad credentials for user: " + user.getEmail());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect password.");
        } catch (Exception e) {
            // Catch any other unexpected errors
            System.out.println("Unexpected error occurred for user: " + user.getEmail());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", e);
        }
    }
}
