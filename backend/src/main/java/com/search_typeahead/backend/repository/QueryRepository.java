package com.search_typeahead.backend.repository;
import com.search_typeahead.backend.model.QueryEntry;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface QueryRepository extends JpaRepository<QueryEntry, Long>{

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE queries SET trending_Score = trending_score * 0.90""", nativeQuery = true)
    void applyDecayToAll();

}


