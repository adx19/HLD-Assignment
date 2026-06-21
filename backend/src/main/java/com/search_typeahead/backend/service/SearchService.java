package com.search_typeahead.backend.service;

import com.search_typeahead.backend.model.QueryEntry;
import com.search_typeahead.backend.model.SearchHistory;
import com.search_typeahead.backend.repository.QueryRepository;
import com.search_typeahead.backend.repository.SearchHistoryRepository;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Scheduled;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SearchService {
    private ArrayList<QueryEntry> queries;
    private HashMap<String, ArrayList<QueryEntry>> prefixIndex;
    private final QueryRepository queryRepository;
    private final RedisService redisService;
    private final ObjectMapper mapper = new ObjectMapper();
    private final ConcurrentHashMap<String, Integer> pendingUpdates = new ConcurrentHashMap<>();
    private final SearchHistoryRepository historyRepository;
    public SearchService(QueryRepository queryRepository, RedisService redisService, SearchHistoryRepository searchHistoryRepository){
        this.queryRepository = queryRepository;
        this.redisService = redisService;
        this.historyRepository = searchHistoryRepository;
        queries = new ArrayList<>();
        prefixIndex = new HashMap<>();

        loadQueriesFromCSV();
    }


    public ArrayList<QueryEntry> search(String prefix){
        System.out.println("SEARCH CALLED: " + prefix);
        prefix = prefix.toLowerCase();
        if(!prefixIndex.containsKey(prefix)){
            return new ArrayList<>();
        }

        String cacheKey = "search:" + prefix;

        String cached = redisService.get(cacheKey);

        if(cached != null){
           try{
               System.out.println("CACHE HIT: " + prefix);

               return new ArrayList<>(Arrays.asList(mapper.readValue(cached, QueryEntry[].class)));
           } catch(Exception e){
               e.printStackTrace();
           }
        }



        ArrayList<QueryEntry> ans = prefixIndex.get(prefix);

        ans.sort((a,b) -> b.getCount() - a.getCount());

        ArrayList<QueryEntry> result;

        if(ans.size() > 10){
            result = new ArrayList<>(ans.subList(0,10));
        }
        else{
            result = ans;
        }

        try {
            redisService.set(
                    cacheKey,
                    mapper.writeValueAsString(result)
            );
        }
        catch(Exception e){
            e.printStackTrace();
        }

        System.out.println("CACHE MISS: " + prefix);

        return result;
    }

    public void buildIndex(){
        for(int i = 0; i<queries.size(); i++){
            QueryEntry entry = queries.get(i);
            String word = queries.get(i).getQuery();
            for(int j = 1; j<=word.length(); j++){
                String prefix = word.substring(0, j).toLowerCase();
                if(prefixIndex.containsKey(prefix)){
                    prefixIndex.get(prefix).add(entry);
                }else{
                    ArrayList<QueryEntry> list = new ArrayList<>();
                    list.add(entry);
                    prefixIndex.put(prefix, list);
                }
            }
        }
    }

    public void loadQueriesFromCSV(){
        try{

            long rows = queryRepository.count();

            System.out.println("Rows in DB: " + rows);

            if(rows > 0){
                System.out.println("Database already populated");
                queries = new ArrayList<>(queryRepository.findAll());

                buildIndex();
                return;
            }

            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("queries.csv");

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            reader.readLine();

            while((line = reader.readLine()) != null){

                String[] parts = line.split(",");

                String query = parts[0];

                int count = Integer.parseInt(parts[1]);

                QueryEntry entry = new QueryEntry(query, count);

                queryRepository.save(entry);
            }

            System.out.println("CSV imported successfully");

        } catch (Exception e){
            e.printStackTrace();

        }
    }

    public void recordClick(String query){
        historyRepository.save(new SearchHistory(query));
        for(QueryEntry q: queries){
            if(q.getQuery().equalsIgnoreCase(query)){
                q.setCount(q.getCount() + 1);
                q.setTrendingScore(q.getTrendingScore() + 1);
                break;
            }
        }

        pendingUpdates.put(query.toLowerCase(), pendingUpdates.getOrDefault(query.toLowerCase(), 0) + 1);
    }
    public ArrayList<QueryEntry> getQueries(){
        return queries;
    }

    public ArrayList<QueryEntry> getTrending(){
        ArrayList<QueryEntry> trending = new ArrayList<>();

        for(QueryEntry q : queries){
            String query = q.getQuery().trim();

            if(query.length() < 2) continue;

            if(query.equals("-")) continue;

            if(query.equals(".")) continue;

            trending.add(q);
        }

        trending.sort((a,b) -> Double.compare(b.getTrendingScore() ,a.getTrendingScore()));

        return new ArrayList<>(trending.subList(0, Math.min(10, trending.size())));
    }

    public ArrayList<QueryEntry> getTrendingByPrefix(String prefix){
        prefix = prefix.toLowerCase();

        ArrayList<QueryEntry> result = new ArrayList<>();

        for(QueryEntry q: queries){

            String query = q.getQuery().toLowerCase();

            if(!query.startsWith(prefix)){
                continue;
            }

            if(query.length() < 2) continue;

            if(query.equals("-")) continue;

            if(query.equals(".")) continue;

            result.add(q);
        }

        result.sort((a,b) -> Double.compare(b.getTrendingScore(), a.getTrendingScore()));

        return new ArrayList<>(
                result.subList(0, Math.min(3, result.size()))
        );
    }

    @Scheduled(fixedRate=10000)
    public void flushBatchUpdates(){
        if(pendingUpdates.isEmpty()){
            return;
        }

        HashMap<String, Integer> batch = new HashMap<>(pendingUpdates);

        pendingUpdates.clear();

        System.out.println("FLUSHING " + batch.size() + " pending updates");

        for(String query: batch.keySet()){
            int increment = batch.get(query);

            System.out.println(query + " -> + " + increment);

            for(QueryEntry q: queries){
                if(q.getQuery().equalsIgnoreCase(query)){
                    queryRepository.save(q);
                    invlaidatePrefixes(query);
                    break;
                }
            }
        }
    }

    public ArrayList<SearchHistory> getHistory(){
        return historyRepository.findTop10ByOrderBySearchedAtDesc();
    }

    @Scheduled(fixedRate = 84600000)
    public void applyDecay(){
        System.out.println("DAILY DECAY RUNNING");
        queryRepository.applyDecayToAll();
        queries = new ArrayList<>(queryRepository.findAll());
    }

    private void invlaidatePrefixes(String query){
        query = query.toLowerCase();

        for(int i = 1; i <= query.length(); i++){
            String prefix = query.substring(0, i);

            redisService.delete("search:" + prefix);

            System.out.println("CACHE INVALIDATED: search: " + prefix);
        }
    }
}
