package com.cloudjini.onlineretail.Servlets;

/*
 * Author:Leena Ubgade
 */
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class BaseServlet extends HttpServlet {


  	
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	  throws ServletException, IOException {

	  /*HttpSession session = req.getSession();
      String userName = (String) session.getAttribute("USER_NAME");
      if (userName == null){
	  	UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	     if (user != null) {
	         session.setAttribute("USER_NAME", user.getNickname());
	     } else {
	         resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
	     }	     
      }*/
    resp.setContentType("application/json; charset=utf-8");
    resp.setHeader("Cache-Control", "no-cache");
  }
}


