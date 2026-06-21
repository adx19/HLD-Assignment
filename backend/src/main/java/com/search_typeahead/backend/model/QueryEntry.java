package com.search_typeahead.backend.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "queries")
public class QueryEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String query;
    private int count;
    private double trendingScore;
    public QueryEntry(){}

    public QueryEntry(String q, int c){
        this.query = q;
        this.count = c;
        this.trendingScore = count;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public double getTrendingScore() {
        return trendingScore;
    }

    public void setTrendingScore(double trendingScore) {
        this.trendingScore = trendingScore;
    }
}
