package com.adjointweb.onlineretailone.entities;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Promotion extends EntityObject {
	@PrimaryKey
	@Persistent(valueStrategy  = IdGeneratorStrategy.IDENTITY)
	private Key key;
	@Persistent
	private String promotionDesc;
	
	@Persistent
	private String promoText;
	
	@Persistent
	private boolean showToCustomer;
	
	@Persistent
	private boolean enabled;
	
	@Persistent
	private boolean requiresCode;
	
	@Persistent
	private long useLimitPerOrder;
	
	@Persistent
	private long useLimitPerCustomer;
	
	@Persistent
	private long useLimitPerPromotion;
	
	
	@Persistent 
	private PromotionRule promorule;
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
	 * @return the promotionDesc
	 */
	public String getPromotionDesc() {
		return promotionDesc;
	}
	/**
	 * @param promotionDesc the promotionDesc to set
	 */
	public void setPromotionDesc(String promotionDesc) {
		this.promotionDesc = promotionDesc;
	}

}
