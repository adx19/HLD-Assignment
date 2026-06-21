package com.search_typeahead.backend.controller;

import com.search_typeahead.backend.cache.CacheNode;
import com.search_typeahead.backend.cache.ConsistentHashingRing;
import com.search_typeahead.backend.model.QueryEntry;
import com.search_typeahead.backend.service.SearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class CacheDebugController {

    private final ConsistentHashingRing ring;
    private final SearchService searchService;

    public CacheDebugController(ConsistentHashingRing ring, SearchService searchService){
        this.ring = ring;
        this.searchService = searchService;
    }

    @GetMapping("/cache/debug")
    public HashMap<String, String> debug(@RequestParam String prefix){

        CacheNode node = ring.getNode(prefix);

        HashMap<String, String> response = new HashMap<>();

        response.put("prefix", prefix);
        response.put("node", node.getNodeId());

        return response;
    }

    @GetMapping("/cache/distribution")
    public HashMap<String, Integer> distribution(){

        HashMap<String, Integer> counts = new HashMap<>();

        for(QueryEntry q: searchService.getQueries()){
            CacheNode node = ring.getNode(q.getQuery());

            counts.put(node.getNodeId(), counts.getOrDefault(node.getNodeId(), 0) + 1);
        }

        return counts;
    }
}
