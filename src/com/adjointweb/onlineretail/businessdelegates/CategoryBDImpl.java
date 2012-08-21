package com.adjointweb.onlineretail.businessdelegates;

import java.util.List;

import javax.cache.Cache;

import com.cloudjini.onlineretail.Dao.CF;
import com.cloudjini.onlineretail.Dao.DAOFactory;
import com.cloudjini.onlineretailone.entities.Category;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class CategoryBDImpl implements CategoryBD{
private boolean CACHE_ENABLED = true;
private final String CAT_LIST_KEY = "CAT_LIST_KEY";

	/**
 * @return the cACHE_ENABLED
 */
public boolean isCACHE_ENABLED() {
	return CACHE_ENABLED;
}

/**
 * @param cACHE_ENABLED the cACHE_ENABLED to set
 */
public void setCACHE_ENABLED(boolean cACHE_ENABLED) {
	CACHE_ENABLED = cACHE_ENABLED;
}

	public void addCategory(Category category) {
		DAOFactory.getCategoryDAOInstance().addCategory(category);
		if (CACHE_ENABLED){
			Cache cache = CF.getCache();
			cache.put(category.getKey(), category);
		} 
	}

	public void updateCategory(Category category) {
		// TODO Auto-generated method stub
		DAOFactory.getCategoryDAOInstance().updateCategory(category);
		if (CACHE_ENABLED){
			Cache cache = CF.getCache();
			cache.put(category.getKey(), category);
		} 
	}

	public void deleteCategory(Key categorykey) {
		// TODO Auto-generated method stub
		
		DAOFactory.getCategoryDAOInstance().deleteCategory(categorykey);
		if (CACHE_ENABLED){
			CF.getCache().remove(categorykey);
		}
	}
	public void deleteCategory(long id) {
		// TODO Auto-generated method stub
		DAOFactory.getCategoryDAOInstance().deleteCategory(id);
		if (CACHE_ENABLED){
			Key key = KeyFactory.createKey(Category.class.getSimpleName(), id);
			CF.getCache().remove(key);
		}
	}

	public List<Category> getCategories(String categoryName) {
		// TODO Auto-generated method stub
		
		if ( CACHE_ENABLED){
			Cache cache  = CF.getCache();
			List<Category> catList = (List<Category>)cache.get(categoryName); 
			if (catList == null){
				catList = DAOFactory.getCategoryDAOInstance().getCategories(categoryName);
				cache.put(categoryName, catList);
				return catList;
			}else {
				return catList;
			}
		}
		List <Category> list = DAOFactory.getCategoryDAOInstance().getCategories(categoryName);
		return list;
	}

	public Category getCategory(long id) {
		if ( CACHE_ENABLED){
			Cache cache  = CF.getCache();
			Key key  = KeyFactory.createKey(Category.class.getSimpleName(), id);
			Category category = (Category)cache.get(key); 
			if (category== null){
				category = DAOFactory.getCategoryDAOInstance().getCategory(id);
				cache.put(category.getKey() ,category);
				return category;
			}else {
				return category;
			}
		}
		Category category = DAOFactory.getCategoryDAOInstance().getCategory(id);
		return category;
	}
	/**
	 * @author Leena Ubgade
	 * Typically should never be called. This is a very expensive call as it 
	 * will return call Categories in the Datastore
	 */
	public List<Category> getCategories() {

		if ( CACHE_ENABLED){
			Cache cache  = CF.getCache();
			List<Category> catList = (List<Category>)cache.get(CAT_LIST_KEY); 
			if (catList == null){
				catList = DAOFactory.getCategoryDAOInstance().getCategories();
				cache.put(CAT_LIST_KEY ,catList);
				return catList;
			}else {
				return catList;
			}
		}
		List <Category> list = DAOFactory.getCategoryDAOInstance().getCategories();
		return list;
	}
	

}
