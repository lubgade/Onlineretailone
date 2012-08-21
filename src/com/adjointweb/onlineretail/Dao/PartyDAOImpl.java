package com.adjointweb.onlineretail.Dao;

import java.util.logging.Logger;

import javax.jdo.JDOCanRetryException;
import javax.jdo.JDOFatalException;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import com.cloudjini.onlineretailone.entities.Order;
import com.cloudjini.onlineretailone.entities.Party;
import com.google.appengine.api.datastore.Key;

public class PartyDAOImpl implements PartyDAO {
	private static final Logger logger = Logger.getLogger(PartyDAOImpl.class.getCanonicalName());

	public void createParty(Party party) {
		// TODO Auto-generated method stub
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx  = pm.currentTransaction();
		 try{	
			 	
				Party partyDB = (Party)pm.getObjectById(Party.class,party.getUserId());
				partyDB.setPartyAddresses(party.getPartyAddresses());
				partyDB.setFirstName(party.getFirstName());
				partyDB.setLastName(party.getLastName());
				partyDB.setPartyName(party.getPartyName());
				partyDB.setPartyType(party.getPartyType());
				partyDB.setPhones(party.getPhones());
				

		 }catch(JDOObjectNotFoundException jx){
			 logger.info("Party Not there for user "+ party.getUserId());
			 logger.info("Party cart for user "+ party.getUserId());
			 try{
				tx.begin();
				pm.makePersistent(party);
				tx.commit();
			  }
			  catch (JDOCanRetryException ex) {
				ex.printStackTrace();
				logger.warning("Error creating party"+party.toString());
				throw ex;
			  }
			  catch(JDOFatalException fx){
				  logger.severe("Error creating party :"+ party.toString());
				  throw fx;
			  }
			 finally{
				 if (tx.isActive()){
						tx.rollback();
				 }
				 pm.close();
			 }
		 } 
		 catch (JDOCanRetryException ex) {
				ex.printStackTrace();
				logger.warning("Error creating Party"+party.toString());
				throw ex;
		}
		catch(JDOFatalException fx){
			logger.severe("Error creating Party :"+ party.toString());
			throw fx;
			}
		 finally{
			 if ( tx.isActive()){
				 tx.rollback();
			 }
			 if ( !pm.isClosed()){
				 pm.close();
			 }
		 }
		
	}

	public void updateParty(Party party) {
		// TODO Auto-generated method stub
		 PersistenceManager pm = PMF.get().getPersistenceManager();
		 Transaction tx  = pm.currentTransaction();
		 try{
				tx.begin();
				Party partyDB = (Party)pm.getObjectById(Party.class,party.getUserId());
				partyDB.setPartyAddresses(party.getPartyAddresses());
				partyDB.setFirstName(party.getFirstName());
				partyDB.setLastName(party.getLastName());
				partyDB.setPartyName(party.getPartyName());
				partyDB.setPartyType(party.getPartyType());
				partyDB.setPhones(party.getPhones());
				tx.commit();
		 }catch (JDOCanRetryException ex) {
				ex.printStackTrace();
				logger.warning("Error updating Party  "+ party);
				throw ex;
			}
			catch(JDOFatalException fx){
			logger.severe("Error updating Party:"+ party);
			throw fx;
			}
		 finally{
			 if (tx.isActive()){
					tx.rollback();
			 }
			 pm.close();
		 }
		
	}

	public Party getParty(String userId) {
		// TODO Auto-generated method stub
		 PersistenceManager pm = PMF.get().getPersistenceManager();
		 pm.setDetachAllOnCommit(true);

		 Transaction tx  = pm.currentTransaction();
		 Party partyDB =  null;
		 try{
			 tx.begin();
		  partyDB = (Party)pm.getObjectById(Party.class,userId);
		  logger.info("Address Size:" + partyDB.getPartyAddresses().size());
		  
		  tx.commit();
		  
		 }catch(JDOObjectNotFoundException jx){
			// jx.printStackTrace();
			 logger.warning("Error getting Party  "+ userId);
		
		 }
		 catch (JDOCanRetryException ex) {
				ex.printStackTrace();
				logger.warning("Error getting Party  "+ userId);
				throw ex;
			}
		catch(JDOFatalException fx){
			logger.severe("Error getting  party:"+ userId);
			throw fx;
		}
		 finally{
			 if (tx.isActive()){
				 tx.rollback();
			 }
			 pm.close();
		 }
		 
		
		return partyDB;
	}

	public void deleteParties(String partyName) {
		// TODO Auto-generated method stub
		
	}

}
