package com.adjointweb.onlineretailone.entities;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.IdentityType;


import com.google.appengine.api.datastore.Key;
 

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class ShoppingCartItem extends EntityObject {

	@PrimaryKey
	@Persistent(valueStrategy  = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	
	@Persistent
	private Long productId;
	
	@Persistent
	private String productName;
	
	@Persistent
	private Double Quantity=new Double(0.00);
	
	@Persistent
	private Double productprice=new Double(0.00);
	
	@Persistent
	private Double subtotal = new Double(Quantity * productprice);
	
	@Persistent
	private Double mrp = new Double(0.00);
	
	@Persistent
	private Double adjustments = new Double(0.00);
	
	@Persistent
	private ShoppingCart cart;
	
	public ShoppingCartItem(){
		this.subtotal = this.Quantity * this.productprice;
	} 
	
	public Double getSubtotal() {
		return subtotal;
	}


	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}



	/**
	 * @return the productId
	 */
	public Long getProductId() {
		return productId;
	}


	/**
	 * @param productId the productId to set
	 */
	public void setProductId(Long productId) {
		this.productId = productId;
	}


	/**
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}


	/**
	 * @param productName the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}


	/**
	 * @return the quantity
	 */
	public Double getQuantity() {
		return Quantity;
	}


	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(Double quantity) {
		Quantity = quantity;
	}


	/**
	 * @return the productprice
	 */
	public Double getProductprice() {
		return productprice;
	}


	/**
	 * @param productprice the productprice to set
	 */
	public void setProductprice(Double productprice) {
		this.productprice = productprice;
	}


	/**
	 * @return the adjustments
	 */
	public Double getAdjustments() {
		return adjustments;
	}


	/**
	 * @param adjustments the adjustments to set
	 */
	public void setAdjustments(Double adjustments) {
		this.adjustments = adjustments;
	}


	public Key getKey() {
		return key;
	}


	public void setKey(Key key) {
		this.key = key;
	}


	public Double getMrp() {
		return mrp;
	}


	public void setMrp(Double mrp) {
		this.mrp = mrp;
	}
	
	
}
