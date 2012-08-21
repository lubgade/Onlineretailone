package com.adjointweb.onlineretail.Dao;

import com.adjointweb.onlineretailone.entities.ShoppingCart;
import com.adjointweb.onlineretailone.entities.ShoppingCartItem;

public interface ShoppingCartDAO {
	ShoppingCart createShoppingCart(ShoppingCart cart);
	void addToCart(String userId, ShoppingCartItem item);
	void updateShoppingCart(ShoppingCart cart);
	ShoppingCart getShoppingCart(String userId);
	void deleteShoppingCart(String userId);
}
