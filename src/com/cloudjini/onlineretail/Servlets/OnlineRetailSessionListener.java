package com.cloudjini.onlineretail.Servlets;

import java.util.logging.Logger;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.cloudjini.onlineretail.Dao.DAOFactory;
import com.cloudjini.onlineretail.Dao.ShoppingCartDAO;

public class OnlineRetailSessionListener implements HttpSessionListener {
	private static final Logger logger = Logger.getLogger(OnlineRetailSessionListener.class.getCanonicalName());

	public void sessionCreated(HttpSessionEvent arg0) {
		// TODO Auto-generated method stub
		logger.info("session created for user");

	}

	public void sessionDestroyed(HttpSessionEvent arg0) {
		// TODO Auto-generated method stub
		
		  ShoppingCartDAO cartDao = DAOFactory.getShoppingCartDAO();
		  cartDao.deleteShoppingCart(		arg0.getSession().getId());
		  logger.info("Cart deleted for session");
		

	}

}
