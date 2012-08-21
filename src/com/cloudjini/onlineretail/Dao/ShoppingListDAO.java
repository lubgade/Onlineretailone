package com.cloudjini.onlineretail.Dao;

import java.util.List;


import com.cloudjini.onlineretailone.entities.Category;
import com.cloudjini.onlineretailone.entities.ShoppingList;
import com.google.appengine.api.datastore.Key;


public interface ShoppingListDAO {

	//list
	 void addShoppingList(ShoppingList list);
	   void updateShoppingList(ShoppingList list);
	   void deleteShoppingList(long id);
	   
	   //getting all lists of 1 user
	   List<ShoppingList> getShoppingLists(String uId);
	   
	   
	   
	   
	 //  List<ShoppingList> searchStores(String storeName);
	   
	   //getting a  part list
	   ShoppingList getShoppingList(long id);
	//   List<ShoppingList> listStores();
	//   void deleteShoppingList(ShoppingList list);
	   
	   //getting all the lists
		 List<ShoppingList> getShoppingLists();
		 
		 void deleteShoppingListForUser(long uId);
}
