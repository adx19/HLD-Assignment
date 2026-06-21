package com.search_typeahead.backend.repository;


import com.search_typeahead.backend.model.SearchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface SearchResultRepository extends JpaRepository<SearchResult, Long> {
    ArrayList<SearchResult> findTop10ByQueryStartingWithIgnoreCase(String query);

}
