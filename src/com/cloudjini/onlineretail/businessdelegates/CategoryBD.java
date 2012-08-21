package com.cloudjini.onlineretail.businessdelegates;

import java.util.List;

import com.cloudjini.onlineretailone.entities.Category;
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
