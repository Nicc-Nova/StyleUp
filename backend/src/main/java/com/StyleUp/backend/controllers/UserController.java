package com.StyleUp.backend.controllers;

import com.StyleUp.backend.models.Collaboration;
import com.StyleUp.backend.models.Room;
import com.StyleUp.backend.models.UserPrincipal;
import com.StyleUp.backend.repositories.CollaborationRepository;
import com.StyleUp.backend.repositories.DecorationRepository;
import com.StyleUp.backend.repositories.RoomRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final RoomRepository roomRepository;
    private final CollaborationRepository collaborationRepository;
    private final DecorationRepository decorationRepository;

    public UserController(RoomRepository roomRepository, CollaborationRepository collaborationRepository,
                          DecorationRepository decorationRepository) {
        this.roomRepository = roomRepository;
        this.collaborationRepository = collaborationRepository;
        this.decorationRepository = decorationRepository;
    }

    //Get user full name
    @GetMapping("/name")
    public ResponseEntity<String> getUserFullName(@AuthenticationPrincipal UserPrincipal user) {
        try {
            // Ensure the user is authenticated and has the necessary information
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
            }
            return ResponseEntity.ok(user.getFname() + " " + user.getLname());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error fetching user details");
        }
    }

    //Get user full name
    @GetMapping("/email")
    public ResponseEntity<String> getUserEmail(@AuthenticationPrincipal UserPrincipal user) {
        try {
            // Ensure the user is authenticated and has the necessary information
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
            }
            System.out.println("USER ID: "+user.toString());
            return ResponseEntity.ok(user.getUsername());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error fetching user details");
        }
    }

    //Get user rooms
    @GetMapping("/rooms")
    public ResponseEntity<?> getUserRooms(@AuthenticationPrincipal UserPrincipal user) {
        try {
            // Ensure the user is authenticated and has the necessary information
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
            }
            return ResponseEntity.ok(roomRepository.findByFku(user.getId()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error fetching user details");
        }
    }

    @GetMapping("/collabRooms")
    public ResponseEntity<?> getUserCollabRooms(@AuthenticationPrincipal UserPrincipal user) {
        try {
            // Ensure the user is authenticated and has the necessary information
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
            }
            List<Collaboration> collabs = collaborationRepository.findByUserId(user.getId());
            List<Room> collabRooms = new ArrayList<>();
            for (Collaboration collab : collabs) {
                Room room = roomRepository.findById(collab.getRoomId()).get();
                collabRooms.add(room);
            }
            return ResponseEntity.ok(collabRooms);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error fetching user details");
        }
    }

    @GetMapping("/wishlist")
    public ResponseEntity<?> getWishlist(@AuthenticationPrincipal UserPrincipal user) {
        try {
            // Ensure the user is authenticated and has the necessary information
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
            }
            System.out.println("wishlist is: "+decorationRepository.findByWishId(user.getId()));
            return ResponseEntity.ok(decorationRepository.findByWishId(user.getId()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error fetching user details");
        }
    }
}
