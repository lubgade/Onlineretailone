package com.cloudjini.onlineretail.Dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOCanRetryException;
import javax.jdo.JDOFatalException;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import  javax.jdo.Query;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.KeepOnlyLastCommitDeletionPolicy;
import org.apache.lucene.index.LogByteSizeMergePolicy;
import org.apache.lucene.index.LogDocMergePolicy;
import org.apache.lucene.index.NoMergePolicy;
import org.apache.lucene.index.NoMergeScheduler;
import org.apache.lucene.index.SerialMergeScheduler;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;


import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.store.GAEDirectory;
import org.apache.lucene.store.GAEFileJDO;
import org.apache.lucene.store.LockObtainFailedException;

import com.beoui.geocell.GeocellManager;
import com.beoui.geocell.model.GeocellQuery;
import com.beoui.geocell.model.Point;
import com.cloudjini.onlineretail.util.Util;
import com.cloudjini.onlineretailone.entities.Address;
import com.cloudjini.onlineretailone.entities.Category;
import com.cloudjini.onlineretailone.entities.Store;
import com.cloudjini.onlineretailone.entities.Shingle;
import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;



public class StoreDAOImpl implements StoreDAO{
	private static final Logger logger = Logger.getLogger(StoreDAOImpl.class.getCanonicalName());
	long version = 1999999999;
	public void addStore(Store store) {
		 PersistenceManager pm = PMF.get().getPersistenceManager();
		 SearchSimple.updateFTSStuffForObject(store);
		 Point p = new Point(store.getAddress().getLat(), store.getAddress().getLng());
		 List<String> cells = GeocellManager.generateGeoCell(p);
		 store.getAddress().setGeoCells(cells);
		 Transaction tx  = pm.currentTransaction();
		 try{
			 tx.begin();
			 pm.makePersistent(store);
			 tx.commit();
			 logger.info("Store added successfully");
 		}
		catch (JDOCanRetryException ex) {
				ex.printStackTrace();
				logger.warning("Error adding store"+store.toString());
				throw ex;
		}
		catch(JDOFatalException fx){
			logger.severe("Error adding store"+store.toString());
			throw fx;
		}
		finally{
			 if (tx.isActive()){
					tx.rollback();
			 }
			 pm.close();
		}
			
	}//end addStore
	public void indexOneStore(Store store){
		IndexWriter writer  = getIndexWriter(version);
		 try {
			this.IndexStore(store,writer);
			writer.close();
		} catch (CorruptIndexException e) {
			logger.severe("StoreDOAImpl:CorruptIndexException"+e.getMessage() );
			e.printStackTrace();
		} catch (IOException e) {
			logger.severe("StoreDOAImpl:IOException"+e.getMessage() );
			e.printStackTrace();
		}
	
	}//end IndexOneStore
	public List<Store> searchStores(String query){
		if (query != null){
			PersistenceManager pm =  PMF.get().getPersistenceManager();
			logger.info("Searching for Store with Query:"+query);
			return SearchSimple.searchStores(query, pm);
		} else {
			logger.info("StoreDAOImpl:searchStores:Query String is null,returning null");
			return null;
		}
	}//end searchStores
	
	public Set<String> searchStoreShingles(String query){
		if (query != null){
			PersistenceManager pm =  PMF.get().getPersistenceManager();
			return SearchSimple.searchShingles(query, pm);
		} else {
			logger.info("StoreDAOImpl:searchStoreShingles:Query String is null,returning null");
			return null;	
		}
	}//end searchStoreShingles
	
