package com.cloudjini.onlineretailone.entities;

import java.util.logging.Logger;

import javax.jdo.JDOCanRetryException;
import javax.jdo.JDOFatalException;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.cloudjini.onlineretail.Dao.PMF;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class OrderNumberGenerator {
	private static final Logger logger = Logger.getLogger(OrderNumberGenerator.class.getCanonicalName());
	@PrimaryKey
	@Persistent
	private String Ordergenerator;
	
	
	/**
	 * @return the ordergenerator
	 */
	public String getOrdergenerator() {
		return Ordergenerator;
	}

	/**
	 * @param ordergenerator the ordergenerator to set
	 */
	public void setOrdergenerator(String ordergenerator) {
		Ordergenerator = ordergenerator;
	}
	@Persistent
	private long previousOrderNumber;
	
	
	public static String generateOrderNumber(long series){
		
		OrderNumberGenerator orderGenerator = getOrderGenerator(series);
	 	long newOrderNumber  = orderGenerator.getPreviousOrderNumber() +1;
	 	orderGenerator.setPreviousOrderNumber(newOrderNumber);
	 	createorupdateOrdernumber(orderGenerator);
		return ""+newOrderNumber;
	}
	
	public static OrderNumberGenerator getOrderGenerator(long series){
		PersistenceManager pm =  PMF.get().getPersistenceManager();
		OrderNumberGenerator orderGenerator = null;
		 try {
				 orderGenerator = pm.getObjectById(OrderNumberGenerator.class, "ORDERGENERATOR"+series);
		 }
		 catch(JDOObjectNotFoundException jx){
				logger.info("OrderGeneratorNotFound creating new orderGenerator" );
				orderGenerator = new OrderNumberGenerator();
				orderGenerator.setPreviousOrderNumber(series);
				orderGenerator.setOrdergenerator("ORDERGENERATOR"+series);
				OrderNumberGenerator.createorupdateOrdernumber(orderGenerator);
		 }
			catch (JDOCanRetryException ex) {
				ex.printStackTrace();
				logger.warning("Error getting order generator");
				throw ex;
			}
			catch(JDOFatalException fx){
				logger.severe("Error updating order generator");
				throw fx;
			}
			 finally {
				pm.close();
			 }
		return orderGenerator;

	}
	public static void createorupdateOrdernumber(OrderNumberGenerator ordernumber){
		 PersistenceManager pm = PMF.get().getPersistenceManager();
		 Transaction tx  = pm.currentTransaction();
		 try{
				tx.begin();

				pm.makePersistent(ordernumber);
				tx.commit();
		 }
		 catch (JDOCanRetryException ex) {
				ex.printStackTrace();
				logger.warning("Error adding ordernumber"+ordernumber.toString());
				throw ex;
			}
			catch(JDOFatalException fx){
			logger.severe("Error adding ordernumber :"+ordernumber.toString());
			throw fx;
			}
		 finally{
			 if (tx.isActive()){
					tx.rollback();
			 }
			 pm.close();
		 }

	}
	public long getPreviousOrderNumber() {
		return previousOrderNumber;
	}
	public void setPreviousOrderNumber(long previousOrderNumber) {
		this.previousOrderNumber = previousOrderNumber;
	}
	

}
