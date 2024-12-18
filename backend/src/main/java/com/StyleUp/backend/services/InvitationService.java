package com.StyleUp.backend.services;

import com.StyleUp.backend.models.Invitation;
import com.StyleUp.backend.models.Room;
import com.StyleUp.backend.models.User;
import com.StyleUp.backend.models.UserPrincipal;
import com.StyleUp.backend.repositories.InvitationRepository;
import com.StyleUp.backend.repositories.RoomRepository;
import com.StyleUp.backend.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Set;

@Service
public class InvitationService {
    private final InvitationRepository invitationRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    public InvitationService(InvitationRepository invRepo, UserRepository userRepository, RoomRepository roomRepository) {
        this.invitationRepository = invRepo;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
    }

    public Invitation addInvitation(String email, Long roomId, String roomName){
        System.out.println("In addInvitation!!");
        if (!userRepository.existsByEmail(email)){
            throw new RuntimeException("User not found!");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long ownerId;
        String senderName;
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            // Cast the principal to UserPrincipal
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            // Access user details (e.g., user ID, username, etc.)
            ownerId = userPrincipal.getId();
            senderName = userPrincipal.getUsername();
            System.out.println("USER ID: " + ownerId);
            System.out.println("ROOM NAME: " + roomName);
        } else {
            // Handle case where the authentication or principal is not present
            System.out.println("No user is logged in.");
            return null;
        }
        Invitation invite = new Invitation(ownerId, email, roomId, roomName, senderName);
        System.out.println("FULL Invitation: " + invite);
        return invitationRepository.save(invite);
    }

    public void removeInvite(Long inviteId){
        if (invitationRepository.existsById(inviteId)) {
            // Delete the invitation by ID
            invitationRepository.deleteById(inviteId);
        } else {
            // Handle the case where the invite does not exist (optional)
            throw new RuntimeException("Invite not found with id: " + inviteId);
        }
    }

    public User acceptInvite(Long inviteId){
        if (invitationRepository.existsById(inviteId)) {
            Invitation invite = invitationRepository.findById(inviteId).get();
            User user = userRepository.findByEmail(invite.getEmail());
            if(roomRepository.existsById(invite.getRoom_id())) {
                Room shared = roomRepository.findById(invite.getRoom_id()).get();
                Set collabRooms = user.getCollabRooms();
                collabRooms.add(shared);
                user.setCollabRooms(collabRooms);
                invitationRepository.deleteById(inviteId);
                return userRepository.save(user);
            } else {
                throw new RuntimeException("Invite not found with id: " + invite.getRoom_id());
            }
        } else {
            // Handle the case where the invite does not exist (optional)
            throw new RuntimeException("Invite not found with id: " + inviteId);
        }
    }
}
