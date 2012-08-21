package com.cloudjini.onlineretail.Servlets;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class OnlineRetailContextListener implements ServletContextListener {
	
		  private static final Logger logger = Logger.getLogger(OnlineRetailContextListener.class.getName());
		  public void contextInitialized(ServletContextEvent sce) {
		    logger.log(Level.INFO, "Loading request occuring.");
		  }

		  public void contextDestroyed(ServletContextEvent sce) {
			    logger.log(Level.INFO, "Context Destroyed");

		  }
		  
}
