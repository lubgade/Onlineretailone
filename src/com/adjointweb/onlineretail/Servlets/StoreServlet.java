package com.adjointweb.onlineretail.Servlets;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.GAEIndexReader;
import org.apache.lucene.index.GAEIndexReaderPool;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.MultiFieldSimpleQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.mortbay.log.Log;
import org.json.simple.JSONValue;

import com.adjointweb.onlineretail.Dao.DAOFactory;
import com.adjointweb.onlineretail.Dao.StoreDAO;
import com.cloudjini.onlineretailone.entities.Store;
import com.cloudjini.onlineretailone.entities.Shingle;
import com.google.appengine.api.backends.BackendServiceFactory;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;
import com.liferay.util.ParamUtil;

/**
 * 
 * @author rushikesh_ubgade
 *
 */
@SuppressWarnings("serial")
public class StoreServlet extends  BaseServlet {
	private static final Logger logger = Logger.getLogger(StoreServlet.class.getCanonicalName());
	
	
	/**
	 * Get the requested store entities in JSON format
	 */
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

    super.doGet(req, resp);
    logger.log(Level.INFO, "Obtaining Store listing");
    doPost(req,resp);
    return;

  }

	/**
	 * Insert the new store
	 */
  protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
    logger.log(Level.INFO, "Creating Store");
	PrintWriter out = resp.getWriter();  
	
    String jstring = req.getParameter("JSON");
 
    Store store = Store.getStorefromjsonstring(jstring);
    StoreDAO storeDao = DAOFactory.getStoreDAOInstance();
    String id = store.getId();
    if ( id == null){
    
    storeDao.addStore(store);
    }
    else {
    	storeDao.updateStore(store);
    }
    out.println("Saved Successfully");
  }

	/**
	 * Delete the Store
	 */
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
    String storeKey = req.getParameter("id");
    logger.log(Level.INFO, "Deleting User {0}", storeKey);
    StoreDAO storeDao = DAOFactory.getStoreDAOInstance();
    long storeid = Long.parseLong(storeKey);
    storeDao.deleteStore(storeid);
    
  }
 
  private JSONObject getJSONObject(List<Store> stores){
	  	
	  	JSONArray storesjson = new JSONArray();
		Iterator<Store> storeIt = stores.iterator();
		while(storeIt.hasNext()){
			Store store = storeIt.next();
			JSONObject jsonObject = store.getJSONObject();
			storesjson.add( jsonObject);
		}
		JSONObject data = new JSONObject();
		data.put("data", storesjson);	
	  
	  return data;
	  
  }
  
  private JSONObject getJSONforShingles(Set<String> shingles){
	  JSONArray jsonArray = new JSONArray();
		Iterator<String> shingleIt = shingles.iterator();
		while(shingleIt.hasNext()){
			String shingle = shingleIt.next();
			jsonArray.add(shingle);
		}
		JSONObject data = new JSONObject();
		data.put("data", jsonArray);	
	  return data;
	  
  } 
  
  @SuppressWarnings("unchecked")
protected void doSearch(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	  	//resp.setContentType("application/json; charset=utf-8");
		//resp.setHeader("Cache-Control", "no-cache");
		String query = req.getParameter("query");
		if ( query != null ){
			logger.warning("Query is :" +query);	

			StoreDAO storeDao = DAOFactory.getStoreDAOInstance();
			List<Store> stores = storeDao.searchStores(query);
			if ( stores != null){
				logger.warning("Number of results for :" +query);	

				JSONObject data = this.getJSONObject(stores);
				PrintWriter out = resp.getWriter();  
				out.println(data.toJSONString());
			}
		}
		logger.warning("doSearch: Query is null or empty");
	}
  
  protected void searchasyouType(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String query = req.getParameter("query");
		StoreDAO storeDao = DAOFactory.getStoreDAOInstance();
 		Set<String> shingles = storeDao.searchStoreShingles(query);
		JSONObject data = new JSONObject();
		data.put("data", this.getJSONforShingles(shingles));
		PrintWriter out = resp.getWriter();
		logger.info("JSON:" +data.toJSONString());
		out.println(data.toJSONString());
	}
