package com.adjointweb.onlineretail.Dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.jdo.JDOCanRetryException;
import javax.jdo.JDOFatalException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.datanucleus.store.appengine.query.JDOCursorHelper;

import com.cloudjini.onlineretailone.entities.Product;
import com.cloudjini.onlineretailone.entities.Shingle;
import com.google.appengine.api.datastore.DatastoreNeedIndexException;
import com.google.appengine.api.datastore.DatastoreTimeoutException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Cursor;

public class ProductDAOImpl implements ProductDAO{
	private static final Logger logger = Logger.getLogger(ProductDAOImpl.class.getCanonicalName());
	private static final int PAGE_SIZE = 6;

	public void addProduct(Product product) {
		// TODO Auto-generated method stub
		 PersistenceManager pm = PMF.get().getPersistenceManager();
		 Transaction tx  = pm.currentTransaction();
		 try{
			    SearchSimple.updateFTSStuffForObject(product);
				tx.begin();

				pm.makePersistent(product);
				tx.commit();
		 }
		 catch (JDOCanRetryException ex) {
				ex.printStackTrace();
				logger.warning("Error adding product"+product.toString());
				throw ex;
			}
			catch(JDOFatalException fx){
			logger.severe("Error adding product :"+product.toString());
			throw fx;
			}
		 finally{
			 if (tx.isActive()){
					tx.rollback();
			 }
			 pm.close();
			 saveorupdateShingles(product);
		 }
	} 

	public void saveorupdateShingles(Product product){
		SearchDAO searchDAO  = DAOFactory.getSearchDAOInstance();
		Set<Shingle> shingles = SearchUtils.createShilgles(product.getProductName());
		searchDAO.addShingles(shingles);
	}
	
	public void updateCategoryMemberShip(Long productId, Set<Long> categoryIds){
		PersistenceManager pm =  PMF.get().getPersistenceManager();
		 Transaction tx  = pm.currentTransaction();
		 try {
				tx.begin();
				Product productdb = pm.getObjectById(Product.class, productId);
				productdb.addCategoryMemberShips(categoryIds);
				productdb.getCategoryMembership().retainAll(categoryIds);
				tx.commit();
		 }
			catch (JDOCanRetryException ex) {
				ex.printStackTrace();
				logger.warning("Error updating product"+categoryIds);
				throw ex;
			}
			catch(JDOFatalException fx){
			logger.severe("Error updating product :"+categoryIds);
			throw fx;
			}
			 finally {
				 if (tx.isActive()){
						tx.rollback();
				 }
				pm.close();
			 }
	}
	
	public void updateProduct(Product product) {
		// TODO Auto-generated method stub
		PersistenceManager pm =  PMF.get().getPersistenceManager();
		 Transaction tx  = pm.currentTransaction();

		String productName = product.getProductName();
		String prodDesc = product.getProdDesc();
		String brandName = product.getBrandName();
		String manufactName = product.getManufactName();
		Double mrp = product.getMrp();
		Double cost = product.getCost();
		Double sellPrice = product.getSellPrice();
		String UOM = product.getUOM();
		String smallImgUrl = product.getSmallImgUrl();
		String mdmImgUrl = product.getMdmImgUrl();
		String lrgImgUrl = product.getLrgImgUrl();
		List<String> prodImgs = product.getProdImgs();
		Date manufactDate = product.getManufactureDate();
		Date expiryDate = product.getExpiryDate();

 
		try {
			tx.begin();
			// We don't have a reference to the selected Product.
			// So we have to look it up first,
			Product productdb = pm.getObjectById(Product.class, product.getId());
			productdb.setProductName(productName);
			productdb.setProdDesc(prodDesc);
			productdb.setBrandName(brandName);
			productdb.setManufactName(manufactName);
			productdb.setMrp(mrp);
			productdb.setCost(cost);
			productdb.setSellPrice(sellPrice);
			productdb.setUOM(UOM);
			productdb.setSmallImgUrl(smallImgUrl);
			productdb.setMdmImgUrl(mdmImgUrl);
			productdb.setLrgImgUrl(lrgImgUrl);
			productdb.setProdImgs(prodImgs);	
			productdb.setManufactureDate(manufactDate);
			productdb.setExpiryDate(expiryDate);
			
		    SearchSimple.updateFTSStuffForObject(productdb);

			pm.makePersistent(productdb);
			tx.commit();
		}
		catch (JDOCanRetryException ex) {
			ex.printStackTrace();
			logger.warning("Error updating product"+product.toString());
			throw ex;
		}
		catch(JDOFatalException fx){
		logger.severe("Error updating product :"+product.toString());
		throw fx;
		}
		 finally {
			 if (tx.isActive()){
					tx.rollback();
			 }
			pm.close();
			saveorupdateShingles(product);
	
	}
	}

	public void deleteProduct(Product product) {
		// TODO Auto-generated method stub
		PersistenceManager pm =  PMF.get().getPersistenceManager();
		try {
			pm.currentTransaction().begin();

			// We don't have a reference to the selected Product.
			// So we have to look it up first,
			Product productdb = pm.getObjectById(Product.class,product.getId());
			pm.deletePersistent(productdb);

			pm.currentTransaction().commit();
		} catch (Exception ex) {
			pm.currentTransaction().rollback();
			throw new RuntimeException(ex);
		} finally {
			pm.close();
		}
	}

	public void deleteProduct(long id) {
		// TODO Auto-generated method stub
		this.deleteProduct(this.getProduct(id));
	}

	public List<Product> getProducts(String productName) {
		// TODO Auto-generated method stub
		PersistenceManager pm =  PMF.get().getPersistenceManager();
		Query query = (Query) pm.newQuery(Product.class);
		query.setFilter("productName == nameParam");
		query.declareParameters("String nameParam");
		return (List<Product>) query.execute(productName);
	}

