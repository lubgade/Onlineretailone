package com.adjointweb.onlineretail.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.adjointweb.onlineretail.Dao.DAOFactory;
import com.adjointweb.onlineretail.Dao.ProductDAO;
import com.adjointweb.onlineretail.Dao.SearchDAO;
import com.adjointweb.onlineretailone.entities.Product;


@SuppressWarnings("serial")
public class ProductServlet extends BaseServlet{
	
	private static final Logger logger = Logger.getLogger(ProductServlet.class.getCanonicalName());
	private static final int PAGE_SIZE = 5;

	/**
	 * Get the requested product entities in JSON format
	 */
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

    super.doGet(req, resp);
    logger.log(Level.INFO, "Obtaining product listing");
    doPost(req,resp);
    return;
  }
  
 //adding product
  
  protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
  logger.log(Level.INFO, "Creating Product");
	PrintWriter out = resp.getWriter();  
	
  String jstring = req.getParameter("JSON");

  Product product = Product.getProductfromjsonString(jstring);
  ProductDAO productDao = DAOFactory.getProductDAOInstance();
  Long key = product.getId();
  if ( key == null){
  
	  productDao.addProduct(product);
  }
  else {
	  productDao.updateProduct(product);
  }
  out.println("Saved Successfully");
}
  
  @SuppressWarnings("unchecked")
  protected void doSearch(HttpServletRequest req, HttpServletResponse resp)
  			throws ServletException, IOException {
  	  	
  		String query = req.getParameter("query");
  		ProductDAO ProductDao = DAOFactory.getProductDAOInstance();
  		List <Product> products = new ArrayList<Product>();
  		JSONObject data = new JSONObject();
		JSONArray productjson = new JSONArray();
  		if ( query != null){
  			if (query.trim().equalsIgnoreCase("")){
  				 products = ProductDao.getProducts();
  			}else {
  			  products = ProductDao.getProducts(query);
  			}	
  			
  		}
  			
  		else{
  			products=ProductDao.getProducts();
  			
  		}
  		
 		
  		
  			Iterator<Product> productIt = products.iterator();
	  		while(productIt.hasNext()){
	  			Product product = (Product) productIt.next();
	  			JSONObject jsonObject = product.getJSONObject();
	  			
	  			productjson.add( jsonObject);
	  		}
		
  		PrintWriter out = resp.getWriter();  
  		data.put("data", productjson);
  		out.println(data.toJSONString());

  	}
  
  @SuppressWarnings("unchecked")
  protected void getProductPage(HttpServletRequest req, HttpServletResponse resp)
  			throws ServletException,IOException {

  	  	Map<String,Object> productPage = (Map) req.getSession().getAttribute("PRODUCT_PAGE");
  	    Long categoryId = 0l;
  	  	if ( productPage == null) productPage = new HashMap();
  		String pageNumbers = req.getParameter("PAGE_NUMBER");
  		String page  = req.getParameter("PAGE");
  		String sCategoryId = req.getParameter("categoryId");
  		if (sCategoryId != null && !sCategoryId.isEmpty() && !sCategoryId.equalsIgnoreCase("")){
  			 categoryId = Long.parseLong(sCategoryId);
  	 	}
  		productPage.put("PAGE", page);
  		int pageNumber =  Integer.parseInt(pageNumbers);
  		ProductDAO productDao = DAOFactory.getProductDAOInstance();
  		List <Product> products = new ArrayList<Product>();
  	//	productPage = productDao.getProducts(pageNumber, productPage);
  		Long productId = null;
  		if (page.equalsIgnoreCase("NEXT") ){
  			productId  = (Long) req.getSession().getAttribute("NEXT_PRODUCT_ID");
  		}else {
  	  		productId  = (Long) req.getSession().getAttribute(pageNumbers+"PREVIOUS_PRODUCT_ID");
  		}
  		if ( productId == null || pageNumber ==1 ){
  		   productId = new Long(1);
  		}else {
  		 req.getSession().setAttribute(pageNumbers+"PREVIOUS_PRODUCT_ID", productId);
  		}
  		if ( sCategoryId == null || sCategoryId.isEmpty() || sCategoryId.equalsIgnoreCase("")){
  		products  = productDao.getProducts(productId);
  		} else {
  			products  = productDao.getProducts(categoryId,productId);	
  		}
  		
  		JSONObject data = new JSONObject();
		JSONArray productjson = new JSONArray();
  	//	products = (List<Product>)productPage.get("PRODUCT_LIST");
  		
		Iterator<Product> productIt = products.iterator();
	  	while(productIt.hasNext()){
	  			Product product =  productIt.next();
	  			JSONObject jsonObject = product.getJSONObject();
	  			productjson.add( jsonObject);
		 }
	  	
	  	if ( products.size() > 0 ){
	  		Product product = products.get(products.size() -1);
	  		req.getSession().setAttribute("NEXT_PRODUCT_ID",new Long(product.getId()));
	  	}
	  	
  		PrintWriter out = resp.getWriter();  
  		data.put("data", productjson);
  		if ( products.size() > PAGE_SIZE){
  			data.put("next_page_exists", true);
  			productjson.remove(productjson.size() -1);
  		}else {
  			data.put("next_page_exists", false);
  		}
  		req.getSession().setAttribute("PRODUCT_PAGE", productPage);
  		out.println(data.toJSONString());
  	}
  
  
  @SuppressWarnings("unchecked")
  protected void getProductPageForCategory(HttpServletRequest req, HttpServletResponse resp)
  			throws ServletException,IOException {

  	  	Map<String,Object> productPage = (Map) req.getSession().getAttribute("PRODUCT_PAGE");
  	  	if ( productPage == null) productPage = new HashMap();
  		String pageNumbers = req.getParameter("PAGE_NUMBER");
  		String page  = req.getParameter("PAGE");
  		String sCategoryId = req.getParameter("categoryId");
  		Long categoryId = Long.parseLong(sCategoryId);
  		productPage.put("PAGE", page);
  		int pageNumber =  Integer.parseInt(pageNumbers);
  		ProductDAO productDao = DAOFactory.getProductDAOInstance();
  		List <Product> products = new ArrayList<Product>();
  	//	productPage = productDao.getProducts(pageNumber, productPage);
  		Long productId = null;
  		if (page.equalsIgnoreCase("NEXT") ){
  			productId  = (Long) req.getSession().getAttribute("NEXT_PRODUCT_ID");
  		}else {
  	  		productId  = (Long) req.getSession().getAttribute(pageNumbers+"PREVIOUS_PRODUCT_ID");
  		}
  		if ( productId == null || pageNumber ==1 ){
  		   productId = new Long(1);
  		}else {
  		 req.getSession().setAttribute(pageNumbers+"PREVIOUS_PRODUCT_ID", productId);
  		}
  		products  = productDao.getProducts(categoryId, productId);
  		
  		JSONObject data = new JSONObject();
		JSONArray productjson = new JSONArray();
  	//	products = (List<Product>)productPage.get("PRODUCT_LIST");
  		
		Iterator<Product> productIt = products.iterator();
	  	while(productIt.hasNext()){
	  			Product product =  productIt.next();
	  			JSONObject jsonObject = product.getJSONObject();
	  			productjson.add( jsonObject);
		 }
	  	
	  	if ( products.size() > 0 ){
	  		Product product = products.get(products.size() -1);
	  		req.getSession().setAttribute("NEXT_PRODUCT_ID",new Long(product.getId()));
	  	}
	  	
  		PrintWriter out = resp.getWriter();  
  		data.put("data", productjson);
  		if ( products.size() > PAGE_SIZE){
  			data.put("next_page_exists", true);
  			productjson.remove(productjson.size() -1);
  		}else {
  			data.put("next_page_exists", false);
  		}
  		req.getSession().setAttribute("PRODUCT_PAGE", productPage);
  		out.println(data.toJSONString());
  	}
  

  protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	  
		PrintWriter out = resp.getWriter();  
		long prodId = 0;
	  String id = req.getParameter("id");
	  Long prodtid = Long.parseLong(id);
	  if (prodtid != null){
	  prodId = prodtid.longValue();
	  }
	  ProductDAO ProductDao = DAOFactory.getProductDAOInstance();
    ProductDao.deleteProduct(prodId);
    
	out.println("Product Deleted successfully");

	  
}
  
  /**
 	 * Redirect the call to doDelete or doPut method
 	 */
 protected void doPost(HttpServletRequest req, HttpServletResponse resp)
 			throws ServletException, IOException {
   String action = req.getParameter("action");
   
  
    if (action != null){
   	if (action.equalsIgnoreCase("delete")) {
     doDelete(req, resp);
     return;
   } else if (action.equalsIgnoreCase("put")) {
     doPut(req, resp);
     return;
   }
   else if ( action.equalsIgnoreCase("search")){
    doSearch(req, resp);
   
   }else if ( action.equalsIgnoreCase("getProductPage")){
	   getProductPage(req, resp);
   }
   else if(action.equalsIgnoreCase("searchasutype")){
	   doSearchasutype(req,resp); 
   }
   }
   }

private void doSearchasutype(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	// TODO Auto-generated method stub
	String searchString  = req.getParameter("term");
	SearchDAO  searchDAO = DAOFactory.getSearchDAOInstance();
	logger.info("Search String:"+ searchString );
	JSONArray searchResult = new JSONArray();

	List products = searchDAO.search(searchString, Product.class);
    Iterator itProducts  = products.iterator();
    while(itProducts.hasNext()){
    	Product product  = (Product)itProducts.next();
    	searchResult.add(product.getJSONObject());
    }
	JSONObject obj =  new JSONObject();
	obj.put("data",searchResult );
	logger.info(obj.toJSONString());
	PrintWriter out = resp.getWriter(); 
	out.print(obj.toJSONString());

}

}