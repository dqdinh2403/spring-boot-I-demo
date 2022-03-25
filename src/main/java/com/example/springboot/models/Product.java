package com.example.springboot.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table
public class Product {
	
	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
	@SequenceGenerator(
			name = "product_sequence",
			sequenceName = "product_sequence",
			allocationSize = 1
			)
	@GeneratedValue(
			strategy = GenerationType.SEQUENCE,
			generator = "product_sequence"
			)
	private Long id;
	
	@Column(nullable = false, unique = true, length = 300)
	private String name;
	
	private Integer year;
	
	private Double price;
	
	private String url;
	
	
	public Product() {}

	public Product(String name, Integer year, Double price, String url) {
		super();
		this.name = name;
		this.year = year;
		this.price = price;
		this.url = url;
	}

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Integer getYear() {
		return year;
	}


	public void setYear(Integer year) {
		this.year = year;
	}


	public Double getPrice() {
		return price;
	}


	public void setPrice(Double price) {
		this.price = price;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", year=" + year + ", price=" + price + ", url=" + url + "]";
	}
	
}
