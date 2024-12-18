package com.StyleUp.backend.services;

import com.StyleUp.backend.models.*;
import com.StyleUp.backend.repositories.InvitationRepository;
import com.StyleUp.backend.repositories.RoomRepository;
import com.StyleUp.backend.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InvitationServiceTests {
    @Mock
    private InvitationRepository invitationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private AuthenticationManager authManager;

    @InjectMocks
    private InvitationService invitationService;

    /*Test successful addition of an invitation*/
    @Test
    void testAddInvitation_success() {
        /**
         * Test Case: Adding a user an invitation to a room
         * Scenario: The User to be invited exists in the database
         * Behavior Expected:
         * - A new invitation is created
         * - The invitation repository saves the invitation and returns it.
         */
        //establish mock parameters
        Long ownerId = 1L;
        String email = "gal@gmail.com";
        Long room_id = 1L;
        String roomName = "Nick's Room";
        String senderName = "nick@gmail.com";
        Invitation newInvitation = new Invitation(ownerId, email, room_id, roomName, senderName);

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
        when(mockUserPrincipal.getId()).thenReturn(ownerId);

        // Mock repository behavior
        when(invitationRepository.save(Mockito.any(Invitation.class))).thenReturn(newInvitation);
        when(userRepository.existsByEmail(Mockito.any(String.class))).thenReturn(true);

        // Act: Call the method to test
        Invitation savedInvitation = invitationService.addInvitation(email, room_id, roomName);

        // Assert: Verify returned room and interactions
        assertNotNull(savedInvitation); // Check that savedRoom is not null
        assertEquals(roomName, savedInvitation.getRoomName()); // Check that the room name is correct

        // Verify interactions with the repository
        verify(invitationRepository).save(Mockito.any(Invitation.class)); // Verify save was called
    }

    /*Test failed addition of an invitation*/
    @Test
    void testAddInvitation_noInvitee() {
        /**
         * Test Case: Adding a user an invitation to a room
         * Scenario: The User to be invited does not exist in the database
         * Behavior Expected:
         * - no invitation is created or saved
         * - an error message is displayed that the user was not found
         */
        //establish mock parameters
        String email = "gal@gmail.com";
        Long room_id = 1L;
        String roomName = "Nick's Room";

        // Mock SecurityContext to simulate an authenticated user
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        // Mock repository behavior
        when(userRepository.existsByEmail(Mockito.any(String.class))).thenReturn(false);

        // Act: Call the method to test
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> invitationService.addInvitation(email, room_id, roomName));
        assertEquals("User not found!", exception.getMessage());
    }

    /*Test Remove Invitation*/
    @Test
    void testRemoveInvite() {
        /**
         * Test Case: Rejecting or withdrawing an invitation
         * Scenario: the invitation exists in the database
         * Behavior Expected:
         * - the invitation is removed from the database
         */
        //establish mock invitation to delete
        Long invId = 1L;
        // Mock existsById to simulate finding the room
        when(invitationRepository.existsById(invId)).thenReturn(true);
        // Call Method to test
        invitationService.removeInvite(invId);
        // Verify that the deleteById method was called on the roomRepository (not delete)
        verify(invitationRepository).deleteById(invId);  // Ensure deleteById was called
    }

    @Test
    void testAcceptInvitation() {
        /**
         * Test Case: Adding a user an invitation to a room
         * Scenario: The User to be invited exists in the database
         * Behavior Expected:
         * - A new invitation is created
         * - The invitation repository saves the invitation and returns it.
         */
        //establish mock parameters
        Long ownerId = 1L;
        String email = "gal@gmail.com";
        Long room_id = 1L;
        String roomName = "Nick's Room";
        String senderName = "nick@gmail.com";
        Invitation newInvitation = new Invitation(ownerId, email, room_id, roomName, senderName);
        User gal = new User( "gal", "riv", "gal@gmail.com", "1111", new ArrayList<>(), new ArrayList<>());
        Room galRoom = new Room("galRoom",1L,new ArrayList<>());
        List<Room> collabs = new ArrayList<>();
        collabs.add(galRoom);

        // Mock SecurityContext to simulate an authenticated user
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        // Mock repository behavior
        when(invitationRepository.existsById(Mockito.any(Long.class))).thenReturn(true);
        when(invitationRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(newInvitation));
        when(roomRepository.existsById(Mockito.any(Long.class))).thenReturn(true);
        when(roomRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(galRoom));
        when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(gal);
        when(userRepository.save(Mockito.any(User.class))).thenReturn(gal);

        // Act: Call the method to test
        User newGal = invitationService.acceptInvite(1L);

        //verify room was added to user's collab rooms list
        assertEquals(collabs.toString(), newGal.getCollabRooms().toString());

        // Verify interactions with the repository
        verify(userRepository).findByEmail(Mockito.any(String.class));//verify user is searched for
        verify(roomRepository).findById(Mockito.any(Long.class));//verify room is searched for
        verify(userRepository).save(Mockito.any(User.class)); // Verify save was called
        verify(invitationRepository).deleteById(Mockito.any(Long.class));//Verify invite was deleted after
    }
}
