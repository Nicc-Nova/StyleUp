package com.StyleUp.backend.services;

import com.StyleUp.backend.models.User;
import com.StyleUp.backend.repositories.DecorationRepository;
import com.StyleUp.backend.repositories.RoomRepository;
import com.StyleUp.backend.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTests {

    // Mock dependencies to isolate AuthService logic
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private DecorationRepository decorationRepository;

    @Mock
    private JWTService jwtService;

    @Mock
    private AuthenticationManager authManager;

    // Inject mocks into the AuthService we want to test
    @InjectMocks
    private AuthService authService;

    //AUTH SERVICE TESTS

    @Test
    void testRegisterUser_newEmail() {
        /**
         * Test Case: Registering a new user with a unique email.
         * Scenario: The email does not already exist in the database.
         * Behavior Expected:
         * - A new User object is created.
         * - The UserRepository saves the user and returns it.
         */

        // Arrange: Define user details and mock repository behavior
        String fname = "John";
        String lname = "Doe";
        String email = "john.doe@example.com";
        String password = "securepassword";
        User newUser = new User(fname, lname, email, password, new ArrayList<>(), new ArrayList<>());

        // Mock findByEmail to simulate that the email does not exist
        when(userRepository.findByEmail(email)).thenReturn(null);

        // Mock save method to simulate saving the user
        when(userRepository.save(Mockito.any(User.class))).thenReturn(newUser);

        // Call the method to test
        User registeredUser = authService.registerUser(fname, lname, email, password);

        // Verify the returned user and interactions
        assertNotNull(registeredUser); // Ensure a user is returned
        assertEquals(fname, registeredUser.getFname()); // Verify first name matches
        assertEquals(email, registeredUser.getEmail()); // Verify email matches
        verify(userRepository).save(Mockito.any(User.class)); // Ensure save was called
    }

    @Test
    void testRegisterUser_emailAlreadyExists() {
        /**
         * Test Case: Registering a user with an already existing email.
         * Scenario: The email is found in the database.
         * Behavior Expected:
         * - The method throws a RuntimeException.
         */

        // Mock behavior to simulate an existing user with the email
        String email = "existing@example.com";
        when(userRepository.findByEmail(email)).thenReturn(new User());

        // Verify exception is thrown and has the correct message
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.registerUser("Jane", "Doe", email, "password123"));
        assertEquals("User already exists with this email", exception.getMessage());
    }

    @Test
    void testVerifyUser_success() {
        /**
         * Test Case: Verifying a user's login credentials.
         * Scenario: The email exists, and the password matches.
         * Behavior Expected:
         * - The AuthenticationManager authenticates successfully.
         * - A JWT token is generated and returned.
         */

        //Mock user details and repository behavior
        String email = "john.doe@example.com";
        String password = "securepassword";
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);

        User user = new User("John", "Doe", email, encodedPassword, new ArrayList<>(), new ArrayList<>());

        // Mock repository to simulate finding the user
        when(userRepository.findByEmail(email)).thenReturn(user);

        // Mock authentication to simulate successful authentication
        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(authManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);

        // Mock JWT generation
        when(jwtService.generateToken(email)).thenReturn("mocked-jwt-token");

        // Call the method to test
        String token = authService.verifyUser(user);

        // Verify the token and interactions
        assertNotNull(token); // Ensure a token is returned
        assertEquals("mocked-jwt-token", token); // Verify token matches expected value
        verify(jwtService).generateToken(email); // Ensure JWT generation was called
    }

    @Test
    void testVerifyUser_userNotFound() {
        /**
         * Test Case: Verifying a user that does not exist.
         * Scenario: The email does not match any records in the database.
         * Behavior Expected:
         * - The method throws a ResponseStatusException with a 404 status.
         */

        // Define user details and mock repository behavior
        String email = "nonexistent@example.com";
        User user = new User("Nonexistent", "User", email, "password", new ArrayList<>(), new ArrayList<>());

        // Mock repository to simulate user not found
        when(userRepository.findByEmail(email)).thenReturn(null);

        // Verify exception is thrown and has the correct status
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> {
                    authService.verifyUser(user);
                });
        // Verify the exception details
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("User not found", exception.getReason());
    }

    @Test
    void testVerifyUser_incorrectPassword() {
        /**
         * Test Case: Verifying a user with incorrect credentials.
         * Scenario: The email exists, but the password is incorrect.
         * Behavior Expected:
         * - The AuthenticationManager throws a BadCredentialsException.
         * - The method throws a ResponseStatusException with a 401 status.
         */

        // Mock user details and repository behavior
        String email = "john.doe@example.com";
        String password = "wrongpassword";
        User user = new User("John", "Doe", email, password, new ArrayList<>(), new ArrayList<>());

        // Mock repository to simulate finding the user
        when(userRepository.findByEmail(email)).thenReturn(user);

        // Mock authentication to throw BadCredentialsException
        when(authManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // Verify exception is thrown and has the correct status
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> authService.verifyUser(user));
        assertEquals("401 UNAUTHORIZED \"Incorrect password.\"", exception.getMessage());
    }

    @Test
    void testVerifyUser_unexpectedError() {
        // Given
        String email = "john.doe@example.com";
        String password = "securepassword";
        User user = new User("John", "Doe", email, password, new ArrayList<>(), new ArrayList<>());

        // Mock repository to simulate finding the user
        User foundUser = new User("John", "Doe", email, "securepassword", new ArrayList<>(), new ArrayList<>());
        when(userRepository.findByEmail(email)).thenReturn(foundUser);

        // Mock authentication to simulate an unexpected exception (e.g., RuntimeException)
        when(authManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        // Call the method to test and assert the exception
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            authService.verifyUser(user);
        });

        // Verify the exception details
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("An unexpected error occurred", exception.getReason());
    }

    /*@Test
    void verifyUser_ShouldThrowUnauthorized_WhenAuthenticationFails() {
        // Given
        String email = "john.doe@example.com";
        String password = "securepassword";
        User user = new User("John", "Doe", email, password, new ArrayList<>());

        // Mock repository to simulate finding the user
        User foundUser = new User("John", "Doe", email, "securepassword", new ArrayList<>());
        when(userRepository.findByEmail(email)).thenReturn(foundUser);

        // Mock authentication to simulate failed authentication (isAuthenticated = false)
        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.isAuthenticated()).thenReturn(false);
        when(authManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);

        // Call the method to test and assert the exception
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            authService.verifyUser(user);
        });

        // Verify the exception details
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Incorrect password", exception.getReason());
    }*/


}
