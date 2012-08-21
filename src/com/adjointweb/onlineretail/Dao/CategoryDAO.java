/*
 * Author: Leena Ubgade
 */

package com.adjointweb.onlineretail.Dao;

import java.util.List;
import java.util.Set;

import com.adjointweb.onlineretailone.entities.Category;
import com.adjointweb.onlineretailone.entities.Product;


import com.google.appengine.api.datastore.Key;


public interface CategoryDAO {
	   void addCategory(Category category);
	   void updateCategory(Category category);
	   void deleteCategory(Key categorykey);
	   void deleteCategory(long id);
	   List<Category> getCategories(String categoryName);
	   Category getCategory(long id);
	   List<Category> getCategories();
	  
	List<Product>getProducts(long id);
		  void addProducts(long id,Set<Long>prodIds);
		  void addProducts(String id,String[] prodIds);
		   void addCategories(long id, Set<Long> catIds); 
		   void addCategories(String id, String[] catIds);
		   List<Category> getChildCategories(long id);
		   List<Category> getChildCategories(Category category);

}
