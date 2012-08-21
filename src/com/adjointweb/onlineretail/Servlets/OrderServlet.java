/*
 * Author: Leena Ghanekar
 */

package com.adjointweb.onlineretail.Servlets;


import java.io.IOException;
import java.io.PrintWriter;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//import net.tanesha.recaptcha.ReCaptchaImpl;

import com.adjointweb.onlineretail.Dao.DAOFactory;
import com.adjointweb.onlineretail.Dao.OrderDAO;
import com.adjointweb.onlineretail.Dao.PartyDAO;
import com.adjointweb.onlineretail.Dao.ShoppingCartDAO;
import com.adjointweb.onlineretail.Dao.ShoppingListDAO;
import com.cloudjini.onlineretailone.entities.Order;
import com.cloudjini.onlineretailone.entities.OrderLineItem;
import com.cloudjini.onlineretailone.entities.OrderNumberGenerator;
import com.cloudjini.onlineretailone.entities.Party;
import com.cloudjini.onlineretailone.entities.ShoppingCart;
import com.cloudjini.onlineretailone.entities.ShoppingCartItem;
import com.cloudjini.onlineretailone.entities.ShoppingList;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

@SuppressWarnings("serial")
public class OrderServlet extends BaseServlet{
	private static final Logger logger = Logger.getLogger(ShoppingCartServlet.class.getCanonicalName());

	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

