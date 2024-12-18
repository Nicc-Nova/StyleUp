package com.StyleUp.backend.controllers;

import com.StyleUp.backend.models.*;
import com.StyleUp.backend.repositories.InvitationRepository;
import com.StyleUp.backend.services.AuthService;
import com.StyleUp.backend.services.InvitationService;
import com.StyleUp.backend.services.JWTService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest //enables integration testing of the Spring application
@AutoConfigureMockMvc //Configures the MockMvc bean for testing REST APIs without starting the actual server.
public class InvitationControllerTests {
    //used to simulate HTTP requests and validate responses
    //It avoids starting a full server but still processes requests through the application context.
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private InvitationRepository invitationRepository;

    //Replaces actual room service with mock room service for testing
    @MockBean
    private InvitationService invitationService;

    @MockBean
    private JWTService jwtService;

    //used to serialize Java objects (e.g., User) into JSON for the test request payload.
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AuthService authService;


    //INVITATION CONTROLLER TESTS
    @Test
    void testShare_Success() throws Exception {
        //establish mock parameters
        Long ownerId = 1L;
        String email = "gal@gmail.com";
        Long room_id = 1L;
        String roomName = "Nick's Room";
        String senderName = "nick@gmail.com";
        Invitation newInvitation = new Invitation(ownerId, email, room_id, roomName, senderName);

        when(invitationService.addInvitation(email,  room_id, roomName)).thenReturn(newInvitation);

        mockMvc.perform(post("/api/invitation/share")
                        .header("Authorization", "Bearer mock-jwt-token") // Include mock token
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Invitation(ownerId, email, room_id, roomName, senderName))))
                .andExpect(status().isOk())
                .andExpect(content().string("ROOM SHARED successfully"));
    }

    @Test
    void testCreateDecoration_Failure() throws Exception {
        //establish mock parameters
        Long ownerId = 1L;
        String email = "gal@gmail.com";
        Long room_id = 1L;
        String roomName = "Nick's Room";
        String senderName = "nick@gmail.com";
        Invitation newInvitation = new Invitation(ownerId, email, room_id, roomName, senderName);

        doThrow(new RuntimeException("Database error")).when(invitationService).addInvitation(
                email,  room_id, roomName);

        mockMvc.perform(post("/api/invitation/share")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newInvitation)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Share failed: Database error"));
    }

    @Test
    void testRemoveDecoration_Success() throws Exception {
        //establish mock parameters
        Long ownerId = 1L;
        String email = "gal@gmail.com";
        Long room_id = 1L;
        String roomName = "Nick's Room";
        String senderName = "nick@gmail.com";
        Invitation newInvitation = new Invitation(ownerId, email, room_id, roomName, senderName);

        doNothing().when(invitationService).removeInvite(newInvitation.getInvitation_id());

        mockMvc.perform(post("/api/invitation/reject")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newInvitation)))
                .andExpect(status().isOk())
                .andExpect(content().string("INVITE deleted successfully"));
    }

    @Test
    void testRemoveDecoration_Failure() throws Exception {
        //establish mock parameters
        Long ownerId = 1L;
        String email = "gal@gmail.com";
        Long room_id = 1L;
        String roomName = "Nick's Room";
        String senderName = "nick@gmail.com";
        Invitation newInvitation = new Invitation(ownerId, email, room_id, roomName, senderName);
        doThrow(new RuntimeException("Invitation not found")).when(invitationService).removeInvite(newInvitation.getInvitation_id());

        mockMvc.perform(post("/api/invitation/reject/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newInvitation)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Delete failed: Invitation not found"));
    }
}
