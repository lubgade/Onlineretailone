package com.cloudjini.onlineretail.Dao;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOCanRetryException;
import javax.jdo.JDOFatalException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.cloudjini.onlineretailone.entities.Order;
import com.cloudjini.onlineretailone.entities.Product;
import com.google.appengine.api.datastore.DatastoreNeedIndexException;
import com.google.appengine.api.datastore.DatastoreTimeoutException;

public class OrderDAOImpl implements OrderDAO{
	private static final Logger logger = Logger.getLogger(OrderDAOImpl.class.getCanonicalName());

	public void createOrder(Order order) {
		// TODO Auto-generated method stub
		 PersistenceManager pm = PMF.get().getPersistenceManager();
		 Transaction tx  = pm.currentTransaction();
		 try{
				tx.begin();
				pm.makePersistent(order);
				tx.commit();
		 }
		 catch (JDOCanRetryException ex) {
				ex.printStackTrace();
				logger.warning("Error creating order"+order.toString());
				throw ex;
			}
			catch(JDOFatalException fx){
			logger.severe("Error creating order :"+ order.toString());
			throw fx;
			}
		 finally{
			 if (tx.isActive()){
					tx.rollback();
			 }
			 pm.close();
		 }
	}



	public void updateOrder(Order order) {
		// TODO Auto-generated method stub
		 PersistenceManager pm = PMF.get().getPersistenceManager();
		 Transaction tx  = pm.currentTransaction();
		 try{
				tx.begin();
				Order orderdb = (Order)pm.getObjectById(Order.class, order.getOrderNumber());
				orderdb.setOrderUserId(order.getOrderUserId());
				orderdb.setOrderStoreId(order.getOrderStoreId());
				orderdb.setOrderName(order.getOrderName());
				orderdb.setBillFromParty(order.getBillFromParty());
				orderdb.setBilltoParty(order.getBilltoParty());
				orderdb.setOrderAdjustment(order.getOrderAdjustment());
				orderdb.setOrderDate(order.getOrderDate());
				orderdb.setOrderEmailAddress(order.getOrderEmailAddress());
				orderdb.setOrderLineItems(order.getOrderLineItems());
				orderdb.getOrderLineItems().retainAll(order.getOrderLineItems());
				orderdb.setOrderNotes(order.getOrderNotes());
				orderdb.setOrderPaymentInfo(order.getOrderPaymentInfo());
				orderdb.setOrderPaymentType(order.getOrderPaymentType());
				orderdb.setOrderRequestedShipDate(order.getOrderRequestedShipDate());
				orderdb.setOrderRequestedShipTime(order.getOrderRequestedShipTime());
				orderdb.setOrderShippingAddress(order.getOrderShippingAddress());
				orderdb.setOrderShippingMethod(order.getOrderShippingMethod());
				orderdb.setOrderShipStatus(order.getOrderShipStatus());
				orderdb.setOrderTaxtype(order.getOrderTaxtype());
				orderdb.setOrderTotalAmount(order.getOrderTotalAmount());
				orderdb.setOrderTotalDiscount(order.getOrderTotalDiscount());
				orderdb.setOrderType(order.getOrderType());
				tx.commit();
		 }catch (JDOCanRetryException ex) {
				ex.printStackTrace();
				logger.warning("Error updating order  "+ order);
				throw ex;
			}
			catch(JDOFatalException fx){
			logger.severe("Error updating order  :"+ order);
			throw fx;
			}
		 finally{
			 if (tx.isActive()){
					tx.rollback();
			 }
			 pm.close();
		 }
		
	}
	
	



	public List<Order> getOrders(Long storeId) {
		List<Order> orderList = new ArrayList();
		PersistenceManager pm =  PMF.get().getPersistenceManager();
		try{
		Query query = (Query) pm.newQuery(Product.class);
		query.setFilter("orderStoreId == IdParam");
		query.declareParameters("Long IdParam");
		orderList  = (List<Order>) query.execute(storeId);
		return orderList;
		}catch (DatastoreTimeoutException e) {
	    	logger.severe(e.getMessage());
	    	logger.severe("datastore timeout at: " );
		} catch (DatastoreNeedIndexException e) {
			logger.severe(e.getMessage());
			logger.severe("datastore need index exception at: ");
		}finally{
			pm.close();
		}
		return orderList;

	}



	public void updateOrderStatus(String orderNumber, String orderStatus) {
		// TODO Auto-generated method stub
		 PersistenceManager pm = PMF.get().getPersistenceManager();
		 Transaction tx  = pm.currentTransaction();
		 try{
				tx.begin();
				Order orderdb = (Order)pm.getObjectById(Order.class, orderNumber);
				orderdb.setOrderStatus(orderStatus);
				tx.commit();
		 }catch (JDOCanRetryException ex) {
				ex.printStackTrace();
				logger.warning("Error updating order status "+ orderStatus);
				throw ex;
			}
			catch(JDOFatalException fx){
			logger.severe("Error updating order status :"+ orderStatus);
			throw fx;
			}
		 finally{
			 if (tx.isActive()){
					tx.rollback();
			 }
			 pm.close();
		 }
		
	}
	
	

	
}
