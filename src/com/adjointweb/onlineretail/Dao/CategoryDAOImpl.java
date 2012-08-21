package com.adjointweb.onlineretail.Dao;
import com.adjointweb.onlineretailone.entities.Category;
import com.adjointweb.onlineretailone.entities.Product;
import com.adjointweb.onlineretailone.entities.SubCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Collection;
import java.util.logging.Logger;

import javax.jdo.JDOCanRetryException;
import javax.jdo.JDOException;
import javax.jdo.JDOFatalException;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;


public class CategoryDAOImpl implements CategoryDAO{
	private static final Logger logger = Logger.getLogger(CategoryDAOImpl.class.getCanonicalName());
	private int NUM_RETRIES = 2;

	@SuppressWarnings("unchecked")
	public List<Category> getCategories(String categoryName) {
		// TODO Auto-generated method stub
		List<Category> returnList = null;
		PersistenceManager pm =  PMF.get().getPersistenceManager();
		 pm.setDetachAllOnCommit(true);
	//	 Transaction tx  = pm.currentTransaction();
		 try{
		// tx.begin();
		Query query = (Query) pm.newQuery(Category.class);
		query.setFilter("categoryName == nameParam");
		query.declareParameters("String nameParam");
		returnList= (List<Category>) query.execute(categoryName);
		//tx.commit();
			if ( returnList == null ){
			logger.info("Resultset is null");
			
			}else {
				long size = returnList.size();
				if ( size == 0){
					logger.info("Category:No records found in the Datatore");
				}
			}
		 }
		 catch (JDOCanRetryException ex) {
				ex.printStackTrace();
				logger.warning("Error retrieving categories ");
				throw ex;
			}
			catch(JDOFatalException fx){
				logger.severe("Error retrieving categories ");
				throw fx;
			}
			finally{
			/*	 if (tx.isActive()){
					 tx.rollback();
				 }
				 pm.close();*/
			 }
			
		 
		 return returnList;
	}
	
	public List<Category> getCategories() {
		List<Category> detachedList = null;
		List<Category> returnList=  new ArrayList<Category>();
		PersistenceManager pm =  PMF.get().getPersistenceManager();
		pm.setDetachAllOnCommit(true);
		Category detached = null;
		//Transaction tx  = pm.currentTransaction();
		try{
			
		
		//tx.begin();
		Query query = (Query) pm.newQuery(Category.class);
		detachedList = (List<Category>) query.execute();
		
		
		if ( detachedList == null ){
			logger.info("Category : Null record found in the Datatore");
		}else {
				long size = detachedList.size();
				
								
				if ( size == 0){
				logger.info("Category:No records found in the Datatore");
				}
		}
	//	tx.commit();

		}
		catch (JDOCanRetryException ex) {
			ex.printStackTrace();
			logger.warning("Error retrieving categories ");
			throw ex;
		}
		catch(JDOFatalException fx){
			logger.severe("Error retrieving categories ");
			throw fx;
		}
		finally{
			/* if (tx.isActive()){
				 tx.rollback();
			 }
			 pm.close();*/
		 }
		
		return detachedList;
		
	}
	
	
	
	public void addCategory(Category category) {
		if ( category != null ){	
		 PersistenceManager pm = PMF.get().getPersistenceManager();
		 Transaction tx  = pm.currentTransaction();
		 try{
			 for ( int i = 0; i <NUM_RETRIES;i++){
				 try{
					tx.begin();
					pm.makePersistent(category);
					tx.commit();
					logger.info("Category:Addded Successfully");
					break;
				  }
			 catch (JDOCanRetryException ex) {
					if (i== (NUM_RETRIES-1)){
						logger.warning("Category:Error in adding category"+ex.getMessage());
						throw new RuntimeException(ex);
					}
			 }//end catch
			 catch (JDOException r){
				 if (r instanceof JDOFatalException) {
					 logger.severe("Fatal Error in adding Category"+r.getMessage());
					 throw r;
				 }	
			 }
			 }//end for
		  }//end try
		 finally{
				 if ( tx.isActive()){
					 tx.rollback();
				 }
				 pm.close();
		  }
				 
		} // end category is not null
		else {
			logger.warning("Category: Not able to add category as it is null");
		}
		
	}//end addCategory
	
	

	
	
