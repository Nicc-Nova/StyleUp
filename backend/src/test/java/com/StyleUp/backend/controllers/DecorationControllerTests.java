package com.StyleUp.backend.controllers;

import com.StyleUp.backend.models.Decoration;
import com.StyleUp.backend.services.DecorationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class DecorationControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DecorationService decorationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateDecoration_Success() throws Exception {
        Decoration decoration = new Decoration("https://example.com", 1L, null,"Description", "Title", "ImageURL");

        when(decorationService.addDecoration(
                decoration.getSearchLink(),
                decoration.getFkr(),
                decoration.getWishId(),
                decoration.getDescription(),
                decoration.getTitle(),
                decoration.getImage()
        )).thenReturn(decoration);

        mockMvc.perform(post("/api/decoration/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(decoration)))
                .andExpect(status().isOk())
                .andExpect(content().string("Decoration Created successfully"));
    }

    @Test
    void testCreateDecoration_Failure() throws Exception {
        Decoration decoration = new Decoration("https://example.com", 1L, null,"Description", "Title", "ImageURL");

        doThrow(new RuntimeException("Database error")).when(decorationService).addDecoration(
                decoration.getSearchLink(),
                decoration.getFkr(),
                decoration.getWishId(),
                decoration.getDescription(),
                decoration.getTitle(),
                decoration.getImage()
        );

        mockMvc.perform(post("/api/decoration/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(decoration)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Creation failed: Database error"));
    }

    @Test
    void testRemoveDecoration_Success() throws Exception {
        Decoration decoration = new Decoration();
        decoration.setDec_id(1L);

        doNothing().when(decorationService).removeDecoration(decoration.getDec_id());

        mockMvc.perform(post("/api/decoration/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(decoration)))
                .andExpect(status().isOk())
                .andExpect(content().string("Decoration deleted successfully"));
    }

    @Test
    void testRemoveDecoration_Failure() throws Exception {
        Decoration decoration = new Decoration();
        decoration.setDec_id(1L);

        doThrow(new RuntimeException("Decoration not found")).when(decorationService).removeDecoration(decoration.getDec_id());

        mockMvc.perform(post("/api/decoration/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(decoration)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Delete failed: Decoration not found"));
    }
}