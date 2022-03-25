package com.example.springboot.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springboot.models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	
	public List<Product> findByName(String name);

}
