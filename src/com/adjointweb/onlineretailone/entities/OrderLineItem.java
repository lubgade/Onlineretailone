package com.adjointweb.onlineretailone.entities;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class OrderLineItem extends EntityObject  {
	@PrimaryKey
	@Persistent(valueStrategy  = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private Long productId;
	
	
	@Persistent
	private Double orderedQuantity;
	
	@Persistent
	private String productName;
	
	@Persistent 
	private Double productPrice;
	
	@Persistent
	private String orderLineItemStatus;
	
	@Persistent
	private Double adjustments;
	
	
	@Persistent
	private Double orderLineItemSubTotal;
	
	@Persistent
	private Long shippedQuantity;
	
	@Persistent
	private Long remainingQuatity;
	
	@Persistent
	private Order order;

	/**
	 * @return the key
	 */
	public Key getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(Key key) {
		this.key = key;
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
	 * @return the orderedQuantity
	 */
	public Double getOrderedQuantity() {
		return orderedQuantity;
	}

	/**
	 * @param orderedQuantity the orderedQuantity to set
	 */
	public void setOrderedQuantity(Double orderedQuantity) {
		this.orderedQuantity = orderedQuantity;
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
	 * @return the productPrice
	 */
	public Double getProductPrice() {
		return productPrice;
	}

	/**
	 * @param productPrice the productPrice to set
	 */
	public void setProductPrice(Double productPrice) {
		this.productPrice = productPrice;
	}

	/**
	 * @return the orderLineItemStatus
	 */
	public String getOrderLineItemStatus() {
		return orderLineItemStatus;
	}

	/**
	 * @param orderLineItemStatus the orderLineItemStatus to set
	 */
	public void setOrderLineItemStatus(String orderLineItemStatus) {
		this.orderLineItemStatus = orderLineItemStatus;
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

	/**
	 * @return the orderLineItemSubTotal
	 */
	public Double getOrderLineItemSubTotal() {
		return orderLineItemSubTotal;
	}

	/**
	 * @param orderLineItemSubTotal the orderLineItemSubTotal to set
	 */
	public void setOrderLineItemSubTotal(Double orderLineItemSubTotal) {
		this.orderLineItemSubTotal = orderLineItemSubTotal;
	}

	/**
	 * @return the shippedQuantity
	 */
	public Long getShippedQuantity() {
		return shippedQuantity;
	}

	/**
	 * @param shippedQuantity the shippedQuantity to set
	 */
	public void setShippedQuantity(Long shippedQuantity) {
		this.shippedQuantity = shippedQuantity;
	}

	/**
	 * @return the remainingQuatity
	 */
	public Long getRemainingQuatity() {
		return remainingQuatity;
	}

	/**
	 * @param remainingQuatity the remainingQuatity to set
	 */
	public void setRemainingQuatity(Long remainingQuatity) {
		this.remainingQuatity = remainingQuatity;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
}