	public Product getProduct(long id) {
		// TODO Auto-generated method stub
		Key key = KeyFactory.createKey(Product.class.getSimpleName(), id);
		return getProduct(key);

 
	}

	private Product getProduct(Key productKey){
		PersistenceManager pm =  PMF.get().getPersistenceManager();
		return 	pm.getObjectById(Product.class,productKey);
		
	}
	
	public List<Product> getProducts() {
		// TODO Auto-generated method stub
		PersistenceManager pm =  PMF.get().getPersistenceManager();
		Query query = (Query) pm.newQuery(Product.class);
		return (List<Product>) query.execute();
	}
	
	public Long getTotalPages() {
		PersistenceManager pm =  PMF.get().getPersistenceManager();
	    Query query = (Query) pm.newQuery(Product.class);
	    query.setResult("count(this)");
	    Long count  =  (Long) query.execute();
		return  (count/PAGE_SIZE);
	}
	
	
	public List<Product> getProducts(Long categoryID, Long productID){
		List<Product> returnList = new ArrayList();
		PersistenceManager pm =  PMF.get().getPersistenceManager();
	    Query query = (Query) pm.newQuery(Product.class);
	    query.setOrdering("id");
		query.setFilter("categoryMembership == catIDParam && id >= IDParam");
		query.declareParameters("Long IDParam,Long catIDParam");
		query.setRange(0,PAGE_SIZE+1);
		try{
			List<Product> prodList = (List<Product>)query.execute(productID, categoryID);
			returnList.addAll(prodList);
		}
		catch (JDOCanRetryException ex) {
			ex.printStackTrace();
			logger.warning("Error getting products");
			throw ex;
		}
		catch(JDOFatalException fx){
			fx.printStackTrace();
			logger.severe("Error getting products:");
			throw fx;
		}
		finally{
			pm.close();
		}
		return returnList;
		
	}
	
	public List<Product> getProducts(Long productID){
		List<Product> returnList = new ArrayList<Product>();
		PersistenceManager pm =  PMF.get().getPersistenceManager();
	    Query query = (Query) pm.newQuery(Product.class);
	    query.setOrdering("id");
		query.setFilter("id >= IDParam");
		query.declareParameters("Long IDParam");
		query.setRange(0,PAGE_SIZE+1);
		try{
		List<Product> prodList = (List<Product>)query.execute(productID);
		returnList.addAll(prodList);
		}
		catch (JDOCanRetryException ex) {
			ex.printStackTrace();
			logger.warning("Error getting products");
			throw ex;
		}
		catch(JDOFatalException fx){
			fx.printStackTrace();
			logger.severe("Error getting products:");
			throw fx;
		}
		finally{
			pm.close();
		}
		return returnList;
		
	}

	public Map<String, Object> getProducts(int pageNumber, Map<String,Object> productPage){
		List<Product> returnList = null;
		if ( productPage == null ) productPage = new HashMap();
		String cursorString = null;
		PersistenceManager pm =  PMF.get().getPersistenceManager();
	    Query query = (Query) pm.newQuery(Product.class);
	    query.setRange(0,PAGE_SIZE+1);
	    if (((String)productPage.get("PAGE")).equalsIgnoreCase("NEXT")){
	    	 cursorString = (String) productPage.get("NEXT_PRODUCT_PAGE_CURSOR");
	    }else {
	    	 cursorString = (String) productPage.get(pageNumber+"PREVIOUS_PRODUCT_PAGE_CURSOR");
	    }
		try{
		if ( cursorString != null && pageNumber > 1){
			Cursor cursor = Cursor.fromWebSafeString(cursorString);       
			Map<String, Object> extensionMap = new HashMap<String, Object>();        
			extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);        
			query.setExtensions(extensionMap);        
			query.setRange(0,PAGE_SIZE+1);
		 }
	     returnList = (List<Product>) query.execute();
	     if ( returnList.size() == (PAGE_SIZE+1)){
	    	 productPage.put("NEXT_PAGE_EXISTS", true);
	    	 productPage.put("PAGE", "NEXT");
	    	// returnList.remove(PAGE_SIZE);
	     }else {
	    	 productPage.put("NEXT_PAGE_EXISTS", false);
	     }
	     Cursor cursor =   JDOCursorHelper.getCursor(returnList); 
	     String cursorS  = cursor.toWebSafeString();
	     if ( cursorString == null || pageNumber == 1 ){
	    	 productPage.put("FIRST_PRODUCT_PAGE_CURSOR",cursorS ); 
	     }else {
		     productPage.put(pageNumber+"PREVIOUS_PRODUCT_PAGE_CURSOR", cursorString);
	     }
	     productPage.put("NEXT_PRODUCT_PAGE_CURSOR", cursorS);
	     
	    if ( returnList == null ){
			logger.info("Product : Null record found in the Datatore");
	    }else {
				long size = returnList.size();
				if ( size == 0){
				logger.info("Product:No records found in the Datatore");
				}else {
					productPage.put("PRODUCT_LIST", returnList);
				}
	    	}
		}
	    catch (DatastoreTimeoutException e) {
	    	logger.severe(e.getMessage());
	    	logger.severe("datastore timeout at: " );// +
																// " - timestamp: "
																// +
																// discreteTimestamp);
		} catch (DatastoreNeedIndexException e) {
			logger.severe(e.getMessage());
			logger.severe("datastore need index exception at: ");// +
																			// " - timestamp: "
																			// +
																			// discreteTimestamp);
		}finally{
			pm.close();
		}
		return productPage;	
	}
}