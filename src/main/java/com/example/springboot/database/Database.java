package com.example.springboot.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.springboot.models.Product;
import com.example.springboot.repositories.ProductRepository;

@Configuration
public class Database {
	
	private static final Logger logger = LoggerFactory.getLogger(Database.class);
	
	@Bean
	CommandLineRunner initDatabase(ProductRepository productRepository) {
		return new CommandLineRunner() {
			
			@Override
			public void run(String... args) throws Exception {
				if(productRepository.findAll().size() == 0) {
					Product productA = new Product("Product A", 1, 100.0, "");
					Product productB = new Product("Product B", 2, 200.0, "");
					logger.info("Insert data: " + productRepository.save(productA));
					logger.info("Insert data: " + productRepository.save(productB));
				}
			}
		};
	}
	
	
}
