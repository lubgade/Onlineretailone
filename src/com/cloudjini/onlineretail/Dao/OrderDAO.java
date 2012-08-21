       package com.cloudjini.onlineretail.Dao;

import java.util.List;

import com.cloudjini.onlineretailone.entities.Order;
/*
 * Author : Leena
 */

public interface OrderDAO {
	void createOrder(Order order);
	void updateOrder(Order order);
	List<Order> getOrders(Long storeId);
	void updateOrderStatus(String orderNumber, String orderStatus);

}
