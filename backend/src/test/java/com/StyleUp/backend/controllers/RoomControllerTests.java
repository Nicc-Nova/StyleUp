package com.StyleUp.backend.controllers;

import com.StyleUp.backend.models.Decoration;
import com.StyleUp.backend.models.Room;
import com.StyleUp.backend.models.UserPrincipal;
import com.StyleUp.backend.repositories.CollaborationRepository;
import com.StyleUp.backend.services.AuthService;
import com.StyleUp.backend.services.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import com.StyleUp.backend.repositories.DecorationRepository;
import com.StyleUp.backend.repositories.RoomRepository;
import com.StyleUp.backend.repositories.UserRepository;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest //enables integration testing of the Spring application
@AutoConfigureMockMvc //Configures the MockMvc bean for testing REST APIs without starting the actual server.
public class RoomControllerTests {
    //used to simulate HTTP requests and validate responses
    //It avoids starting a full server but still processes requests through the application context.
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private RoomRepository roomRepository;

    //Replaces actual room service with mock room service for testing
    @MockBean
    private RoomService roomService;

    //used to serialize Java objects (e.g., User) into JSON for the test request payload.
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AuthService authService;

    //ROOM CONTROLLER TESTS
    @Test
    void testCreateRoom_Success() throws Exception {
        /**
         * TestCase: Test API Call to Room Control Service
         * Scenario: User is logged in
         * Behavior Expected:
         * - API Call succeeds
         * -
         */
        // Arrange: Define room details and mock repository behavior
        String roomName = "Bedroom";
        Long userId = 1L;  // Mock user ID
        List<Decoration> decorations = new ArrayList<>();
        Room newRoom = new Room(roomName, userId, decorations);

        //A POST request is made to /api/register with the User serialized as JSON.
        mockMvc.perform(post("/api/room/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRoom)))
                .andExpect(status().isOk()) //Checks if the response status is 200 OK.
                .andExpect(content().string("Room Created successfully")); //Verifies the response message: "Room registered successfully".

    }

    @Test
    void testDeleteRoom_Success() throws Exception {
        /**
         * TestCase: Test API Call to Room Control Service
         * Scenario: User is logged in
         * Behavior Expected:
         * - API Call succeeds
         * -
         */
        // Arrange: Define room details and mock repository behavior
        String roomName = "Bedroom";
        Long userId = 1L;  // Mock user ID
        List<Decoration> decorations = new ArrayList<>();
        Room deleteRoom = new Room(roomName, userId, decorations);

        //A POST request is made to /api/register with the User serialized as JSON.
        mockMvc.perform(post("/api/room/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deleteRoom)))
                .andExpect(status().isOk()) //Checks if the response status is 200 OK.
                .andExpect(content().string("Room deleted successfully")); //Verifies the response message: "Room deleted successfully".

    }

        @Test
        void addItemToRoom_Success() throws Exception {
            /**
             * TestCase: Test API Call to Room Control Service
             * Scenario: User is logged in
             * Behavior Expected:
             * - API Call succeeds
             */
            // Arrange: Define room details and mock repository behavior
            String roomName = "Bedroom";
            Long userId = 1L;  // Mock user ID
            Long roomId = 1L; // Mock room id
            List<Decoration> decorations = new ArrayList<>();
            Room newRoom = new Room(roomName, userId, decorations);

            Decoration newDecoration = new Decoration();
            newDecoration.setDescription("description");
            newDecoration.setTitle("Test Decoration");

            //A POST request is made to /api/register with the User serialized as JSON.
            mockMvc.perform(post("/api/room/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(newRoom))
                            .content(objectMapper.writeValueAsString(newDecoration)) //Check that Posting a decoration works
                            .param("roomId", roomId.toString())) //Check Posting the correct roomid works
                    .andExpect(status().isOk()) //Checks if the response status is 200 OK.
                    .andExpect(content().string("Item added to room successfully")); //Verifies the response message: "Item added to room successfully".

        }
}
