package com.search_typeahead.backend.dto;

import com.search_typeahead.backend.model.QueryEntry;

import java.util.ArrayList;

public class SearchResponse {

    private ArrayList<QueryEntry> trending;
    private ArrayList<QueryEntry> suggestions;
    private long latencyMs;

    public SearchResponse(ArrayList<QueryEntry> trending, ArrayList<QueryEntry> suggestions, long latencyMs){
        this.trending = trending;
        this.suggestions = suggestions;
        this.latencyMs = latencyMs;
    }

    public ArrayList<QueryEntry> getTrending(){
        return trending;
    }

    public ArrayList<QueryEntry> getSuggestions(){
        return suggestions;
    }

    public long getLatencyMs(){
        return latencyMs;
    }
}
