package com.adjointweb.onlineretail.Dao;

import java.util.logging.Logger;

import javax.jdo.JDOCanRetryException;
import javax.jdo.JDOFatalException;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import com.cloudjini.onlineretailone.entities.ShoppingCart;
import com.cloudjini.onlineretailone.entities.ShoppingCartItem;
import com.google.appengine.api.datastore.DatastoreNeedIndexException;
import com.google.appengine.api.datastore.DatastoreTimeoutException;

public class ShoppingCartDAOImpl implements ShoppingCartDAO {
	private static final Logger logger = Logger.getLogger(ShoppingCartDAOImpl.class.getCanonicalName());

	public ShoppingCart createShoppingCart(ShoppingCart cart) {
		// TODO Auto-generated method stub
		 PersistenceManager pm = PMF.get().getPersistenceManager();
		 Transaction tx  = pm.currentTransaction();
		 try{
				ShoppingCart cartdb = (ShoppingCart)pm.getObjectById(ShoppingCart.class, cart.getUserId());
				tx.begin();
				cartdb.addItems(cart.getItems());
				cartdb.retainItems(cart.getItems());
				tx.commit();
				return cartdb;
		 }
		 catch(JDOObjectNotFoundException ox){
				logger.info("Cart Not there for user "+ cart.getUserId());
				logger.info("creating cart for user "+ cart.getUserId());
				try{
					tx.begin();
						pm.makePersistent(cart);
					tx.commit();
					ShoppingCart cartdb = (ShoppingCart)pm.getObjectById(ShoppingCart.class, cart.getUserId());
					return cartdb;
				}catch (JDOCanRetryException ex) {
					ex.printStackTrace();
					logger.warning("Error creating Cart"+cart.toString());
					throw ex;
				}
				catch(JDOFatalException fx){
				logger.severe("Error creating cart :"+ cart.toString());
				throw fx;
				}finally{
					 if (tx.isActive()){
							tx.rollback();
					 }
					 pm.close();
				 }
				
				
		 }
		 catch (JDOCanRetryException ex) {
				ex.printStackTrace();
				logger.warning("Error creating Cart"+cart.toString());
				throw ex;
			}
			catch(JDOFatalException fx){
			logger.severe("Error creating cart :"+ cart.toString());
			throw fx;
			}
		 finally{
			 if (tx.isActive()){
					tx.rollback();
			 }

			 if ( !pm.isClosed()){
				 pm.close();
			 }
		 }
		 
	}

	public void addToCart(String userId ,ShoppingCartItem item) {
		// TODO Auto-generated method stub
		 PersistenceManager pm = PMF.get().getPersistenceManager();
		 Transaction tx  = pm.currentTransaction();
		 try{
			tx.begin();
			ShoppingCart cartdb = (ShoppingCart)pm.getObjectById(ShoppingCart.class, userId);
			cartdb.addItem(item);
			tx.commit();

		 }
		 catch (JDOCanRetryException ex) {
				ex.printStackTrace();
				logger.warning("Error updating cart  "+ item);
				throw ex;
			}
			catch(JDOFatalException fx){
			logger.severe("Error updating cart  :"+ item);
			throw fx;
			}
		 finally{
			 if (tx.isActive()){
					tx.rollback();
			 }
			 pm.close();
		 }
	} 

	public void updateShoppingCart(ShoppingCart cart) {
		// TODO Auto-generated method stub
		 PersistenceManager pm = PMF.get().getPersistenceManager();
		 Transaction tx  = pm.currentTransaction();
		 try{
			tx.begin();
			ShoppingCart cartdb = (ShoppingCart)pm.getObjectById(ShoppingCart.class, cart.getUserId());
			cartdb.addItems(cart.getItems());
			cartdb.retainItems(cart.getItems());
			tx.commit();

		 }
		 catch (JDOCanRetryException ex) {
				ex.printStackTrace();
				logger.warning("Error updating cart  "+ cart);
				throw ex;
			}
			catch(JDOFatalException fx){
			logger.severe("Error updating cart  :"+ cart);
			throw fx;
			}
		 finally{
			 if (tx.isActive()){
					tx.rollback();
			 }
			 pm.close();
		 }
	}
	public void deleteShoppingCart(String userId){
		 PersistenceManager pm = PMF.get().getPersistenceManager();
		 Transaction tx = pm.currentTransaction();
		 try{
			ShoppingCart cartReturn = (ShoppingCart)pm.getObjectById(ShoppingCart.class, userId);
			tx.begin();
			 pm.deletePersistent(cartReturn);
			tx.commit();
			logger.info("cart Deleted");
		 }catch(JDOObjectNotFoundException jx){
			 logger.warning("No Shopping cart for user available");
		 }
		 finally{
			 if (tx.isActive()){
					tx.rollback();
			 }
			 pm.close();
			 
		 }

	}

	public ShoppingCart getShoppingCart(String userid) {
		// TODO Auto-generated method stub
		 ShoppingCart cartReturn = null;
		 ShoppingCart detached = null;
		 PersistenceManager pm = PMF.get().getPersistenceManager();

		 Transaction tx = pm.currentTransaction();
		 pm.setDetachAllOnCommit(true);
		 try{
			 tx.begin();
			 cartReturn = (ShoppingCart)pm.getObjectById(ShoppingCart.class, userid);
			 logger.info("items in cart" + cartReturn.getItems().size());
			 tx.commit();
		 }catch(JDOObjectNotFoundException jx){
			 logger.warning("No Shopping cart for user available");
		 }
		 catch (DatastoreTimeoutException e) {
		    	logger.severe(e.getMessage());
		    	logger.severe("datastore timeout at: " );// +
		 } 
		 catch (DatastoreNeedIndexException e) {
				logger.severe(e.getMessage());
				logger.severe("datastore need index exception at: ");// +
		}
		 finally{
			 if ( tx.isActive()){
				 tx.rollback();
			 }
			 pm.close();
		 }
		 return cartReturn;
	}
}
