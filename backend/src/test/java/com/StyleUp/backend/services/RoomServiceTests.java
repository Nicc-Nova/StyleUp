package com.StyleUp.backend.services;

import com.StyleUp.backend.models.*;
import com.StyleUp.backend.repositories.CollaborationRepository;
import org.junit.jupiter.api.Test;
import com.StyleUp.backend.repositories.DecorationRepository;
import com.StyleUp.backend.repositories.RoomRepository;
import com.StyleUp.backend.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private DecorationRepository decorationRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private CollaborationRepository collaborationRepository;

    @Mock
    private AuthenticationManager authManager;


    @InjectMocks
    private RoomService roomService;

    /*Test Add Room Success*/
    @Test
    void testAddRoom_Success() {
        /**
         * Test Case: Adding a room to a user account
         * Scenario: The User exists in the database
         * Behavior Expected:
         * - A new room is created
         * - The room repository saves the Room and returns it.
         */
        // Arrange: Define room details and mock repository behavior
        String roomName = "Bedroom";
        Long userId = 1L;  // Mock user ID
        List<Decoration> decorations = new ArrayList<>();
        Room newRoom = new Room(roomName, userId, decorations);

        // Mock SecurityContext to simulate an authenticated user
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        // Mock the Authentication object
        Authentication mockAuthentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(mockAuthentication);

        // Mock the UserPrincipal object (your custom user details)
        UserPrincipal mockUserPrincipal = mock(UserPrincipal.class);
        when(mockAuthentication.getPrincipal()).thenReturn(mockUserPrincipal);

        // Simulate the user ID being returned from UserPrincipal
        when(mockUserPrincipal.getId()).thenReturn(userId);

        // Mock repository behavior
        when(roomRepository.save(Mockito.any(Room.class))).thenReturn(newRoom);

        // Act: Call the method to test
        Room savedRoom = roomService.addRoom(roomName);

        // Assert: Verify returned room and interactions
        assertNotNull(savedRoom); // Check that savedRoom is not null
        assertEquals(roomName, savedRoom.getRoomName()); // Check that the room name is correct

        // Verify interactions with the repository
        verify(roomRepository).save(Mockito.any(Room.class)); // Verify save was called
    }


    /*Test Add Room No User Logged in Found*/
    @Test
    void testAddRoom_UserNotLoggedIn() {
        /**
         * Test Case: Attempting to add a room when no user is logged in
         * Scenario: The user is not authenticated
         * Behavior Expected:
         * - A UsernameNotFoundException is thrown with the message "No user is logged in."
         */

        // Arrange: Mock SecurityContext to simulate no authentication
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        // Simulate no authentication in SecurityContext
        when(securityContext.getAuthentication()).thenReturn(null);

        // Act & Assert: Verify exception is thrown with correct message
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> roomService.addRoom("Bedroom"));
        assertEquals("No user is logged in.", exception.getMessage());
    }


    /*Test Remove Room*/
    @Test
    void testRemoveRoom() {
        /**
         * Test Case: Adding a room to a user account
         * Scenario: The User exists in the database
         * Behavior Expected:
         * - A new room is created
         *  - The room repository saves the Room and returns it.
         */
        //Arrange: Define room details and mock repository behavior
        String roomName = "Bedroom";
        Long roomId = 1L;
        Long fku = 1L;
        List<Decoration> decorations = new ArrayList<>();
        Room roomToDelete = new Room(roomName, fku, decorations);


        // Mock existsById to simulate finding the room
        when(roomRepository.existsById(roomId)).thenReturn(true);


        // Call Method to test
        roomService.removeRoom(roomId);

        // Verify that the deleteById method was called on the roomRepository (not delete)
        verify(roomRepository).deleteById(roomId);  // Ensure deleteById was called
        verify(collaborationRepository).deleteByRoomId(roomId);  // Ensure collaborationRepository deleteByRoomId was called
    }

    @Test
    void testRemoveCollaborator() {
        // Arrange: Define room and user details
        Long roomId = 1L;
        Long userId = 2L;

        Room room = new Room();
        room.setRoom_id(roomId);

        User user = new User();
        user.setId(userId);
        Set<Room> collabRooms = new HashSet<>();
        collabRooms.add(room);
        user.setCollabRooms(collabRooms);

        // Mock Collaboration object
        Collaboration mockCollaboration = new Collaboration(userId, roomId);

        // Mock repository behavior
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(collaborationRepository.deleteByRoomIdAndUserId(roomId, userId)).thenReturn(mockCollaboration);
        when(userRepository.save(user)).thenReturn(user);

        // Act
        User updatedUser = roomService.removeCollaborator(roomId, userId);

        // Assert
        verify(userRepository).findById(userId);
        verify(roomRepository).findById(roomId);
        verify(collaborationRepository).deleteByRoomIdAndUserId(roomId, userId);
        verify(userRepository).save(user);

        assertFalse(updatedUser.getCollabRooms().contains(room), "Room should be removed from user's collabRooms");
    }


}