	public void updateStoreCategory(long storeId, long categoryId){
		PersistenceManager pm =  PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try{
			Store storedb = pm.getObjectById(Store.class, storeId);
			tx.begin();
			storedb.setCategoryId(categoryId);
			tx.commit();
		}
		catch (JDOObjectNotFoundException ox){
			ox.printStackTrace();
			logger.warning("Error updatng store:Store Not Found"+ storeId);
		}
		catch (JDOCanRetryException ex) {
			ex.printStackTrace();
			logger.warning("Error updatng store"+ storeId);
			throw ex;
			
		}catch(JDOFatalException fx){
			logger.severe("Error updatng store"+ storeId);
			throw fx;
		}
		finally {
			if ( tx.isActive()){
				tx.rollback();
			}
			pm.close();
		}
	}
	
	
	public void updateStore(Store store) {
		//Update FTS Field
		SearchSimple.updateFTSStuffForObject(store);
		//update Geocells for geo spatial Search/Search nearby
		Point p = new Point(store.getAddress().getLat(), store.getAddress().getLng());
		List<String> cells = GeocellManager.generateGeoCell(p);
		store.getAddress().setGeoCells(cells);
		PersistenceManager pm =  PMF.get().getPersistenceManager();
		String storeName = store.getStoreName();
		String storeDesc = store.getStoreDesc();
		String modifiedBy = store.getModifiedBy();
		Date modifiedDate = new Date(System.currentTimeMillis());
		String managerName = store.getManagerName();
		List<Email> emails = store.geteMailaddreses();
		List<String> phones = store.getPhoneNumbers();
		int deliveryDistance = store.getDeliveryDistance();
		Address address = store.getAddress();
		Transaction tx = pm.currentTransaction();
		try {
				tx.begin();
			// We don't have a reference to the selected Store.
				//So we have to look it up first,
				Store storedb = pm.getObjectById(Store.class, store.getKey());
				storedb.setStoreName(storeName);
				storedb.setStoreDesc(storeDesc);
				storedb.setManagerName(managerName);
				storedb.seteMailaddreses(emails);
				storedb.setPhoneNumbers(phones);
				storedb.setDeliveryDistance(deliveryDistance);
				//get the Address object from storedb as just setting directly is creating a new one
				Address addressdb = storedb.getAddress(); 
				addressdb.setAddressline1(address.getAddressline1());
				addressdb.setAddressline2(address.getAddressline2());
				addressdb.setCity(address.getCity());
				addressdb.setState(address.getState());
				addressdb.setCountry(address.getCountry());
				addressdb.setZip(address.getZip());
				addressdb.setGeoCells(address.getGeoCells());
				addressdb.setLat(address.getLat());
				addressdb.setLng(address.getLng());
				addressdb.setModifiedDate(modifiedDate);
				addressdb.setModifiedBy(modifiedBy);
				
				
				storedb.setAllowStorePayment(store.isAllowStorePayment());
				storedb.setAllowOrderModification(store.isAllowOrderModification());
				storedb.setMultipleStores(store.isMultipleStores());
				storedb.setModifiedBy(modifiedBy);
				storedb.setModifiedDate(modifiedDate);
				pm.makePersistent(storedb);
				tx.commit();
				logger.info("Store updated successfully");
		} catch (JDOCanRetryException ex) {
			ex.printStackTrace();
			logger.warning("Error updatng store"+store.getKey().getId());
			throw ex;
			
		}catch(JDOFatalException fx){
			logger.severe("Error updatng store"+store.getKey().getId());
			throw fx;
		}
		finally {
			if ( tx.isActive()){
				tx.rollback();
			}
			pm.close();
		}
	}

	/* (non-Javadoc)
	 * @see com.cloudjini.onlineretail.Dao.StoreDAO#deleteStore(com.google.appengine.api.datastore.Key)
	 */
	public void deleteStore(Key store) {
		
		PersistenceManager pm =  PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
				tx.begin();
			   	Store storedb = pm.getObjectById(Store.class,store);
				pm.deletePersistent(storedb);
				tx.commit();
		} 
		catch (JDOObjectNotFoundException ex) {
			ex.printStackTrace();
			logger.warning("StoreDAOImpl:Error in deleting Object");
			throw ex;
		}catch(JDOCanRetryException rx){
			rx.printStackTrace();
			logger.severe("StoreDAOImpl:Error in deleting Object");
			throw rx;
		}catch(JDOFatalException fx){
			fx.printStackTrace();
			logger.severe("StoreDAOImpl:Fatal Error in deleting");
			throw fx;
		}
		
