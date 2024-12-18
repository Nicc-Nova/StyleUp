package com.StyleUp.backend.controllers;

import com.StyleUp.backend.models.*;
import com.StyleUp.backend.repositories.CollaborationRepository;
import com.StyleUp.backend.repositories.RoomRepository;
import com.StyleUp.backend.repositories.UserRepository;
import com.StyleUp.backend.services.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/room")
public class RoomController {

    private final RoomService roomService;
    private final RoomRepository roomRepository;
    private final CollaborationRepository collaborationRepository;
    private final UserRepository userRepository;

    public RoomController(RoomService roomService, RoomRepository roomRepository,
                          CollaborationRepository collaborationRepository, UserRepository userRepository) {
        this.roomService = roomService;
        this.roomRepository = roomRepository;
        this.collaborationRepository = collaborationRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createRoom(@RequestBody Room room) {
        String roomName = room.getRoomName();
        System.out.println("RECEIVED CREATE REQUEST FOR ROOM: "+roomName);
        try {
            roomService.addRoom(roomName);
            return ResponseEntity.ok("Room Created successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Creation failed: " + e.getMessage());
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<String> remRoom(@RequestBody Room room) {
        System.out.println("RECEIVED remove REQUEST FOR ROOM: "+room);
        Long roomId=room.getRoom_id();
        System.out.println("RECEIVED REMOVE REQUEST FOR ROOM: "+roomId);
        try {
            roomService.removeRoom(roomId);
            return ResponseEntity.ok("Room deleted successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Delete failed: " + e.getMessage());
        }
    }

    @PostMapping("/getThisRoom")
    public ResponseEntity<Room> getThisRoom(@RequestBody Room room) {
        System.out.println("RECEIVED GET REQUEST FOR ROOM: "+room.toString());
        Long roomId=room.getRoom_id();
        System.out.println("RECEIVED GET REQUEST FOR ROOM: " + roomId);
        Room thisRoom;
        if(roomRepository.findById(roomId).isPresent()) {
            thisRoom = roomRepository.findById(roomId).get();
            return ResponseEntity.ok(thisRoom);
        } else{
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping("/leave")
    public ResponseEntity<String> leaveRoom(@RequestBody Invitation invitation) {
        System.out.println("RECEIVED LEAVE REQUEST FOR ROOM: "+invitation);
        try{
            String email = invitation.getEmail(); //stupid redundant parsing bs
            User user1 = userRepository.findByEmail(email);
            roomService.removeCollaborator(invitation.getRoom_id(), user1.getId());
            return ResponseEntity.ok("Collaborator left successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to leave room: " + e.getMessage());
        }

    }

    @PostMapping("/getCollaborators")
    public ResponseEntity<?> getCollaborators(@RequestBody Room room) {
        System.out.println("RECEIVED GETCOLLABORATORS REQUEST FOR ROOM: "+room);
        try{
            List<Collaboration> collabs = collaborationRepository.findByRoomId(room.getRoom_id());
            System.out.println("collabs: "+collabs);
            List<User> collabUsers = new ArrayList<>();
            for (Collaboration collab : collabs) {
                User user = userRepository.findById(collab.getUserId()).get();
                collabUsers.add(user);
            }
            System.out.println("collabUsers: "+collabUsers);
            return ResponseEntity.ok(collabUsers);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
