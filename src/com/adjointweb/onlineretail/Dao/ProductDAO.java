package com.adjointweb.onlineretail.Dao;

import java.util.List;


import com.adjointweb.onlineretailone.entities.Product;


public interface ProductDAO {
	
	void addProduct(Product product);
	void updateProduct(Product product);
	   void deleteProduct(Product product);
	   void deleteProduct(long id);
	   List<Product> getProducts(String productName);
	   List<Product> getProducts(Long aProductId);

	   List<Product> getProducts(Long aCategoryId, Long aProductId);

	   Product getProduct(long id);
	 List<Product> getProducts();
	  
}