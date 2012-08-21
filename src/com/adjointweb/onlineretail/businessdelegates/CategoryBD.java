package com.adjointweb.onlineretail.businessdelegates;

import java.util.List;

import com.adjointweb.onlineretailone.entities.Category;
import com.google.appengine.api.datastore.Key;

public interface CategoryBD {
	   void addCategory(Category category);
	   void updateCategory(Category category);
	   void deleteCategory(Key categorykey);
	   void deleteCategory(long id);
	   List<Category> getCategories(String categoryName);
	   Category getCategory(long id);
	   List<Category> getCategories();
	   
}
