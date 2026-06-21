package com.search_typeahead.backend.repository;

import com.search_typeahead.backend.model.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {

    ArrayList<SearchHistory> findTop10ByOrderBySearchedAtDesc();
}
