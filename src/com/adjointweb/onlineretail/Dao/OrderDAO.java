       package com.adjointweb.onlineretail.Dao;

import java.util.List;

import com.adjointweb.onlineretailone.entities.Order;

public interface OrderDAO {
	void createOrder(Order order);
	void updateOrder(Order order);
	List<Order> getOrders(Long storeId);
	void updateOrderStatus(String orderNumber, String orderStatus);

}
