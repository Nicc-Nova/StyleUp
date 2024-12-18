package com.StyleUp.backend.controllers;

import com.StyleUp.backend.models.User;
import com.StyleUp.backend.services.AuthService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

@SpringBootTest //enables integration testing of the Spring application
@AutoConfigureMockMvc //Configures the MockMvc bean for testing REST APIs without starting the actual server.
public class AuthControllerTests {

    //used to simulate HTTP requests and validate responses
    //It avoids starting a full server but still processes requests through the application context.
    @Autowired
    private MockMvc mockMvc;

    //Replaces the actual AuthService bean with a mock during the test.
    @MockBean
    private AuthService authService;

    //used to serialize Java objects (e.g., User) into JSON for the test request payload.
    @Autowired
    private ObjectMapper objectMapper;

    //AUTH CONTROLLER TESTS

    //Tests the AuthController /api/register endpoint for a successful user registration.
    @Test
    public void testRegisterUser_Success() throws Exception {
        // Create a user object to be sent in the request
        User user = new User("John", "Doe", "john.doe@example.com", "password123", new ArrayList<>(), new ArrayList<>());

        //A POST request is made to /api/register with the User serialized as JSON.
        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk()) //Checks if the response status is 200 OK.
                .andExpect(content().string("User registered successfully")); //Verifies the response message: "User registered successfully".
    }

    //Tests the AuthController /api/register endpoint for failed registration due to a backend error.
    @Test
    public void testRegisterUser_Failure() throws Exception {
        // The authService.registerUser method is mocked to throw a RuntimeException when invoked.
        //We do this because we assume AuthService works and is separately tested, isolating controller
        Mockito.doThrow(new RuntimeException("Registration error")).when(authService)
                .registerUser(any(), any(), any(), any());

        // Create a user object to be sent in the request
        User user = new User("John", "Doe", "john.doe@example.com", "password123", new ArrayList<>(), new ArrayList<>());

        // A POST request is made with the same payload as the success test.
        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isInternalServerError()) //Checks if the response status is 500 Internal Server Error.
                .andExpect(content().string("Registration failed: Registration error")); //Verifies the error message: "Registration failed: Registration error".
    }

    // Tests the AuthController /api/login endpoint for successful user login.
    @Test
    public void testLoginUser_Success() throws Exception {
        // The authService.verifyUser method is mocked to return "Login successful".
        when(authService.verifyUser(any())).thenReturn("Login successful");

        // Create a user object to be sent in the request
        User user = new User("John", "Doe", "john.doe@example.com", "password123", new ArrayList<>(), new ArrayList<>());

        // A POST request is made to /api/login with valid credentials in the payload.
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk()) //Checks if the response status is 200 OK.
                .andExpect(content().string("Login successful")); //Verifies the success message: "Login successful".
    }

    // Tests the AuthController /api/login endpoint for login failure due to invalid credentials.
    @Test
    public void testLoginUser_Failure() throws Exception {
        // The authService.verifyUser method is mocked to throw a RuntimeException with the message "Invalid credentials".
        when(authService.verifyUser(any())).thenThrow(new RuntimeException("Invalid credentials"));

        // Create a user object to be sent in the request
        User user = new User("John", "Doe", "john.doe@example.com", "password123", new ArrayList<>(), new ArrayList<>());

        // A POST request is made to /api/login with invalid credentials in the payload.
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isUnauthorized()) //Checks if the response status is 401 Unauthorized.
                .andExpect(content().string("Invalid credentials")); //Verifies the error message: "Invalid credentials".
    }

}
