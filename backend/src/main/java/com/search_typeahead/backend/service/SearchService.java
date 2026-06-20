package com.search_typeahead.backend.service;

import com.search_typeahead.backend.model.QueryEntry;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.HashMap;

@Service
public class SearchService {
    private ArrayList<QueryEntry> queries;
    private HashMap<String, ArrayList<QueryEntry>> prefixIndex;


    public SearchService(){
        queries = new ArrayList<>();
        prefixIndex = new HashMap<>();
        queries.add(new QueryEntry("iphone", 100000));
        queries.add(new QueryEntry("iphone 15", 85000));
        queries.add(new QueryEntry("iphone charger", 60000));
        queries.add(new QueryEntry("java tutorial", 40000));
        queries.add(new QueryEntry("spring boot", 35000));

        buildIndex();
    }


    public ArrayList<QueryEntry> search(String prefix){
        prefix = prefix.toLowerCase();
        if(!prefixIndex.containsKey(prefix)){
            return new ArrayList<>();
        }

        ArrayList<QueryEntry> ans = prefixIndex.get(prefix);

        ans.sort((a,b) -> b.getCount() - a.getCount());

        if(ans.size() > 10){
            return new ArrayList<>(ans.subList(0,10));
        }

        return ans;
    }

    public void buildIndex(){
        for(int i = 0; i<queries.size(); i++){
            String word = queries.get(i).getQuery();
            for(int j = 1; j<=word.length(); j++){
                String prefix = word.substring(0, j).toLowerCase();
                if(prefixIndex.containsKey(prefix)){
                    prefixIndex.get(prefix).add(new QueryEntry(word, queries.get(i).getCount()));
                }else{
                    ArrayList<QueryEntry> list = new ArrayList<>();
                    list.add(new QueryEntry(word, queries.get(i).getCount()));
                    prefixIndex.put(prefix, list);
                }
            }
        }
    }
}