/*
  protected void searchusingLucene(HttpServletRequest req, HttpServletResponse resp) throws InvalidTokenOffsetsException, IOException, ParseException{
		List<Store> stores = new ArrayList<Store>();
	    StoreDAO storeDao = DAOFactory.getStoreDAOInstance();
		String queryString = ParamUtil.getString(req, "query", "").trim();

	    
	    logger.info("Searching for Store using query " + queryString);	
	 Analyzer analyzer = new EnglishAnalyzer(
				org.apache.lucene.util.Version.LUCENE_34);

		String[] matchFields = new String[] { "storeName", "storeDesc" };
		
		BooleanClause.Occur[] matchFlags = new BooleanClause.Occur[] {
				BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD};

		
	//	GAEIndexReaderPool readerPool = GAEIndexReaderPool.getInstance();
		//GAEIndexReader indexReader;
		
		GAEVFSDirectory gaeVfsDirectory = new GAEVFSDirectory("store");
		IndexReader indexReader = IndexReader.open(gaeVfsDirectory);	
	//	indexReader = readerPool.borrowReader("store");
		if (indexReader != null) {
				

		int maxDoc = indexReader.maxDoc();
		int numDocs = indexReader.numDocs();
		int hitsPerPage = 10;
		logger.info("no of docs"+ numDocs);
		logger.info("no of max docs"+ maxDoc);
		
		Query queryObject;
		MultiFieldSimpleQueryParser parser = new MultiFieldSimpleQueryParser(matchFields,analyzer, new HashMap()); 
				
		queryObject  = parser.parse(queryString);
		IndexSearcher searcher = new IndexSearcher(indexReader);
		TopScoreDocCollector collector = TopScoreDocCollector.create(
				hitsPerPage, true);
		searcher.search(queryObject, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		int resultCount = hits.length;

		//readerPool.returnReader(indexReader);
		if (resultCount > 0) {
			logger.info("Results more than 0");
			QueryScorer queryScorer = new QueryScorer(queryObject);
			SimpleHTMLFormatter htmlFormater = new SimpleHTMLFormatter(
					"<font color=#ff0000>", "</font>");
			Highlighter highlighter = new Highlighter(htmlFormater, queryScorer);
			for (int i = 0; i < resultCount; i++) {
				int docid = hits[i].doc;
				Document doc = searcher.doc(docid);
				float score = hits[i].score;
				String title = (doc.getFieldable("storeName") != null) ? doc
						.getFieldable("storeName").stringValue() : "";
				String content = (doc.getFieldable("storeDesc") != null) ? doc
						.getFieldable("storeDesc").stringValue() : "";
				String author = (doc.getFieldable("managerName") != null) ? doc
						.getFieldable("managerName").stringValue() : "";
				String pubtime = (doc.getFieldable("address") != null) ? doc
						.getFieldable("address").stringValue() : "";
				String id = (doc.getFieldable("id") != null) ? doc
						.getFieldable("id").stringValue() : "";

				String titleshighlighted = highlighter.getBestFragment(
						analyzer, "storeName", title);
				if (titleshighlighted == null)
					titleshighlighted = title;
				String contenthighlighted = highlighter.getBestFragment(
						analyzer, "address", content);
				if ( !id.equalsIgnoreCase("")){
					long storeid= Long.parseLong(id);
					Store store = storeDao.getStore(storeid);
					stores.add(store);
				}
			}
			
			JSONObject data = this.getJSONObject(stores);
			PrintWriter out = resp.getWriter();  
			out.println(data.toJSONString());
			indexReader.close();
		}//end result count >0
		else {
			logger.info("No results for th search" );
		}//end result count < 0
		
	}// reader not null
		

  }//end searchusinglucene
  */
  /*
  public void init() throws ServletException {
      String rootPath = getServletContext().getRealPath( "/" );
      GaeVFS.setRootPath(rootPath);
  }*/
  
  private void createBackendTask() {
	  	com.google.appengine.api.taskqueue.Queue queue = QueueFactory.getQueue("StoreIndexQueue");
	  	TaskOptions taskOptions = TaskOptions.Builder.withUrl("/store")
	  							   .param("action", "indexall")
	  	                          .header("Host", BackendServiceFactory.getBackendService().getBackendAddress("store-index-backend"))
	  	                          .method(Method.POST);
	  	queue.add(taskOptions);
	  }
   
	/**
	 * Redirect the call to doDelete or doPut method
	 */
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
    String action = req.getParameter("action");
    PrintWriter out  = resp.getWriter();
    /*Enumeration paramNames = req.getParameterNames();
    while(paramNames.hasMoreElements()){
    	String paramName = paramNames.nextElement().toString();
    	String value = req.getParameter(paramName);
    	out.println("Parameter Name :" +paramName);
    	out.println("paramter value:" + req.getParameter(paramName));
    	 
    }*/
     if (action != null) {
		if (action.equalsIgnoreCase("delete")) {
    		 doDelete(req, resp);
    		 return;
    	 } else if (action.equalsIgnoreCase("put")) {
    		 doPut(req, resp);
    		 return;
    	 }
    	 else if ( action.equalsIgnoreCase("search")){
    		 doSearch(req, resp);
    		 return;
    	 }else if ( action.equalsIgnoreCase("index")){
    		 this.createBackendTask();
    		 out.println("Created a Backend Task to Index all stores");
    	 }
    	 else if ( action.equalsIgnoreCase("searchasyouType")){
    		 this.searchasyouType(req, resp);
    	 }
    	 else if ( action.equalsIgnoreCase("searchbyAddress")){
    		 this.searchByAddress(req,resp);
    	 }
    	 else if (action.equalsIgnoreCase("searchByAddressFields")){
    		 this.searchByAddressFields(req, resp);
    	 }
    	 else if ( action.equalsIgnoreCase("indexall")){
    		 this.processIndex();
    	 }
    	 else if ( action.equalsIgnoreCase("makeStoreCategory")){
    		 this.makeStoreCategory(req, resp);
    		 
    	 }else if ( action.equalsIgnoreCase("setStoreForOrder")){
    		 this.setStoreForOrder(req,resp);
    	 }else if (action.equalsIgnoreCase("getSelectedStoreFromSession")){
    		 this.getSelectedStoreFromSession(req, resp);
    	 }
    	 else if ( action.equalsIgnoreCase("searchusinglucene")){
    		/* try {
    	//		 searchusingLucene(req, resp);
    		 } catch (InvalidTokenOffsetsException e) {
    			 logger.warning("Exception:"+e.getMessage());
    			 e.printStackTrace();
    			 
    		 } catch (ParseException e) {
    			 logger.warning("Exception:"+e.getMessage());
    			 e.printStackTrace();
    		 }*/
    	 }
     }
  }

  	private void setStoreForOrder(HttpServletRequest req,
			HttpServletResponse resp)throws ServletException, IOException {
  	  String storeid = req.getParameter("selectedStore");
  	  if ( storeid != null){
  		  
  		  req.getSession().setAttribute("SELECTEDSTORE", storeid);
  		  long id = new Long(storeid).longValue();
  		  Store store=   DAOFactory.getStoreDAOInstance().getStore(id);
  		  req.getSession().setAttribute("STORE", store);
  	  
  	  }
  	}
  	private void getSelectedStoreFromSession(HttpServletRequest req,
			HttpServletResponse resp)throws ServletException, IOException {
  		Store store = (Store)req.getSession().getAttribute("STORE");
		PrintWriter out = resp.getWriter();
		if (store != null) {
			out.println(store.getJSONObject().toJSONString());
		}
  	}
  	
   private void searchByAddressFields(HttpServletRequest req,
			HttpServletResponse resp)throws ServletException, IOException {
	  
	 Map<String, String> searchParams  = 	new HashMap<String,String>();
	 String addressline1  = req.getParameter("addressline1");
	 String addressline2 =  req.getParameter("addressline2");
	 String city = req.getParameter("city");
	 String state = req.getParameter("state");
	 searchParams.put("addressline2", addressline2);
	 searchParams.put("city", city);
	 searchParams.put("state", state);

	 
	 if (addressline1 != null && !addressline1.trim().equalsIgnoreCase("") ){
		 searchParams.put("addressline1", addressline1);
	 }
	 
	 
	 if (searchParams.size() > 0){
		 StoreDAO storeDAO  = DAOFactory.getStoreDAOInstance();
		 List<Store> storeList = storeDAO.getStoresByAddressFields(searchParams);
		 if ( storeList != null ){
				JSONObject data  = this.getJSONObject(storeList);
				PrintWriter out = resp.getWriter();  
				out.println(data.toJSONString());
		}else {
				logger.info("StoreServlet :searchByAddressFields no results");
		}
	 }else{
			logger.info("StoreServlet :searchByAddressFields Parameters are null");
	 }
	 
	}
  
	private void searchByAddress(HttpServletRequest req,
			HttpServletResponse resp)throws ServletException, IOException {
		String lat = req.getParameter("latitude");
		String lon = req.getParameter("longitude");
		String distance = req.getParameter("distance");
		
		if (lat != null && lon != null && distance != null){
			double latitude = Double.valueOf(lat);
			double longitude = Double.valueOf(lon);
			int iDistance = Integer.valueOf(distance); 
			/*
			 * Distance is in Km whereas geocell requires it in meters
			 */
			iDistance = iDistance * 1000; // converting to km;
			StoreDAO storeDAO  = DAOFactory.getStoreDAOInstance();
			List<Store> storeList  = storeDAO.getStoresNearLocation(latitude, longitude, iDistance);
			if ( storeList != null ){
				JSONObject data  = this.getJSONObject(storeList);
				PrintWriter out = resp.getWriter();  
				out.println(data.toJSONString());
			}else {
				logger.info("StoreServlet :SearchbyAddress no results");
			}
		}//end parameter null check.
		else {
			logger.info("StoreServlet :SearchbyAddress Parameters are null");
		}
			
	}//end searchbyAddress

	private void processIndex() throws CorruptIndexException, IOException {
		// TODO Auto-generated method stub
	    StoreDAO storeDao = DAOFactory.getStoreDAOInstance();
	    storeDao.indexall();
	}
	protected void makeStoreCategory(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		long catID =0,storeID = 0; 
		PrintWriter out = resp.getWriter(); 
		String categoryid = req.getParameter("categoryid");
		String storeid= req.getParameter("storeid");
		if ( categoryid != null) catID = Long.parseLong(categoryid);
		if (storeid != null) storeID = Long.parseLong(storeid);
		StoreDAO storeDAO = DAOFactory.getStoreDAOInstance();
		storeDAO.updateStoreCategory(storeID, catID);
		out.println("Saved Successfully");
		
	}
	
}