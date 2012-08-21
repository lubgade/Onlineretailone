package com.adjointweb.onlineretail.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.adjointweb.onlineretail.Dao.DAOFactory;
import com.adjointweb.onlineretail.Dao.PartyDAO;
import com.adjointweb.onlineretailone.entities.Party;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class PartyServlet extends BaseServlet{
	
	private static final Logger logger = Logger.getLogger(PartyServlet.class.getCanonicalName());
		@Override
	  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
		  throws ServletException, IOException {
		 this.doPost(req, resp);
	 }
	 
		 protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
					throws ServletException, IOException {	
			 
		 }
		 protected void doSearch(HttpServletRequest req, HttpServletResponse resp)
					throws ServletException, IOException {	
			 
		 }
		
	  protected void doPut(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
	  logger.log(Level.INFO, "Creating Party");
		PrintWriter out = resp.getWriter();  
		UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();	
	    if ( user == null){
	    	resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
	    }else{
	  	  String jstring = req.getParameter("JSON");
		  Party party = Party.getPartyFromjsonstring(jstring);
		  party.setUserId(user.getEmail());
		  PartyDAO partyDao = DAOFactory.getPartyDAO();
		  partyDao.createParty(party);
		  out.println("Saved Successfully");
	    }
		
	}
	 
	  /**
		 * Redirect the call to doDelete or doPut method
		 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
		UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();	
	    if ( user == null){
	    	resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
	    }else{
	    	 PartyDAO partyDao = DAOFactory.getPartyDAO();
	    	 Party party =  partyDao.getParty(user.getEmail());
	    	 if ( party == null){
	    		 logger.info("Ask User to create a Profile");
	    		 resp.sendRedirect("/profilepage.jsp");
	    	 }
	    }
	    
		
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
	  } //end if action is not null
	}
	  
}
