package com.StyleUp.backend.services;

import com.StyleUp.backend.models.Decoration;
import com.StyleUp.backend.repositories.DecorationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DecorationServiceTests {

    @Mock
    private DecorationRepository decorationRepository;

    @InjectMocks
    private DecorationService decorationService;

    @Test
    void testAddDecoration_Success() {

        Decoration decoration = new Decoration("https://example.com", 1L,null ,"Description", "Title", "ImageURL");
        when(decorationRepository.save(any(Decoration.class))).thenReturn(decoration);

        Decoration createdDecoration = decorationService.addDecoration(
                "https://example.com", 1L,null, "Description", "Title", "ImageURL");

        assertNotNull(createdDecoration);
        assertEquals("https://example.com", createdDecoration.getSearchLink());
        verify(decorationRepository).save(any(Decoration.class));
    }

    @Test
    void testAddDecoration_RepositoryThrowsException() {

        when(decorationRepository.save(any(Decoration.class))).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            decorationService.addDecoration("https://example.com", 1L, null, "Description", "Title", "ImageURL");
        });
        assertEquals("Database error", exception.getMessage());
    }

    @Test
    void testRemoveDecoration_Success() {

        Long decorationId = 1L;
        when(decorationRepository.existsById(decorationId)).thenReturn(true);
        doNothing().when(decorationRepository).deleteById(decorationId);

        // Act
        decorationService.removeDecoration(decorationId);

        // Assert
        verify(decorationRepository).deleteById(decorationId);
    }

    @Test
    void testRemoveDecoration_DecorationNotFound() {

        Long decorationId = 1L;
        when(decorationRepository.existsById(decorationId)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            decorationService.removeDecoration(decorationId);
        });
        assertEquals("Room not found with id: 1", exception.getMessage());
    }
}