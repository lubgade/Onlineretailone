package com.adjointweb.onlineretail.Dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;


import com.cloudjini.onlineretailone.entities.EntityObject;
import com.cloudjini.onlineretailone.entities.Store;
import com.cloudjini.onlineretailone.entities.Shingle;
import com.google.appengine.api.datastore.DatastoreNeedIndexException;
import com.google.appengine.api.datastore.DatastoreTimeoutException;

public class SearchSimple {

	private static final Logger log = Logger.getLogger(SearchSimple.class
			.getName());

	public static final int MAXIMUM_NUMBER_OF_WORDS_TO_SEARCH = 5;

	public static final int MAX_NUMBER_OF_WORDS_TO_PUT_IN_INDEX = 10;

	public static Set<String> searchShingles(String queryString,
			PersistenceManager pm) {

		Set<String> returnSet = new HashSet<String>();

		try {
					Shingle shingle = pm.getObjectById(Shingle.class,queryString.toLowerCase());
					returnSet.addAll(shingle.getNgrams());
			
		}catch(JDOObjectNotFoundException jx){
			log.info("No Shingle for the Term in Database"+queryString);
		}
		
		catch (DatastoreTimeoutException e) {
			log.severe(e.getMessage());
			log.severe("datastore timeout at: " + queryString);// +
																// " - timestamp: "
																// +
																// discreteTimestamp);
		} catch (DatastoreNeedIndexException e) {
			log.severe(e.getMessage());
			log.severe("datastore need index exception at: " + queryString);// +
																			// discreteTimestamp);
		}finally{
			pm.close();
		}
		
		return returnSet;

	}

	public static List<Object> search(Set<String> queryTokens,Class entity,
			PersistenceManager pm) {

		StringBuffer queryBuffer = new StringBuffer();
		List<Object> result = new ArrayList<Object>();

		queryBuffer.append("SELECT FROM " + entity.getName() + " WHERE ");


		if (queryTokens.isEmpty()) {
			log.info("QueryTokens Empty");
			return result;
		}
		List<String> parametersForSearch = new ArrayList<String>(queryTokens);

		StringBuffer declareParametersBuffer = new StringBuffer();

		int parameterCounter = 0;

		while (parameterCounter < queryTokens.size()) {

			queryBuffer.append("fts == param" + parameterCounter);
			declareParametersBuffer.append("String param" + parameterCounter);

			if (parameterCounter + 1 < queryTokens.size()) {
				queryBuffer.append(" || ");
				declareParametersBuffer.append(", ");

			}

			parameterCounter++;

		}

		log.info("Query:" + queryBuffer.toString() + " Parameters"
				+ parametersForSearch);
		Query query = pm.newQuery(queryBuffer.toString());

		query.declareParameters(declareParametersBuffer.toString());

		try {
			result.addAll ((List<Object>) query.executeWithArray(parametersForSearch
					.toArray()));
			

		} catch (DatastoreTimeoutException e) {
			log.severe(e.getMessage());
			log.severe("datastore timeout at: " + queryTokens);// +
																// " - timestamp: "
																// +
																// discreteTimestamp);
		} catch (DatastoreNeedIndexException e) {
			log.severe(e.getMessage());
			log.severe("datastore need index exception at: " + queryTokens);// +
																			// " - timestamp: "
																			// +
																			// discreteTimestamp);
		}finally{
			pm.close();
		}

		return result ;

	}

	
	
	public static List<Store> searchStores(String queryString,
			PersistenceManager pm) {

		StringBuffer queryBuffer = new StringBuffer();
		List<Store> result = new ArrayList<Store>();

		queryBuffer.append("SELECT FROM " + Store.class.getName() + " WHERE ");

		Set<String> queryTokens = SearchUtils.getTokensForIndexingOrQuery(
				queryString, MAXIMUM_NUMBER_OF_WORDS_TO_SEARCH);

		if (queryTokens.isEmpty()) {
			log.info("QueryTokens Empty");
			return result;
		}
		List<String> parametersForSearch = new ArrayList<String>(queryTokens);

		StringBuffer declareParametersBuffer = new StringBuffer();

		int parameterCounter = 0;

		while (parameterCounter < queryTokens.size()) {

			queryBuffer.append("fts == param" + parameterCounter);
			declareParametersBuffer.append("String param" + parameterCounter);

			if (parameterCounter + 1 < queryTokens.size()) {
				queryBuffer.append(" && ");
				declareParametersBuffer.append(", ");

			}

			parameterCounter++;

		}

		log.info("Query:" + queryBuffer.toString() + " Parameters"
				+ parametersForSearch);
		Query query = pm.newQuery(queryBuffer.toString());

		query.declareParameters(declareParametersBuffer.toString());
		

		try {
			result  = (List<Store>) query.executeWithArray(parametersForSearch
					.toArray());

		} 
		catch(JDOObjectNotFoundException jx){
			log.warning("No records found in Database : "+ queryString);
			
		}
		catch (DatastoreTimeoutException e) {
			log.severe(e.getMessage());
			log.severe("datastore timeout at: " + queryString);// +
																// " - timestamp: "
																// +
																// discreteTimestamp);
		} catch (DatastoreNeedIndexException e) {
			log.severe(e.getMessage());
			log.severe("datastore need index exception at: " + queryString);// +
																			// " - timestamp: "
																			// +
																			// discreteTimestamp);
		}finally{
		//	pm.close();
		}

		return result;

	}

	public static void updateFTSStuffForObject(EntityObject obj) {

		StringBuffer sb = new StringBuffer();

		sb.append(obj.getStringtoBeIndex()).append(" ");

		Set<String> new_ftsTokens = SearchUtils.getTokensForIndexingOrQuery(
				sb.toString(), MAX_NUMBER_OF_WORDS_TO_PUT_IN_INDEX);

		Set<String> ftsTokens = obj.getFts();

		ftsTokens.clear();

		for (String token : new_ftsTokens) {
			ftsTokens.add(token);

		}//end for
	}//end updateFTS

}