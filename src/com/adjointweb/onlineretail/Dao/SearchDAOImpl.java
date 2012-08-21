package com.adjointweb.onlineretail.Dao;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.jdo.JDOCanRetryException;
import javax.jdo.JDOFatalException;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.cloudjini.onlineretailone.entities.Shingle;
import com.cloudjini.onlineretailone.entities.Store;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class SearchDAOImpl implements SearchDAO {
	private static final Logger logger = Logger.getLogger(SearchDAOImpl.class.getCanonicalName());

	public Set<String> getShingles(String query) {
		PersistenceManager pm =  PMF.get().getPersistenceManager();
		return SearchSimple.searchShingles(query, pm);
		
	}
	public List<Object> search(String query, Class entity){
		PersistenceManager pm =  PMF.get().getPersistenceManager();
		
		return SearchSimple.search(this.getShingles(query), entity, pm);
		
	}
	private void addShingle(Shingle shingle){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();	
		 try{
			Shingle shingledb =  pm.getObjectById(Shingle.class, shingle.getShingle());
			if (shingledb != null) {
				shingledb.setShingle(shingle.getShingle());
				shingledb.addAll(shingle.getNgrams());
				 tx.begin();
				 pm.makePersistent(shingledb);	
				 tx.commit();
			}
		 }
		 catch(JDOObjectNotFoundException ox){
			 tx.begin();
			 pm.makePersistent(shingle);	
			 tx.commit();
			 logger.info("Shingles added successfully");
			 //ignore the exception as we want to add a new if it does not exists already
		 }
		 catch (JDOCanRetryException ex) {
				ex.printStackTrace();
				logger.warning("Error adding shingles"+shingle);
				throw new RuntimeException(ex);
		}
		 catch(JDOFatalException ex){
				logger.warning("Error adding shingles"+shingle);
				throw ex;
		 }
		finally{
			 if (tx.isActive()){
				 tx.rollback();
			 }
			 pm.close();
				 
		 }
		
	}

	
	public void addShingles(Set<Shingle> shingles) {
		 if ( shingles == null){
			 logger.warning("Shingles are Null cannot add");
		}else {
			Iterator<Shingle> itShingle = shingles.iterator();
			while(itShingle.hasNext()){
				addShingle(itShingle.next());
			}
		}
	}

	public Shingle getShingle(String shingle){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		Shingle shingleObj = null;
		 try{
			shingleObj =  pm.getObjectById(Shingle.class, shingle);
		 }
		 catch(JDOObjectNotFoundException ox){
			 logger.info("Shingles not  Found");
		 }
		 catch (JDOCanRetryException ex) {
				ex.printStackTrace();
				logger.warning("Error getting shingles"+shingle);
				throw new RuntimeException(ex);
		}
		 catch(JDOFatalException ex){
				logger.warning("Error gettting shingles"+shingle);
				throw ex;
		 }
		finally{
			 if (tx.isActive()){
				 tx.rollback();
			 }
			 pm.close();
				 
		 }
		
		return shingleObj;
		
	}

	public void deleteShingles(String query) {
		if ( query == null) {
			logger.warning("SearchDAO: Query is null");
		}else {
		Shingle shingle = this.getShingle(query);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
				tx.begin();
					pm.deletePersistent(shingle);
				tx.commit();
				logger.info("Deleted Shingles successfully"+shingle);
			} catch (JDOCanRetryException ex) {
				logger.warning("Error deleting shingles"+shingle);
				throw new RuntimeException(ex);
			} 
			catch (JDOFatalException rx){
				logger.severe("Fatal Error deleting shingles"+shingle);
				throw rx;
			}
			finally {
				if ( tx.isActive()){
					tx.rollback();
				}
				pm.close();
			}
		}//end else
	}//end deleteShingles

	

}
