package com.search_typeahead.backend.controller;

import com.search_typeahead.backend.model.QueryEntry;
import com.search_typeahead.backend.service.SearchService;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@CrossOrigin(origins= "http://localhost:5173")
@RestController
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService){
        this.searchService = new SearchService();
    }

    @GetMapping("/search")
    public ArrayList<QueryEntry> searchSuggestion(@RequestParam String q){
        return searchService.search(q);
    }

}