	public void updateCategory(Category category) {
		if ( category != null ) {
		PersistenceManager pm =  PMF.get().getPersistenceManager();
		String categoryName = category.getCategoryName();
		String categoryDesc = category.getCategoryDesc();
		List <SubCategory> childCategories = category.getSubCategories(); 
		Transaction tx = pm.currentTransaction();
		try {
			 for ( int i = 0; i <NUM_RETRIES;i++){
				 try {
					 tx.begin();
						// We don't have a reference to the selected Product.
						// So we have to look it up first,
						Category categorydb = pm.getObjectById(Category.class, category.getKey());
						categorydb.setCategoryName(categoryName);
						categorydb.setCategoryDesc(categoryDesc);
						categorydb.getSubCategories().retainAll(childCategories);
						pm.makePersistent(categorydb);
					 tx.commit();
					 logger.info("Category updated Successfully");
					 break;
				 	}
					catch (JDOCanRetryException ex) {
						if (i== (NUM_RETRIES-1)){
							logger.warning("Category:Error in updating category"+ex.getMessage());
							throw new RuntimeException(ex);
						}
					}//end catch
				 	catch (JDOException r){
							 if (r instanceof JDOFatalException) {
								 logger.severe("Fatal Error in updating Category"+r.getMessage());
								 throw r;
							 }//end if	
					}//end catch
		}//end for		 

		}//end try
		finally {
			if ( tx.isActive()){
				tx.rollback();
			}
			pm.close();
		}
	  }//end if category not null 
	  else {
		  logger.warning("Category: Not able to update category as it is null");
	  }
		
}




private Category getCategory(Key categoryKey){
	PersistenceManager pm =  PMF.get().getPersistenceManager();
	
	Category category = null;
	try {
		category = 	pm.getObjectById(Category.class,categoryKey);
	}catch(JDOObjectNotFoundException ex){
		logger.warning("Category does not exist in Datastore");
		throw ex;
	}	
	return category;
}


public Category getCategory(long id) {
	// TODO Auto-generated method stub
	Key key = KeyFactory.createKey(Category.class.getSimpleName(), id);
	return getCategory(key);
}


public void deleteCategory(long id) {
	Key key = KeyFactory.createKey(Category.class.getSimpleName(), id);
	this.deleteCategory(key);
}

public void deleteCategory(Key key) {
	PersistenceManager pm =  PMF.get().getPersistenceManager();
	Transaction tx  = pm.currentTransaction();
	try {
		tx.begin();
			// We don't have a reference to the selected Category.
			// So we have to look it up first,
			Category categorydb = pm.getObjectById(Category.class,key);
			pm.deletePersistent(categorydb);
		tx.commit();
		logger.info("Category Deleted successfully");
	} 
	catch(JDOObjectNotFoundException re){
		logger.info("Category not found in DB"+ re.getMessage());
		throw re;
	}
	catch (JDOFatalException ex) {
		logger.info("FatalException occured by deleting category"+ ex.getMessage());
		throw ex;
	} 
	finally {
		if ( tx.isActive()){
			tx.rollback();
		}
		pm.close();
	}
}//end deleteCategory

public List<Category> getChildCategories(Category category){
	PersistenceManager pm =  PMF.get().getPersistenceManager();
	try{
		
		Set<Long> catIds = category.getCategoryIds();
		if ( catIds == null || catIds.isEmpty()){
			return null;
		}
		Set<Object> list  = new HashSet<Object>();
		for ( Long catId : catIds){	
			Key key  = KeyFactory.createKey(Category.class.getSimpleName(), catId);
			list.add(pm.newObjectIdInstance(Category.class, key));
		}
		return (List<Category>)pm.getObjectsById(list);

	}
	catch(JDOObjectNotFoundException re){
		logger.info("Category not found in DB"+ re.getMessage());
		throw re;
	}
	catch (JDOFatalException ex) {
		logger.info("FatalException occured while getting child categories"+ ex.getMessage());
		throw ex;
	} 
	finally {
		
		pm.close();
	}
		
}


public List<Category> getChildCategories(long id){
	PersistenceManager pm =  PMF.get().getPersistenceManager();
	try{
		
		Category categorydb = pm.getObjectById(Category.class,id);
		Set<Long> catIds = categorydb.getCategoryIds();
		if ( catIds == null || catIds.isEmpty()){
			return null;
		}
		Set<Object> list  = new HashSet<Object>();
		for ( Long catId : catIds){	
			Key key  = KeyFactory.createKey(Category.class.getSimpleName(), catId);
			list.add(pm.newObjectIdInstance(Category.class, key));
		}
		return (List<Category>)pm.getObjectsById(list);

	}
	catch(JDOObjectNotFoundException re){
		logger.info("Category not found in DB"+ re.getMessage());
		throw re;
	}
	catch (JDOFatalException ex) {
		logger.info("FatalException occured while getting child categories"+ ex.getMessage());
		throw ex;
	} 
	finally {
		
		pm.close();
	}
		
}

@SuppressWarnings("unchecked")
public List<Product> getProducts(long id) {
	// TODO Auto-generated method stub
	PersistenceManager pm =  PMF.get().getPersistenceManager();

	try{
		
		Category categorydb = pm.getObjectById(Category.class,id);
		Set<Long> prodIds = categorydb.getProductIds();
		if ( prodIds == null || prodIds.isEmpty()){
			return null;
		}
		Set<Object> list  = new HashSet<Object>();
		for ( Long prodid : prodIds){	
			//Key key  = KeyFactory.createKey(Product.class.getSimpleName(), prodid);
			list.add(pm.newObjectIdInstance(Product.class, prodid));
		}
		return (List<Product>)pm.getObjectsById(list);
	}
	catch(JDOObjectNotFoundException re){
		logger.info("Category not found in DB"+ re.getMessage());
		throw re;
	}
	catch (JDOFatalException ex) {
		logger.info("FatalException occured while adding products"+ ex.getMessage());
		throw ex;
	} 
	finally {
		
		pm.close();
	}
	
}

public void addProducts(String id, String[] prodIds){
	
	long catid = Long.valueOf(id).longValue();
	Set<Long> list  = new HashSet<Long>(); 
	List<String> listprodIds = Arrays.asList(prodIds);
	for ( String s: listprodIds){
		list.add(Long.valueOf(s));
	}
	this.addProducts(catid, list);
	
}
public void addCategories(String id, String[] catIds){
	
	long catid = Long.valueOf(id).longValue();
	Set<Long> list  = new HashSet<Long>(); 
	List<String> listprodIds = Arrays.asList(catIds);
	for ( String s: listprodIds){
		list.add(Long.valueOf(s));
	}
	this.addCategories(catid,list);
	
}


public void addCategories(long id, Set<Long> catIds) {
	// TODO Auto-generated method stub
	PersistenceManager pm =  PMF.get().getPersistenceManager();

	Transaction tx  = pm.currentTransaction();
	try{
		tx.begin();	
			Category categorydb = pm.getObjectById(Category.class,id);
			if (	categorydb.getCategoryIds() == null ||  categorydb.getCategoryIds().isEmpty() ) {
				categorydb.setCategoryIds(catIds);
			}else {
				categorydb.addchildIds(catIds);				
			}
			categorydb.getCategoryIds().retainAll(catIds);
		tx.commit();	
	logger.info("Added Categories to the Category");
	}
	catch(JDOObjectNotFoundException re){
		logger.info("Category not found in DB"+ re.getMessage());
		throw re;
	}
	catch (JDOFatalException ex) {
		logger.info("FatalException occured while adding products"+ ex.getMessage());
		throw ex;
	} 
	finally {
		if ( tx.isActive()){
			tx.rollback();
		}
		pm.close();
	}
	
	
}


public void addProducts(long id, Set<Long> prodIds) {
	// TODO Auto-generated method stub
	PersistenceManager pm =  PMF.get().getPersistenceManager();

	Transaction tx  = pm.currentTransaction();
	try{
	tx.begin();	
		Category categorydb = pm.getObjectById(Category.class,id);
		if (	categorydb.getProductIds() == null ||  categorydb.getProductIds().isEmpty() ) {
			categorydb.addProducts(prodIds);
		}else {
			categorydb.addProducts(prodIds);
			categorydb.getProductIds().retainAll(prodIds);
		}
	
	tx.commit();	
	
	Iterator<Long> itProductIds  = prodIds.iterator();
	while( itProductIds.hasNext()){
		Long productId  = itProductIds.next();
		tx.begin();
		Product productdb  = pm.getObjectById(Product.class, productId);
		productdb.addCategoryMemberShip(new Long(id));
		tx.commit();
	}

	logger.info("Added Products to the Category");
	}
	catch(JDOObjectNotFoundException re){
		logger.info("Category not found in DB"+ re.getMessage());
		throw re;
	}
	catch (JDOFatalException ex) {
		logger.info("FatalException occured while adding products"+ ex.getMessage());
		throw ex;
	} 
	finally {
		if ( tx.isActive()){
			tx.rollback();
		}
		pm.close();
	}
	
	
}


}


