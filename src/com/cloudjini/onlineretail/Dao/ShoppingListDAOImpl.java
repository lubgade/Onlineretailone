package com.cloudjini.onlineretail.Dao;

import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOCanRetryException;
import javax.jdo.JDOFatalException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.cloudjini.onlineretail.Servlets.StoreServlet;
import com.cloudjini.onlineretailone.entities.Category;
import com.cloudjini.onlineretailone.entities.ShoppingList;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class ShoppingListDAOImpl implements ShoppingListDAO{
	private static final Logger logger = Logger.getLogger(ShoppingListDAOImpl.class.getCanonicalName());


	public void addShoppingList(ShoppingList list) {
		// TODO Auto-generated method stub
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			pm.makePersistent(list);
			tx.commit();
			logger.info("Added/Saved Shopping List successfully");
		}
		catch(JDOCanRetryException ex){
			logger.warning("Error in Adding/Saving:"+ex.getMessage());
			throw ex;
		}
		catch(JDOFatalException fx){
			logger.severe("Error in Adding/Saving:"+fx.getMessage());
			throw fx;

			
		}
		
		finally{
			if(tx.isActive()){
				tx.rollback();
			}
		 pm.close();
		}
		
	}

	public void updateShoppingList(ShoppingList list) {
		// TODO Auto-generated method stub
		PersistenceManager pm =  PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
		tx.begin();	
		ShoppingList listfromdb = (ShoppingList) pm.getObjectById(ShoppingList.class,list.getkey());
		listfromdb.setListName(list.getListName());
		listfromdb.setitems(list.getitems());
		tx.commit();
		logger.info("Added/Saved Shopping List successfully");
	    }
		catch(JDOCanRetryException ex){
			logger.warning("Error Updating:"+ex.getMessage());
			throw ex;
		}
		catch(JDOFatalException fx){
			logger.severe("Error Updating:"+fx.getMessage());
			throw fx;

			
		}
		
		finally{
			if(tx.isActive()){
				tx.rollback();
			}
		 pm.close();
		}
		
	}
	

	@SuppressWarnings("unchecked")
	public List<ShoppingList> getShoppingLists(String uId) {
		// TODO Auto-generated method stub
		
		PersistenceManager pm =  PMF.get().getPersistenceManager();
		Query query = (Query) pm.newQuery(ShoppingList.class);
		query.setFilter("uId == uIdParam");
		query.declareParameters("String uIdParam");
		return (List<ShoppingList>) query.execute(uId);
	
		
	}

	public ShoppingList getShoppingList(long id) {
		// TODO Auto-generated method stub
		Key key = KeyFactory.createKey(ShoppingList.class.getSimpleName(), id);
		return getShoppingList(key);
	}
	
	private ShoppingList getShoppingList(Key listKey){
		PersistenceManager pm =  PMF.get().getPersistenceManager();
		return 	pm.getObjectById(ShoppingList.class,listKey);
		
	}
	
	public void deleteShoppingList(long id) {
		// TODO Auto-generated method stub
		Key key = KeyFactory.createKey(ShoppingList.class.getSimpleName(), id);

		
		this.deleteShoppingList(key);
	}

	private void deleteShoppingList(Key key) {
		// TODO Auto-generated method stub

		PersistenceManager pm =  PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();

			// We don't have a reference to the selected Product.
			// So we have to look it up first,
			ShoppingList listdb = pm.getObjectById(ShoppingList.class,key);
			pm.deletePersistent(listdb);

			tx.commit();
			logger.info("Deleted list "+key.getId());
		} catch (JDOCanRetryException ex) {
			logger.warning("Error Deleting List"+ex.getMessage());
			throw ex;
		} catch(JDOFatalException fx){
			logger.severe("Error Deleting List:"+fx.getMessage());
			throw fx;

			
		}
		finally {
			if(tx.isActive()){
				tx.rollback();
			}
			pm.close();
		}				
		
		
		
	}

	public List<ShoppingList> searchStores(String storeName) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ShoppingList> listStores() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ShoppingList> getShoppingLists() {
		// TODO Auto-generated method stub
		PersistenceManager pm =  PMF.get().getPersistenceManager();
		Query query = (Query) pm.newQuery(ShoppingList.class);
		return (List<ShoppingList>) query.execute();
	}

	public void deleteShoppingListForUser(long uId) {
		// TODO Auto-generated method stub
		
	}

	

}
