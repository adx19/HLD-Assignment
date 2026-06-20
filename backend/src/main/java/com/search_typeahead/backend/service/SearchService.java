package com.search_typeahead.backend.service;

import com.search_typeahead.backend.model.QueryEntry;
import org.springframework.stereotype.Service;


import java.util.ArrayList;

@Service
public class SearchService {
    private ArrayList<QueryEntry> queries;

    public SearchService(){
        queries = new ArrayList<>();
        queries.add(new QueryEntry("iphone", 100000));
        queries.add(new QueryEntry("iphone 15", 85000));
        queries.add(new QueryEntry("iphone charger", 60000));
        queries.add(new QueryEntry("java tutorial", 40000));
        queries.add(new QueryEntry("spring boot", 35000));
    }


    public ArrayList<QueryEntry> search(String prefix){

        ArrayList<QueryEntry> ans = new ArrayList<>();

        for(int i = 0; i<queries.size(); i++){
            QueryEntry q = queries.get(i);
            String word = q.getQuery();
            if(word.toLowerCase().startsWith(prefix)){
                ans.add(new QueryEntry(word, q.getCount()));
            }


        }

        ans.sort((a, b) -> b.getCount() - a.getCount());
        return ans;
    }
}
