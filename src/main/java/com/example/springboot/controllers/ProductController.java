package com.example.springboot.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.models.Product;
import com.example.springboot.models.ResponseObject;
import com.example.springboot.repositories.ProductRepository;

@RestController
@RequestMapping(path = "/api/products")
public class ProductController {
	
	@Autowired
	private ProductRepository repository;
	
	@GetMapping("")
	List<Product> getAllProducts(){
		return repository.findAll();
	}
	
	@GetMapping("/{id}")
	ResponseEntity<ResponseObject> getProduct(@PathVariable Long id) {
		Optional<Product> foundProduct = repository.findById(id);
		
		return foundProduct.isPresent() ?
			ResponseEntity.status(HttpStatus.OK).body(
					new ResponseObject(HttpStatus.OK.getReasonPhrase(), "Find product successfully", foundProduct)) :
			ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new ResponseObject(HttpStatus.NOT_FOUND.getReasonPhrase(), "Cannot find product with id = " + id, ""));
	}
	
	@PostMapping("/insert")
	ResponseEntity<ResponseObject> insertProduct(@RequestBody Product product){
		List<Product> foundProducts = repository.findByName(product.getName());
		
		if(foundProducts.size() > 0) {
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
					new ResponseObject(HttpStatus.NOT_IMPLEMENTED.getReasonPhrase(), "Product Name already exist", "")
					);
		}
		return ResponseEntity.status(HttpStatus.OK).body(
				new ResponseObject(HttpStatus.OK.getReasonPhrase(), "Insert product successfully", repository.save(product))
				);
		
	}
	
	@PutMapping("/{id}")
	ResponseEntity<ResponseObject> upsertProduct(@PathVariable Long id, @RequestBody Product newProduct){
		Product updatedProduct = repository.findById(id)
								.map(product -> {
									product.setName(newProduct.getName());
									product.setPrice(newProduct.getPrice());
									product.setYear(newProduct.getYear());
									product.setUrl(newProduct.getUrl());
									return repository.save(product);
								})
								.orElseGet(() -> {
									return repository.save(newProduct);
								});
		
		return ResponseEntity.status(HttpStatus.OK).body(
				new ResponseObject(HttpStatus.OK.getReasonPhrase(), "Upsert successfully", repository.save(updatedProduct))
				);
	}
	
	@DeleteMapping("/{id}")
	ResponseEntity<ResponseObject> deleteProduct(@PathVariable Long id){
		if(repository.existsById(id)) {
			repository.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK).body(
					new ResponseObject(HttpStatus.OK.getReasonPhrase(), "Delete product successfully", "")
					);
		}
		else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new ResponseObject(HttpStatus.NOT_FOUND.getReasonPhrase(), "Product not exist", "")
					);
		}
		
	}
	

}
