package com.StyleUp.backend.services;

import com.StyleUp.backend.models.Decoration;
import com.StyleUp.backend.models.Room;
import com.StyleUp.backend.repositories.DecorationRepository;
import org.springframework.stereotype.Service;

@Service
public class DecorationService {
    private final DecorationRepository decorationRepository;

    public DecorationService(DecorationRepository decoRepository) {
        this.decorationRepository = decoRepository;
    }

    public Decoration addDecoration(String link, Long roomId, Long wishId,String description, String title, String image){
        System.out.println("In addDecoration!!");
        Decoration decoration = new Decoration(link, roomId, wishId,description, title, image);
        System.out.println("FULL DECORATION: " + decoration);
        return decorationRepository.save(decoration);
    }

    public void removeDecoration(Long decorationId){
        if (decorationRepository.existsById(decorationId)) {
            // Delete the user by ID
            decorationRepository.deleteById(decorationId);
        } else {
            // Handle the case where the user does not exist (optional)
            throw new RuntimeException("Room not found with id: " + decorationId);
        }
    }

}
