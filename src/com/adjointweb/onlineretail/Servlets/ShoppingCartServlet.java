package com.adjointweb.onlineretail.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.adjointweb.onlineretail.Dao.DAOFactory;
import com.adjointweb.onlineretail.Dao.ShoppingCartDAO;
import com.adjointweb.onlineretailone.entities.ShoppingCart;
import com.adjointweb.onlineretailone.entities.ShoppingCartItem;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class ShoppingCartServlet extends BaseServlet {
	private static final Logger logger = Logger.getLogger(ShoppingCartServlet.class.getCanonicalName());
	
	
	 protected void doGet(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {

	    super.doGet(req, resp);
	    logger.log(Level.INFO, "Obtaining Cart");
	    doPost(req,resp);
	    return;
	  }
	 
	 
	//add items to the cart or update
	 
	 protected void doPut(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
	  logger.log(Level.INFO, "Creating ShoppingCart");
	  PrintWriter out = resp.getWriter();  
	  String jstring = req.getParameter("JSON");
	  logger.info("JSON String:"+ jstring);
	  ShoppingCart cart = ShoppingCart.getShoppingCartfromjsonString(jstring);
	  Set<ShoppingCartItem>  sCartItems = cart.getItems();
	  Iterator<ShoppingCartItem>  itCart = sCartItems.iterator();
	  while(itCart.hasNext()){
		logger.info(" Shopping Cart productid:"+ itCart.next().getProductId());
	  }
	  UserService userService = UserServiceFactory.getUserService();         
	  User user = userService.getCurrentUser();   
	  if ( user != null){
		  cart.setUserId (user.getUserId());
		  ShoppingCartDAO cartDao = DAOFactory.getShoppingCartDAO();
		  cartDao.createShoppingCart(cart);	 
		  out.println("Saved Successfully");
	  }else {
		  String userName =  null;
		  Cookie[] cookies = req.getCookies();
		  if ( cookies.length > 0){
			 for (Cookie cok : cookies){
				 if ( cok.getName().equalsIgnoreCase("UserName")){
					 userName =  cok.getValue();
				     logger.info("User Name:"+ userName);
				 }
			 }
		  }
		  if (userName == null){
			  Cookie cUserName = new Cookie("UserName",req.getSession().getId());
			  cUserName.setMaxAge(3600*24*2);
			  resp.addCookie(cUserName);
			  cart.setUserId(req.getSession().getId());
		  }else {
			  cart.setUserId(userName);
		  }
		  ShoppingCartDAO cartDao = DAOFactory.getShoppingCartDAO();
		  cartDao.createShoppingCart(cart);	
	  }
}
	 
	 protected void doPost(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
		 String action = req.getParameter("action");
		 if (action != null){
			  	if (action.equalsIgnoreCase("getcart")) {
			    getcart(req, resp);
			    return;
			  } else if (action.equalsIgnoreCase("put")) {
			    doPut(req, resp);
			    return;
			  }
		 	 }
	 }


	private void getcart(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		 ShoppingCart cart = null;
		 UserService userService = UserServiceFactory.getUserService();         
		 User user = userService.getCurrentUser();    
		 String userID ="Anonymous" ;
		  if ( user == null){
		        // resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
			  Cookie[] cookies = req.getCookies();
			  String userName = null;
			  if (cookies != null){
				  if ( cookies.length > 0){
					 for (Cookie cok : cookies){
						 if ( cok.getName().equalsIgnoreCase("UserName")){
							 userName =  cok.getValue();
						     logger.info("User Name:"+ userName);
						 }
					 }
				  }
			      userID= userName;
			      
			  }  
		  }else {
			  userID =  user.getUserId();
		  }
		  if ( userID == null || userID.equalsIgnoreCase("Anonymous")  ) {
			  return;
		  }
		  ShoppingCartDAO cartDao = DAOFactory.getShoppingCartDAO();
		  cart=cartDao.getShoppingCart(userID);
		  if( cart != null){
			  HttpSession session = req.getSession();
			  session.setAttribute("CART",cart);
			  Set<ShoppingCartItem>  sCartItems = cart.getItems();
			  Iterator<ShoppingCartItem>  itCart = sCartItems.iterator();
			  while(itCart.hasNext()){
				logger.info(" Shopping Cart Object:"+ itCart.next().getProductName());
			  }
			  PrintWriter out = resp.getWriter();
			  out.println(cart.toJSON());
		}		
	}
}
