package com.adjointweb.onlineretail.Dao;

import java.util.List;


import com.cloudjini.onlineretailone.entities.Product;


public interface ProductDAO {
	
	void addProduct(Product product);
	void updateProduct(Product product);
	   void deleteProduct(Product product);
	   void deleteProduct(long id);
	   List<Product> getProducts(String productName);

	 
	   Product getProduct(long id);
	 List<Product> getProducts();
	  
}