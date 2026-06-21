package com.search_typeahead.backend.repository;
import com.search_typeahead.backend.model.QueryEntry;
import org.springframework.data.jpa.repository.JpaRepository;
public interface QueryRepository extends JpaRepository<QueryEntry, Long>{

}


