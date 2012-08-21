package com.cloudjini.onlineretail.Servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


import java.util.UUID;
import java.util.logging.Logger;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
@SuppressWarnings("serial")
public class FacebookLogin extends BaseServlet {
	private static final Logger logger = Logger.getLogger(FacebookLogin.class.getCanonicalName());

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//	super.doGet(req, resp);
		logger.info("Inside do Get");
		PrintWriter out = resp.getWriter();
		String app_id = "224808994230016";
		String app_secret = "7c98deeff3b736a4b19184f8e35b54c0";
		String my_url = "http://localhost:8888/faceBookLogin";
		String code =  req.getParameter("code");
		logger.info("Inside do Get My_URL"+my_url);

		if ( code == null ||  code.length() <= 0){
			req.getSession().setAttribute("state", UUID.randomUUID().toString());	
			String myurl = URLEncoder.encode(my_url, "UTF-8");
			logger.info("Inside do Get"+myurl);
			String url = "https://www.facebook.com/dialog/oauth?client_id="+ app_id+ "&redirect_uri="+ myurl + "&state=" + req.getSession().getAttribute("state");
			resp.sendRedirect(url);
			return;
		}
		String statefrreq =  req.getParameter("state");
		String statefromsession = (String) req.getSession().getAttribute("state");
		if ( statefrreq != null) logger.info("State from req:"+statefrreq);
		if ( statefromsession != null) logger.info("State from session:"+statefromsession);
		if(statefromsession.equalsIgnoreCase(statefrreq)) {
			 String myurl = URLEncoder.encode(my_url, "UTF-8");
		     String token_url = "https://graph.facebook.com/oauth/access_token?" + "client_id="+app_id + "&redirect_uri=" +myurl + "&client_secret=" + app_secret + "&code=" + code ;
			 URL url = new URL(token_url);
			 logger.info("Inside do Get Opening graph with url :"+token_url);
			 String access_token= "";
			 StringBuffer data  = new StringBuffer();
			 String inputLine;
			 BufferedReader inbr = new BufferedReader(
                     new InputStreamReader(url.openStream()));
			 while(( inputLine = inbr.readLine()) != null){
				 data.append(inputLine);
			 }
			 String[] params = data.toString().split("=");
			 if ( params.length > 1) {
				 access_token = params[1];
				 logger.info(access_token + " using stream " ); 
			 }
			 
			 String graph_url = "https://graph.facebook.com/me?access_token=" +access_token;
			 URL graphurl = new URL(graph_url);
			 
			 BufferedReader in = new BufferedReader(
                     new InputStreamReader(graphurl.openStream()));
			 
			 StringBuffer jsonData  = new StringBuffer();
			 while(( inputLine = in.readLine()) != null){
				 jsonData.append(inputLine);
			 }
			 JSONParser parser = new JSONParser();
			 JSONObject user;
			try {
				user = (JSONObject) parser.parse(jsonData.toString());
				 String name = (String) user.get("name");
				 String firstName = (String) user.get("first_name");
				 String lastName = (String) user.get("last_name");
				 String username = (String) user.get("username");
				 String birthday = (String) user.get("birthday");
				 String gender  = (String) user.get("gender");
				 String email  = (String)user.get("email");
				 String id = (String)user.get("id");
				 String link  =(String)user.get("link");
				 Boolean verified =   (Boolean)user.get("verified");
				 out.println("Welcome, " + name);
				
			} catch (org.json.simple.parser.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
		   }
		   else {
			   out.println("The state does not match. You may be a victim of CSRF.");
		   }
		
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
		
	}

}
