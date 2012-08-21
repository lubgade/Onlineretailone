/*
 * Author : Leena Ubgade
 */

package com.adjointweb.onlineretail.Dao;

public class DAOFactory {

	static StoreDAO storeDAO = null;	
	
	static CategoryDAO categoryDAO = null;
	static SearchDAO searchDAO = null;
	static ProductDAO productDAO = null;
	static ShoppingListDAO shoppingListDAO = null;
	static OrderDAO orderDAO = null;
	static ShoppingCartDAO shoppingCartDAO = null;
	static ShoppingListDAO listDAO = null;
	static PartyDAO partyDAO = null;
	
    
	public static StoreDAO getStoreDAOInstance(){
		
		
		if (storeDAO == null){
			storeDAO = new StoreDAOImpl(); 
		}
	return storeDAO;
	
}
	public static SearchDAO getSearchDAOInstance(){
		if (searchDAO == null){
		 searchDAO = new SearchDAOImpl();	
		}
		return searchDAO;
	}
		
	public static CategoryDAO getCategoryDAOInstance(){
		
		
		if (categoryDAO == null){
			categoryDAO = new CategoryDAOImpl(); 
		}
	return categoryDAO;
	
}

	public static ProductDAO getProductDAOInstance(){
		
		
		if (productDAO == null){
			productDAO = new ProductDAOImpl(); 
		}
	return productDAO;
	
}
	
public static ShoppingListDAO getShoppingListDAOInstance(){
		
		
		if (shoppingListDAO == null){
			shoppingListDAO = new ShoppingListDAOImpl(); 
		}
	return shoppingListDAO;
	
}	

public static OrderDAO getOrderDAOInstance(){
	
	
	if (orderDAO == null){
		orderDAO = new OrderDAOImpl(); 
	}
return orderDAO;

}	

public static PartyDAO getPartyDAOInstance(){
	
	
	if (partyDAO == null){
		partyDAO = new PartyDAOImpl(); 
	}
return partyDAO;

}	


public static ShoppingCartDAO getShoppingCartDAO(){
	
	
	if (shoppingCartDAO == null){
		shoppingCartDAO = new ShoppingCartDAOImpl(); 
	}
return shoppingCartDAO;

}	

public static PartyDAO getPartyDAO(){
	
	
	if (partyDAO == null){
		partyDAO = new PartyDAOImpl(); 
	}
return partyDAO;

}	



	
}