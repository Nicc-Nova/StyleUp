package com.StyleUp.backend.controllers;

import com.StyleUp.backend.services.GoogleSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController  //makes it capable of handling HTTP requests and send responses in JSON format.
@RequestMapping("/api/search") //The search endpoint will be accessible at /api/search
@CrossOrigin // Allows CORS for development, configure as needed for production
public class SearchController {

    private final GoogleSearchService googleSearchService;

    @Autowired
    public SearchController(GoogleSearchService googleSearchService) {
        this.googleSearchService = googleSearchService;
    }

    //Searches google with given String Query returning a map between the query
    @PostMapping
    public Map<String, Object> search(@RequestBody String query) {
        return googleSearchService.search(query);
    }
}