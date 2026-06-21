package com.search_typeahead.backend.controller;

import com.search_typeahead.backend.dto.SearchResponse;
import com.search_typeahead.backend.model.QueryEntry;
import com.search_typeahead.backend.model.SearchHistory;
import com.search_typeahead.backend.service.SearchService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@CrossOrigin(origins= "http://localhost:5173")
@RestController
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService){
        this.searchService = searchService;
    }

    @GetMapping("/search")
    public SearchResponse searchSuggestion(@RequestParam String q){

        long start = System.nanoTime();

        ArrayList<QueryEntry> trending = searchService.getTrendingByPrefix(q);

        ArrayList<QueryEntry> suggestions = searchService.search(q);

        suggestions.removeIf(suggestion -> trending.stream().anyMatch(trend->trend.getQuery().equalsIgnoreCase(suggestion.getQuery())));
        long end = System.nanoTime();

        long latencyMs = (end - start) / 1_000_000;
        return new SearchResponse(trending, suggestions, latencyMs);
    }

    @PostMapping("/search/click")
    public void recordClick(@RequestParam String query){
        searchService.recordClick(query);
    }

    @GetMapping("/count")
    public int getCount(@RequestParam String query){
        for(QueryEntry q : searchService.getQueries()){
            if(q.getQuery().equalsIgnoreCase(query)){
                return q.getCount();
            }
        }
        return -1;
    }

    @GetMapping("/trending")
    public ArrayList<QueryEntry> getTrending(){
        return searchService.getTrending();
    }

    @GetMapping("/history")
    public ArrayList<SearchHistory> getHistory(){
        return searchService.getHistory();
    }

}
