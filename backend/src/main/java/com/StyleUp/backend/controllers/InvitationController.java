package com.StyleUp.backend.controllers;

import com.StyleUp.backend.models.Invitation;
import com.StyleUp.backend.models.UserPrincipal;
import com.StyleUp.backend.repositories.InvitationRepository;
import com.StyleUp.backend.services.InvitationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invitation")
public class InvitationController {

    private final InvitationService invitationService;
    private final InvitationRepository invitationRepository;

    public InvitationController(InvitationService invitationService, InvitationRepository invitationRepository) {
        this.invitationService = invitationService;
        this.invitationRepository = invitationRepository;
    }

    //share will be in room controller
    //need find by email to render received invitations
    @GetMapping("/getReceived")
    public ResponseEntity<?> getReceivedInvites(@AuthenticationPrincipal UserPrincipal user) {
        try {
            // Ensure the user is authenticated and has the necessary information
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
            }
            return ResponseEntity.ok(invitationRepository.findByEmail(user.getUsername()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error fetching user details");
        }
    }
    //find by owner to render sent invitations
    @GetMapping("/getSent")
    public ResponseEntity<?> getSentInvites(@AuthenticationPrincipal UserPrincipal user) {
        try {
            // Ensure the user is authenticated and has the necessary information
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
            }
            return ResponseEntity.ok(invitationRepository.findByOwnerId(user.getId()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error fetching user details");
        }
    }
    //remove for reject invitation
    @PostMapping("/reject")
    public ResponseEntity<String> remInvite(@RequestBody Invitation invite) {
        System.out.println("RECEIVED REMOVE REQUEST FOR INVIte: " + invite.getInvitation_id());
        try {
            invitationService.removeInvite(invite.getInvitation_id());
            return ResponseEntity.ok("Decoration deleted successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Delete failed: " + e.getMessage());
        }
    }

    @PostMapping("/share")
    public ResponseEntity<String> share(@RequestBody Invitation invite) {
        System.out.println("RECEIVED Share REQUEST FOR invite: " + invite.getInvitation_id());
        try {
            invitationService.removeInvite(invite.getInvitation_id());
            return ResponseEntity.ok("Decoration deleted successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Delete failed: " + e.getMessage());
        }
    }
}
