package com.cloudjini.onlineretailone.entities;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.IdentityType;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.cloudjini.onlineretail.Servlets.ShoppingListServlet;
import com.google.appengine.api.datastore.Key;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class ShoppingCart extends EntityObject {
	
	private static final Logger logger = Logger.getLogger(ShoppingListServlet.class.getCanonicalName());
	
	
	@PrimaryKey
	@Persistent	
	private String userId;
	
	 
	
    @Persistent(mappedBy = "cart")
    @Element(dependent = "true")
	Set<ShoppingCartItem> items;
	
	public ShoppingCart(){
		this.items = new HashSet();
	}

	/**
	 * @return the items
	 */
	public Set<ShoppingCartItem> getItems() {
		return items;
	}
	
	public void addItem(ShoppingCartItem item){
		this.items.add(item);
	}
	
	public void addItems(Set<ShoppingCartItem> cartitems){
		this.items.addAll(cartitems);
	}
	public void addItems(Collection<ShoppingCartItem> cartitems){
		this.items.addAll(cartitems);
	}
	
	public void removeItem(ShoppingCartItem item){
		this.items.remove(item);
	}
	
	public void retainItems(Set<ShoppingCartItem> cartItems){
		this.items.retainAll(cartItems);
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(Set<ShoppingCartItem> items) {
		this.items = items;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	
public  String toJSON(){
       JSONSerializer serializer = new JSONSerializer();
       return serializer.exclude("*.class","items.key").include("items").serialize(this);
}

public static ShoppingCart getShoppingCartfromjsonString(String jsonstring){
	ShoppingCart shoppingCart = new ShoppingCart();
	shoppingCart = new JSONDeserializer<ShoppingCart>().deserialize(jsonstring, ShoppingCart.class);
	
	return shoppingCart;
}


}