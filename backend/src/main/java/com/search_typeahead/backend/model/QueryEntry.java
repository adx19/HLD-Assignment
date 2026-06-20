package com.search_typeahead.backend.model;

public class QueryEntry {
    String query;
    int count;

    public QueryEntry(String q, int c){
        query = q;
        count = c;
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
}
