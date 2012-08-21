package com.adjointweb.onlineretail.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import sun.awt.datatransfer.DataTransferer.ReencodingInputStream;

import com.adjointweb.onlineretail.Dao.CategoryDAO;
import com.adjointweb.onlineretail.Dao.DAOFactory;
import com.adjointweb.onlineretail.Dao.ShoppingListDAO;
import com.adjointweb.onlineretailone.entities.Category;
import com.adjointweb.onlineretailone.entities.ShoppingList;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class ShoppingListServlet extends BaseServlet{
	
	private static final Logger logger = Logger.getLogger(ShoppingListServlet.class.getCanonicalName());
	
	
	 protected void doGet(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {

	    super.doGet(req, resp);
	    logger.log(Level.INFO, "Obtaining List");
	    doPost(req,resp);
	    return;
	  }

//add items to the list or update
	 
	 protected void doPut(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
	  logger.log(Level.INFO, "Creating ShoppingList");
		PrintWriter out = resp.getWriter();  
		
	  String jstring = req.getParameter("JSON");

	  ShoppingList list = ShoppingList.getShoppingListfromjsonString(jstring);
	  UserService userService = UserServiceFactory.getUserService();         
	  User user = userService.getCurrentUser();    
	  list.setuId (user.getUserId());
	  ShoppingListDAO listDao = DAOFactory.getShoppingListDAOInstance();
	  Key key = list.getkey();
	  if ( key == null){
  
		  listDao.addShoppingList(list);
	  }
	  else {
		  logger.log(Level.INFO, "Updating shoppingList");
		 listDao.updateShoppingList(list);
	  }
	  out.println("Saved Successfully");
	}	 
	 
	
	 //To Delete items
	 
	 protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
		  
			PrintWriter out = resp.getWriter();  
			long listId = 0;
		  String id = req.getParameter("id");
		  Long listlid = Long.parseLong(id);
		  if (listlid != null){
		  listId = listlid.longValue();
		  
		  }
		  ShoppingListDAO ShoppingListDao = DAOFactory.getShoppingListDAOInstance();
	      ShoppingListDao.deleteShoppingList(listId);
	      
	 	out.println("Shopping Deleted successfully");

		  
	  }	 
	 
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
	  }
	  }
	 }

	private void doSearch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// TODO Auto-generated method stub
		String query = req.getParameter("query");
		if ( query == null){
			UserService userService = UserServiceFactory.getUserService();         
			User user = userService.getCurrentUser();    
			List<ShoppingList> sList = DAOFactory.getShoppingListDAOInstance().getShoppingLists(user.getUserId());
			Iterator<ShoppingList> itList  = sList.iterator();
			JSONObject data = new JSONObject();
			JSONArray sListArray = new JSONArray(); 
			while(itList.hasNext()){
				sListArray.add(itList.next().getJSONObject());
			}
			data.put("lists", sListArray);
			PrintWriter out = resp.getWriter();
			out.println(data.toJSONString());
		}
		
		
		
	}
}
