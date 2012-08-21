package com.cloudjini.onlineretailone.entities;


import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class ShoppingListItem extends EntityObject{
	
	
	@PrimaryKey
	@Persistent(valueStrategy  = IdGeneratorStrategy.IDENTITY)
	private Key id;
	@Persistent
	private String itemName;

	@Persistent
	private double qty;
	
	@Persistent
	private String uom;


	
	
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public double getQty() {
		return qty;
	}

	public void setQty(double qty) {
		this.qty = qty;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public Key getId() {
		return id;
	}

	public void setId(Key id) {
		this.id = id;
	}



	
	
	

}
