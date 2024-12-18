package com.StyleUp.backend.controllers;

import com.StyleUp.backend.models.Room;
import com.StyleUp.backend.models.Decoration;
import com.StyleUp.backend.models.UserPrincipal;
import com.StyleUp.backend.repositories.DecorationRepository;
import com.StyleUp.backend.services.DecorationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/api/decoration")
public class DecorationController {

    private final DecorationService decoService;
    private final DecorationRepository decoRepository;

    public DecorationController(DecorationService decoService, DecorationRepository decoRepository) {
        this.decoService = decoService;
        this.decoRepository = decoRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createDecoration(@RequestBody Decoration decoration) {
        String link= decoration.getSearchLink();
        Long roomId=decoration.getFkr();
        Long wishId=decoration.getWishId();
        String description=decoration.getDescription();
        String title=decoration.getTitle();
        String image=decoration.getImage();

        System.out.println("RECEIVED CREATE REQUEST FOR Decoration: " + decoration);
        try {
            decoService.addDecoration(link, roomId, wishId,description, title,  image);
            return ResponseEntity.ok("Decoration Created successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Creation failed: " + e.getMessage());
        }
    }

    @PostMapping("/wish")
    public ResponseEntity<String> wishDecoration(@RequestBody Decoration decoration) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String link= decoration.getSearchLink();
        Long wishId;
        String description=decoration.getDescription();
        String title=decoration.getTitle();
        String image=decoration.getImage();

        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            // Cast the principal to UserPrincipal
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            // Access user details (e.g., user ID, username, etc.)
            wishId = userPrincipal.getId();
            System.out.println("Wish ID: " + wishId);
        } else {
            // Handle case where the authentication or principal is not present
            System.out.println("No user is logged in.");
            return null;
        }

        System.out.println("RECEIVED Wish REQUEST FOR Decoration: " + decoration);
        try {
            decoService.addDecoration(link, null, wishId,description, title,  image);
            return ResponseEntity.ok("Decoration wishlisted successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Wishlist failed: " + e.getMessage());
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<String> remDecoration(@RequestBody Decoration decoration) {
        System.out.println("RECEIVED REMOVE REQUEST FOR DECORATION: " + decoration.getDec_id());
        try {
            decoService.removeDecoration(decoration.getDec_id());
            return ResponseEntity.ok("Decoration deleted successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Delete failed: " + e.getMessage());
        }
    }
}