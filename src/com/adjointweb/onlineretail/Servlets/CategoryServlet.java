package com.adjointweb.onlineretail.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.adjointweb.onlineretail.Dao.CategoryDAO;
import com.adjointweb.onlineretail.Dao.DAOFactory;
import com.adjointweb.onlineretailone.entities.Category;
import com.adjointweb.onlineretailone.entities.Product;
import com.adjointweb.onlineretailone.entities.SubCategory;
import com.google.appengine.api.datastore.Key;
import com.liferay.util.ParamUtil;

import flexjson.JSONSerializer;



@SuppressWarnings("serial")
public class CategoryServlet extends BaseServlet{
	
	private static final Logger logger = Logger.getLogger(CategoryServlet.class.getCanonicalName());

	/**
	 * Get the requested Category entities in JSON format
	 */
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

    super.doGet(req, resp);
    logger.log(Level.INFO, "Obtaining category listing");
    doPost(req,resp);
    return;
  }
  
  
  //adding category
  protected void addProductstoCategory(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	  logger.log(Level.INFO, "Adding Products to Category");
		PrintWriter out = resp.getWriter();  

	 String categoryId  =  req.getParameter("id");
	 String productIds =  req.getParameter("productids");
	 String[] productIDs = productIds.split(",");
	 
	 
	 logger.info("categoryid:"+categoryId + "Productids:"+productIDs);
	 
	CategoryDAO categoryDao = DAOFactory.getCategoryDAOInstance();
	 categoryDao.addProducts(categoryId, productIDs);
	  out.println("Saved Successfully");
	  
  }

  protected void getProducts(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	  logger.log(Level.INFO, "Adding Products to Category");
		PrintWriter out = resp.getWriter();  
		JSONArray searchResult = new JSONArray();
		JSONObject obj =  new JSONObject();

	 String categoryId  =  req.getParameter("id");
	 if ( categoryId == null){
		 obj.put("data",searchResult );
		 out.print(obj.toJSONString());
		 return;
	 }
	 Long id = Long.valueOf(categoryId);
	CategoryDAO categoryDao = DAOFactory.getCategoryDAOInstance();
	List<Product> products  = categoryDao.getProducts(id);
	if ( products != null && !products.isEmpty()){
		Iterator<Product> itProducts  = products.iterator();
		while(itProducts.hasNext()){
			Product product  = (Product)itProducts.next();
			searchResult.add(product.getJSONObject());
		}
	}
	obj.put("data",searchResult );
	out.print(obj.toJSONString());
	  
} 

  protected void getChildCategories(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter out = resp.getWriter(); 
		List<Category> data = new ArrayList<Category>();
		JSONSerializer serializer = new JSONSerializer();

		
	 String categoryId  =  req.getParameter("id");
	 if ( categoryId == null || "null".equals(categoryId)){
			out.print(serializer.exclude("*.class","childCategories.key","*.key","JSONObject").serialize(data));
		 return;
	 }
	// Long id = Long.valueOf(categoryId);
	CategoryDAO categoryDao = DAOFactory.getCategoryDAOInstance();
	
	List<Category> categories  = categoryDao.getChildCategories(Long.valueOf(categoryId));
	if ( categories != null && !categories.isEmpty()){
		Iterator<Category> itCats  = categories.iterator();
		while(itCats.hasNext()){
		Category category = itCats.next();
		 /* if ( category.getCategoryIds().remove(id)){
			  logger.info("Removed1:"+ id);
		  }*/
		  data.add(category);
		}
	}
	String categoriesJSon  = serializer.exclude("*.class","childCategories.key","*.key","JSONObject").serialize(data);
	logger.info("data:" + categoriesJSon );
	out.print(categoriesJSon);
	  
}  
  
  

  
  
  protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
  logger.log(Level.INFO, "Creating Category");
	PrintWriter out = resp.getWriter();  
	
  String jstring = req.getParameter("JSON");
  if ( jstring != null){
	  logger.info("JSON" + jstring);
	  
	  Category category = Category.getCategoryfromjson(jstring);
	  CategoryDAO categoryDao = DAOFactory.getCategoryDAOInstance();
	  Key key = category.getKey();
	  if ( key == null){
	  
		  categoryDao.addCategory(category);
	  }
	  else {
		  categoryDao.updateCategory(category);
	  }
	  out.println("Saved Successfully");
	  logger.info("Category"+ category.getCategoryName());

  }
}
  
  @SuppressWarnings("unchecked")
  protected void doSearch(HttpServletRequest req, HttpServletResponse resp)
  			throws ServletException, IOException {
  	  	//resp.setContentType("application/json; charset=utf-8");
  		//resp.setHeader("Cache-Control", "no-cache");
		JSONSerializer serializer = new JSONSerializer();
  		PrintWriter out = resp.getWriter();  
  		String jsonstring = "{data:{}}";
  		String query = req.getParameter("query");
  		CategoryDAO CategoryDao = DAOFactory.getCategoryDAOInstance();
  		List <Category> categories = new ArrayList<Category>();
  		if ( query != null){
  			if (query.trim().equalsIgnoreCase("")){
  				 categories = CategoryDao.getCategories();
  			}else {
  			  categories = CategoryDao.getCategories(query);
  			}	
  			
  		}
  			
  		else{
  			categories=CategoryDao.getCategories();
  			
  		}
  		String categoriesJSon  = "" ; //serializer.exclude("*.class","*.key").include("subcategories","childCategories").serialize(categories);
  		StringBuffer buffer = new StringBuffer();
  		Iterator<Category> itCategory = categories.iterator();
  		while(itCategory.hasNext()){
  			Category category = itCategory.next();
  			buffer.append(category.toJSON()).append(",");
  		}
  		buffer.deleteCharAt(buffer.lastIndexOf(","));
  		categoriesJSon = "[" + buffer.toString() +"]";
  		
  		logger.info("data:" + categoriesJSon );
  		if ( categoriesJSon != null  && !categoriesJSon.isEmpty() && !categoriesJSon.equalsIgnoreCase("{}") ){
  			jsonstring =  "{\"data\":" +categoriesJSon  + "}";
  		}
  	
  		out.println(jsonstring);

 		
  		/*
  			Iterator<Category> categoryIt = categories.iterator();
	  		while(categoryIt.hasNext()){
	  			Category category = (Category) categoryIt.next();
	  			JSONObject jsonObject = category.getJSONObject();
	  			List<SubCategory> subcategories =  category.getCategories();
	  			JSONArray subArray = new JSONArray();
	  			if ( subcategories != null) {
	  			Iterator<SubCategory> itSub = subcategories.iterator();
	  			
	  			while(itSub.hasNext()){
	  				SubCategory subcat = (SubCategory)itSub.next();
	  				subArray.add(subcat.getJSONObject());
	  			}
	  			jsonObject.put("subcategories", subArray);
	  			categoryjson.add( jsonObject);
	  		}
	  		}
		*/
  		
  		

  	}
  protected void addcategories(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter out = resp.getWriter();  
		 String categoryId  =  req.getParameter("id");
		 String categoryIds =  req.getParameter("categoriesid");
		 String[] categoryIDs = categoryIds.split(",");
		 
		 
		 logger.info("categoryid:"+categoryId + "categoryIds:"+categoryIDs);
		 
		CategoryDAO categoryDao = DAOFactory.getCategoryDAOInstance();
		 categoryDao.addCategories(categoryId, categoryIDs);
		  out.println("Saved Successfully");
		
 
  }
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	  
		PrintWriter out = resp.getWriter();  
		long catId = 0;
	  String id = req.getParameter("id");
	  Long catlid = Long.parseLong(id);
	  if (catlid != null){
	  catId = catlid.longValue();
	  }
	  CategoryDAO CategoryDao = DAOFactory.getCategoryDAOInstance();
      CategoryDao.deleteCategory(catId);
      
 	out.println("Category Deleted successfully");

	  
  }
  /**
	 * Redirect the call to doDelete or doPut method
	 */
protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
  String action = req.getParameter("action");
  
  /*Enumeration paramNames = req.getParameterNames();
  while(paramNames.hasMoreElements()){
  	String paramName = paramNames.nextElement().toString();
  	String value = req.getParameter(paramName);
  	out.println("Parameter Name :" +paramName);
  	out.println("paramter value:" + req.getParameter(paramName));
  	 
  }*/
   if (action != null){
  	if (action.equalsIgnoreCase("delete")) {
    doDelete(req, resp);
    return;
  } else if (action.equalsIgnoreCase("put")) {
     doPut(req, resp);
    return;
  }
  else if ( action.equalsIgnoreCase("addproducts")){
	  this.addProductstoCategory(req, resp);
	  return;
  }else if ( action.equalsIgnoreCase("getproducts")){
	  this.getProducts(req, resp);
	  return;
  }else if ( action.equalsIgnoreCase("addcategories")){
	  this.addcategories(req,resp);
  }else if ( action.equalsIgnoreCase("getChildCategories")){
	  this.getChildCategories(req, resp);
  }
  else if ( action.equalsIgnoreCase("getCategory")){
	  this.getCategory(req, resp);
  }
  	
  else if ( action.equalsIgnoreCase("search")){
    doSearch(req, resp);
  }
  }
  }
protected void getCategory(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
	  long catId = 0;
	  CategoryDAO categoryDao = DAOFactory.getCategoryDAOInstance();
	  PrintWriter out = resp.getWriter();  
	   String data = "{data:{}}";
	  String id = req.getParameter("id");
	  if ( id != null && !("".equalsIgnoreCase(id))){
		  
			  Long catlid = Long.parseLong(id);
			  if (catlid != null){
				  catId = catlid.longValue();
			  }
		
			  Category catdb = categoryDao.getCategory(catId);
			  if ( catdb != null){
				    data  = "{\"data\":"+catdb.toJSON() + "}";
				/*  data.put("data",catdb.getJSONObject());*/
			  }
			  out.println(data);
		}

}


}
