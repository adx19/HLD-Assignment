package com.search_typeahead.backend.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "search_history")
public class SearchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String query;

    private LocalDateTime searchedAt;

    public SearchHistory(){}

    public Long getId() {
        return id;
    }

    public LocalDateTime getSearchedAt() {
        return searchedAt;
    }

    public String getQuery() {
        return query;
    }

    public SearchHistory(String query){
        this.query = query;
        this.searchedAt = LocalDateTime.now();
    }
}
