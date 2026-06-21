package com.search_typeahead.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SearchTypeAheadApplication {

	public static void main(String[] args) {
		SpringApplication.run(SearchTypeAheadApplication.class, args);
	}

}