    super.doGet(req, resp);
    logger.log(Level.INFO, "Obtaining Order");
    checkLogin(req,resp);
    doPost(req,resp);
    return;
  }
	protected void checkLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException{
		HttpSession session = req.getSession();
	      String userName = (String) session.getAttribute("USER_NAME");
	      if (userName == null){
		  	UserService userService = UserServiceFactory.getUserService();
		    User user = userService.getCurrentUser();
		     if (user != null) {
		         session.setAttribute("USER_NAME", user.getNickname());
		         session.setAttribute("USER_ID",user.getUserId());
		        
		        RequestDispatcher disp =  req.getRequestDispatcher("/placeorder.jsp");
		        disp.forward(req,resp);
		         
		     } else {
		         resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
		     }	     
	      }
	      else
	      {
	    	  RequestDispatcher disp =  req.getRequestDispatcher("/placeorder.jsp");
		        disp.forward(req,resp);
	      }
	}
	
	
	protected void getparty(HttpServletRequest req, HttpServletResponse resp) throws IOException{
			
			PartyDAO partyDao = DAOFactory.getPartyDAOInstance();
			HttpSession session = req.getSession();
			PrintWriter out = resp.getWriter();

			Party party = partyDao.getParty(session.getAttribute("USER_ID").toString());
			if( party != null ){
				out.println(party.toJSON());
			}else {
				out.println("{}");
			}						
	}
	
	
	protected void createparty(HttpServletRequest req, HttpServletResponse resp) throws IOException{
	        String jstring = req.getParameter("JSON");
	        logger.info(jstring);
			HttpSession session = req.getSession();

	        Party party = Party.getPartyfromjsonString(jstring);
	        party.setUserId(session.getAttribute("USER_ID").toString());
			PartyDAO partyDao = DAOFactory.getPartyDAOInstance();
			partyDao.createParty(party);
		
		
		
	}
	
	protected void createorder(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException{
		String jstring = req.getParameter("JSON");
		HttpSession session = req.getSession();
		PrintWriter out  = resp.getWriter();
		 String remoteAddr = req.getRemoteAddr();
	        ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
	        reCaptcha.setPrivateKey("6LdFP9ASAAAAAK3N8aaBWqdl5pZHonWmzXFiOg8d");
	        String challenge = req.getParameter("recaptcha_challenge_field");
	        String uresponse = req.getParameter("recaptcha_response_field");
	        
	        if ( challenge == null || uresponse ==  null ){
	        	resp.sendError(resp.SC_BAD_REQUEST, "RECAPCHA_VALIDATION_FAILED");
	        	return;
	        }
	        ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, uresponse);
	        
	    if (reCaptchaResponse.isValid()) {   
	    	Order order = Order.getOrderfromjsonString(jstring);
	    	order.setOrderNumber(OrderNumberGenerator.generateOrderNumber(10000));
	    	String email="";
	    	UserService userService = UserServiceFactory.getUserService();         
	    	User user = userService.getCurrentUser();     
	    	if ( user == null){ 
							resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));         
	    	}
	    	else{
	    		email=user.getEmail();
	    	}
	    	order.setOrderEmailAddress(email);
	    	order.setOrderUserId(user.getUserId());
	    	order.setOrderStatus("Confirmed");
	    	order.setOrderDate(new Date(System.currentTimeMillis()));
	    	order.setOrderStoreId((Long.parseLong((String)session.getAttribute("SELECTEDSTORE"))));
	    	order.setBillFromParty((String)session.getAttribute("SELECTEDSTORE"));
	    	order.setBilltoParty(user.getUserId());
	    	ShoppingCart cart  = (ShoppingCart)session.getAttribute("CART");
	    	setOrderLineItems(order,cart);
		
	    	OrderDAO orderDao = DAOFactory.getOrderDAOInstance();
	    	orderDao.createOrder(order);
	    	ShoppingCartDAO cartDAO = DAOFactory.getShoppingCartDAO();
	    	cartDAO.deleteShoppingCart(user.getUserId());
	    	req.getSession().setAttribute("LATEST_ORDER", order);
	    	/* RequestDispatcher dispatcher = req.getRequestDispatcher("/orderresponse.jsp");
	    	if (dispatcher != null) dispatcher.forward(req, resp);*/
	    	out.println(order.toJSON());
		//resp.sendRedirect("/orderresponse.jsp");
	    }
	        else{
	        	
	        	//out.println("RECAPCHA_VALIDATION_FAILED");
	        	resp.sendError(resp.SC_BAD_REQUEST, "RECAPCHA_VALIDATION_FAILED");
	        }
	}//end create order
	
	
	private void setOrderLineItems(Order aOrder,ShoppingCart aCart){
		  Set<ShoppingCartItem>  sCartItems = aCart.getItems();
		  Iterator<ShoppingCartItem>  itCart = sCartItems.iterator();
		  while(itCart.hasNext()){
			  ShoppingCartItem cartItem = itCart.next();
			  OrderLineItem lineitem = new OrderLineItem();
			  lineitem.setProductId(cartItem.getProductId());
			  lineitem.setAdjustments(cartItem.getAdjustments());
			  lineitem.setOrderedQuantity(cartItem.getQuantity());
			  lineitem.setProductName(cartItem.getProductName());
			  lineitem.setOrderLineItemSubTotal(cartItem.getSubtotal());
			  lineitem.setProductPrice(cartItem.getProductprice());
			 
			  aOrder.addOrderLineItem(lineitem);
		  }

	}
			
	/* protected void doPut(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
	  logger.log(Level.INFO, "Creating Order");
	  PrintWriter out = resp.getWriter();  
		
	  String jstring = req.getParameter("JSON");

	  Order order = Order.getOrderfromjsonString(jstring);
	  UserService userService = UserServiceFactory.getUserService();         
	  User user = userService.getCurrentUser();    
	  order.setOrderUserId(order.getOrderUserId());
	  OrderDAO orderDao = DAOFactory.getOrderDAOInstance();
	  String key = order.getOrderNumber();
	  if ( key == null){
	  
		  orderDao.createOrder(order);
	  }
	  else {
		  logger.log(Level.INFO, "Updating order");
		orderDao.updateOrder(order);
	  }
	  out.println("Saved Successfully");
	}	 */
 
	 protected void doPost(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
		 String action = req.getParameter("action");
		 if (action != null){
			  	if (action.equalsIgnoreCase("getparty")) {
			  		getparty(req, resp);
			  		return;
			  	}
			  	if (action.equalsIgnoreCase("createparty")){
			  		createparty(req, resp);
			  		return;
			  	}
			  	if (action.equalsIgnoreCase("createorder")){
			  		createorder(req,resp);
			  	}
			  	}
		 	 }
	 }
	 
	 
	 
	 