		finally {
			if ( tx.isActive()){
				tx.rollback();
			}
			pm.close();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Store> getStores(String storeName) {
		PersistenceManager pm =  PMF.get().getPersistenceManager();
		Query query = (Query) pm.newQuery(Store.class);
		query.setFilter("storeName == nameParam");
		query.declareParameters("String nameParam");
		logger.info("Store:Executing Query with Parameter"+storeName);
		List<Store> storeList = null;
		storeList  = (List<Store>) query.execute(storeName);
		return storeList;
	}
	
	private Store getStore(Key storeKey){
		PersistenceManager pm =  PMF.get().getPersistenceManager();
		Store store = pm.getObjectById(Store.class,storeKey);
		return 	store;
	}

	@SuppressWarnings("unchecked")
	public List<Store> listStores() {
		PersistenceManager pm =  PMF.get().getPersistenceManager();
		String query = "select from " + Store.class.getName();
		logger.info("Store:Getting all Stores in database");
		List<Store> storeList = null;
		storeList =  (List<Store>) pm.newQuery(query).execute();
		return storeList;
	}
	
	public  void indexall() throws CorruptIndexException, IOException{
		GAEFileJDO.batchDelete("store", version);
		List<Store> stores = listStores();
		Iterator<Store> itStores = stores.iterator();
		IndexWriter writer = getIndexWriter(version);
		while(itStores.hasNext()){
		 Store store =	(Store)itStores.next();
		 this.IndexStore(store,writer);
		 saveorupdateShingles(store);
		}
		writer.close();
	}//end Indexall
	
	public void saveorupdateShingles(Store store){
		SearchDAO searchDAO  = DAOFactory.getSearchDAOInstance();
		Set<Shingle> shingles = SearchUtils.createShilgles(store.getStoreName());
		searchDAO.addShingles(shingles);
	}
	
	
	public Store getStore(long id) {
		// TODO Auto-generated method stub
		Key key = KeyFactory.createKey(Store.class.getSimpleName(), id);
		return getStore(key);
	
	}
	public void deleteStore(long id){
		Key key = KeyFactory.createKey(Store.class.getSimpleName(), id);
		this.deleteStore(key);
	}
	
	/**
	 * @param store
	 */
	public static IndexWriter getIndexWriter(long version){
		Analyzer analyzer =  new EnglishAnalyzer(
                org.apache.lucene.util.Version.LUCENE_34);
	
		GAEDirectory gaeDirectory = new GAEDirectory("store",version);
		IndexWriterConfig config = new IndexWriterConfig(org.apache.lucene.util.Version.LUCENE_34, analyzer);
		config.setMaxBufferedDocs(10);
		config.setIndexDeletionPolicy(new KeepOnlyLastCommitDeletionPolicy());
		config.setMergePolicy(NoMergePolicy.COMPOUND_FILES);
	    config.setMaxThreadStates(1);
	    
		IndexWriter writer = null;
		try {
			writer = new IndexWriter(gaeDirectory, config);
			return writer;
		} catch (CorruptIndexException e) {
			logger.warning("Index Currupted Exception"+ e.getMessage());
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			logger.warning("Failed to Obtain Lock Exception"+ e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.warning("Failed to read Database"+ e.getMessage());
			e.printStackTrace();
		}
		return writer;
	
	}
	
	
	public  void IndexStore(Store store,IndexWriter writer) throws CorruptIndexException, IOException{
		   if ( writer == null) return;	
		    Document storeDoc = new Document();
			storeDoc.add(new Field("id", false, Util.convertlongtoString(store.getKey().getId()), Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS,Field.TermVector.NO));  
			storeDoc.add(new Field("storeName", store.getStoreName(), Field.Store.YES,Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS));  
			storeDoc.add(new Field("storeDesc", store.getStoreDesc(),Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS));  
			storeDoc.add(new Field("managerName", store.getManagerName(),Field.Store.NO, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS));  
			StringBuffer address = new StringBuffer();
			address.append(store.getAddress().getAddressline1()).append(",").append(store.getAddress().getAddressline2()).append(",").append(store.getAddress().getCity())
			 .append(",").append(store.getAddress().getState()).append(",").append(store.getAddress().getCountry()).append(",").append(store.getAddress().getZip());
			storeDoc.add(new Field("address", address.toString(),Field.Store.NO, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS));  
			storeDoc.add(new Field("city",store.getAddress().getCity() ,Field.Store.NO, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS));  
			storeDoc.add(new Field("state",store.getAddress().getState() ,Field.Store.NO, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS));  
			storeDoc.add(new Field("country",store.getAddress().getCountry(),Field.Store.NO, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS));  
			storeDoc.add(new Field("zip",store.getAddress().getZip(),Field.Store.NO, Field.Index.NOT_ANALYZED_NO_NORMS, Field.TermVector.WITH_POSITIONS_OFFSETS));  
			storeDoc.add(new Field("lat",String.valueOf(store.getAddress().getLat()),Field.Store.NO, Field.Index.NOT_ANALYZED_NO_NORMS, Field.TermVector.WITH_POSITIONS_OFFSETS));  
			storeDoc.add(new Field("lng",String.valueOf(store.getAddress().getLng()),Field.Store.NO, Field.Index.NOT_ANALYZED_NO_NORMS, Field.TermVector.WITH_POSITIONS_OFFSETS));  
			logger.info("Store Doc object during indexing:"+storeDoc.toString());
			writer.addDocument(storeDoc);
			writer.optimize();  
	}
	
	public List<Store> getStoresByAddressFields(Map<String,String> addressInfo){
		if ( addressInfo == null){
			return null;
			
		}
		
		int parameterCounter = 0;
		
		PersistenceManager pm =  PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		List<Address> addressList = null;
		StringBuffer queryBuffer = new StringBuffer();
		StringBuffer declareParametersBuffer = new StringBuffer();

		List<Store> storeList = null;
	
		pm.setDetachAllOnCommit(true);
		queryBuffer.append("SELECT FROM " + Address.class.getName() + " WHERE ");
		Map<String,String> queryParamMap = new HashMap<String,String>();
		Iterator<String> addresssFieldsIterator = addressInfo.keySet().iterator();
		while(addresssFieldsIterator.hasNext()){
			String sQueryField = addresssFieldsIterator.next();
				queryBuffer.append(sQueryField);
				queryBuffer.append(" == Param" +parameterCounter);
				declareParametersBuffer.append("String Param" + parameterCounter);
				queryParamMap.put("Param" + parameterCounter, addressInfo.get(sQueryField));
				queryBuffer.append(" && ");
				declareParametersBuffer.append(", ");
				parameterCounter++;
		
		}
		String sQuery  = queryBuffer.toString().substring(0, queryBuffer.toString().lastIndexOf("&&"));
		String sParmeters = declareParametersBuffer.toString().substring(0, declareParametersBuffer.toString().lastIndexOf(","));
		Query query = (Query) pm.newQuery(sQuery);
		query.declareParameters(sParmeters);
		
		logger.info("JDO Query:" +sQuery);
		try{
			tx.begin();
			
			addressList  = (List<Address>)query.executeWithArray(addressInfo.values().toArray());
			tx.commit();
			logger.info("StoreDAO:Stores Near This location:"+addressList.size());

		}catch(JDOObjectNotFoundException rx){
			logger.severe("Object not found"+rx.getMessage());
		}catch(JDOCanRetryException ex){
			logger.warning("Datastore exception"+ex.getMessage());
		}catch(JDOFatalException fx){
			logger.severe("Fatal Error in fetching data");
			throw fx;
		}finally{
			if ( tx.isActive()){
				tx.rollback();
			}
		}
		
		if ( addressList.size() == 0 ){
			logger.warning("StoreDAO:Zero Stores near this location");
			pm.close();
		}else{
			storeList  = getStoresFromAddressList(addressList,pm);
		}
		return storeList;
	}
	
	@SuppressWarnings("unchecked")
	public List<Store> getStoresByAddressField(String fieldName, String value){
		PersistenceManager pm =  PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		List<Address> addressList = null;
		List<Store> storeList = null;
		pm.setDetachAllOnCommit(true);
		Query query = (Query) pm.newQuery(Address.class);
		query.setFilter(fieldName+" == nameParam");
		query.declareParameters("String nameParam");
		try{
			tx.begin();
			addressList  = (List<Address>) query.execute(value);
			tx.commit();
		logger.info("StoreDAO:Stores Near This location:"+addressList.size());

		}catch(JDOObjectNotFoundException rx){
			logger.severe("Object not found"+rx.getMessage());
		}catch(JDOCanRetryException ex){
			logger.warning("Datastore exception"+ex.getMessage());
		}catch(JDOFatalException fx){
			logger.severe("Fatal Error in fetching data");
			throw fx;
		}finally{
			if ( tx.isActive()){
				tx.rollback();
			}
		}
		if ( addressList.size() == 0 ){
			logger.warning("StoreDAO:Zero Stores near this location");
			pm.close();
		}else{
			storeList  = getStoresFromAddressList(addressList,pm);
		}
		
		return storeList;
		
	}
	@SuppressWarnings("unchecked")
	private List<Store> getStoresFromAddressList(List<Address> addressList, PersistenceManager pm){
		pm.setDetachAllOnCommit(true);
		List<Store> storeList = new ArrayList<Store>();
		//List<Object> storeIds = new ArrayList<Object>();
	
		Iterator<Address> itAddress = addressList.iterator();
		while (itAddress.hasNext()){
			Address address = itAddress.next();
			Key parentKey  = address.getKey().getParent();
			Key storeKey = KeyFactory.createKey(Store.class.getSimpleName(), parentKey.getId());
			//storeIds.add(pm.newObjectIdInstance(Store.class, storeKey));
			try{
				Store store = (Store)pm.getObjectById(Store.class,storeKey);
				Store detachedStore = pm.detachCopy(store);
				detachedStore.setAddress(pm.detachCopy(store.getAddress()));
				storeList.add(detachedStore);
			}catch(JDOObjectNotFoundException rx){
				logger.severe("Object not found"+rx.getMessage());
			}catch(JDOCanRetryException ex){
				logger.warning("Datastore exception"+ex.getMessage());
			}catch(JDOFatalException fx){
				logger.severe("Fatal Error in fetching data");
				throw fx;
			}finally{
				
			 }
		}
		pm.close();
		
		if ( storeList.size()==0) logger.info("StoreDAO:No Stores near this location");
	return storeList;
		
}
	
	
	@SuppressWarnings("unchecked")
	public List<Store> getStoresNearLocation(double alat, double alng, int distance){
        GeocellQuery baseQuery = new GeocellQuery();
        Point center = new Point(alat,alng);
        List<Address> queryResults = new ArrayList<Address>();
		PersistenceManager pm =  PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		pm.setDetachAllOnCommit(true);
		try{
			tx.begin();
			queryResults  = GeocellManager.proximitySearch(center, 20,distance, Address.class, baseQuery, pm);
			tx.commit();
			logger.info("StoreDAO:Stores Near This location:"+queryResults.size());
		
		}
		catch(JDOObjectNotFoundException rx){
			logger.severe("Object not found"+rx.getMessage());
		}catch(JDOCanRetryException ex){
			logger.warning("Datastore exception"+ex.getMessage());
		}catch(JDOFatalException fx){
			logger.severe("Fatal Error in fetching data");
			throw fx;
		}finally{
			if(tx.isActive()){
				tx.rollback();
			}
		}
		List<Store> storeList = null;
		if ( queryResults.size() == 0 ){
			logger.warning("StoreDAO:Zero Stores near this location");
			pm.close();
		}
		else {
			storeList =  getStoresFromAddressList(queryResults,pm);
		}
		return storeList;
	}

}
