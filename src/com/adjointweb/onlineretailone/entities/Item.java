/**
 * 
 */
package com.adjointweb.onlineretailone.entities;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import java.util.Date;
import java.util.List;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.Rating;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PrimaryKey;

/**
 * @author Leena Ubgade
 * Testing
 */
@PersistenceCapable
public class Item extends EntityObject{
	@PrimaryKey
	@Persistent(valueStrategy  = IdGeneratorStrategy.IDENTITY)
	private Key key;
	@Persistent
	private  Date creationDate ;
	@Persistent
	private Date modifiedDate;
	@Persistent
	private String createdBy;
	@Persistent
	private String modifiedBy;
	
	@Persistent
	private String itemName;
	
	@Persistent
	private String itemDesc;
	
	@Persistent
	private double price;
	
	@Persistent
	private long barcode;
	
	@Persistent 
	private Link itemImageurl;
	
	@Persistent
	private Link smallImageurl;
	
	@Persistent
	private Link mediumImageurl;
	
	@Persistent
	private Link largeImageurl;
	
	@Persistent 
	private Link contentUrl;
	
	@Persistent 
	private Rating rating;
	
	@Persistent
	private String brandName;
	
	@Persistent
	private String uOM;
	
	@Persistent
	private String weight;
	
	@Persistent
	private String manufacturerId;
	
	@Persistent
	private List <String> promotions;

	@Persistent
	private List<Category> categories;
	
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
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}
	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	/**
	 * @return the modifiedDate
	 */
	public Date getModifiedDate() {
		return modifiedDate;
	}
	/**
	 * @param modifiedDate the modifiedDate to set
	 */
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}
	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	/**
	 * @return the modifiedBy
	 */
	public String getModifiedBy() {
		return modifiedBy;
	}
	/**
	 * @param modifiedBy the modifiedBy to set
	 */
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	/**
	 * @return the itemName
	 */
	public String getItemName() {
		return itemName;
	}
	/**
	 * @param itemName the itemName to set
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	/**
	 * @return the itemDesc
	 */
	public String getItemDesc() {
		return itemDesc;
	}
	/**
	 * @param itemDesc the itemDesc to set
	 */
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}
	/**
	 * @return the barcode
	 */
	public long getBarcode() {
		return barcode;
	}
	/**
	 * @param barcode the barcode to set
	 */
	public void setBarcode(long barcode) {
		this.barcode = barcode;
	}
	/**
	 * @return the itemImageurl
	 */
	public Link getItemImageurl() {
		return itemImageurl;
	}
	/**
	 * @param itemImageurl the itemImageurl to set
	 */
	public void setItemImageurl(Link itemImageurl) {
		this.itemImageurl = itemImageurl;
	}
	/**
	 * @return the rating
	 */
	public Rating getRating() {
		return rating;
	}
	/**
	 * @param rating the rating to set
	 */
	public void setRating(Rating rating) {
		this.rating = rating;
	}
	/**
	 * @return the brandName
	 */
	public String getBrandName() {
		return brandName;
	}
	/**
	 * @param brandName the brandName to set
	 */
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	/**
	 * @return the smallImageurl
	 */
	public Link getSmallImageurl() {
		return smallImageurl;
	}
	/**
	 * @param smallImageurl the smallImageurl to set
	 */
	public void setSmallImageurl(Link smallImageurl) {
		this.smallImageurl = smallImageurl;
	}
	/**
	 * @return the mediumImageurl
	 */
	public Link getMediumImageurl() {
		return mediumImageurl;
	}
	/**
	 * @param mediumImageurl the mediumImageurl to set
	 */
	public void setMediumImageurl(Link mediumImageurl) {
		this.mediumImageurl = mediumImageurl;
	}
	/**
	 * @return the largeImageurl
	 */
	public Link getLargeImageurl() {
		return largeImageurl;
	}
	/**
	 * @param largeImageurl the largeImageurl to set
	 */
	public void setLargeImageurl(Link largeImageurl) {
		this.largeImageurl = largeImageurl;
	}
	/**
	 * @return the uOM
	 */
	public String getUOM() {
		return uOM;
	}
	/**
	 * @param uOM the uOM to set
	 */
	public void setUOM(String uOM) {
		this.uOM = uOM;
	}
	/**
	 * @return the uOM
	 */
	public String getuOM() {
		return uOM;
	}
	/**
	 * @param uOM the uOM to set
	 */
	public void setuOM(String uOM) {
		this.uOM = uOM;
	}
	/**
	 * @return the weight
	 */
	public String getWeight() {
		return weight;
	}
	/**
	 * @param weight the weight to set
	 */
	public void setWeight(String weight) {
		this.weight = weight;
	}
	/**
	 * @return the contentUrl
	 */
	public Link getContentUrl() {
		return contentUrl;
	}
	/**
	 * @param contentUrl the contentUrl to set
	 */
	public void setContentUrl(Link contentUrl) {
		this.contentUrl = contentUrl;
	}
	/**
	 * @return the manufacturerId
	 */
	public String getManufacturerId() {
		return manufacturerId;
	}
	/**
	 * @param manufacturerId the manufacturerId to set
	 */
	public void setManufacturerId(String manufacturerId) {
		this.manufacturerId = manufacturerId;
	}
	/**
	 * @return the promotions
	 */
	public List <String> getPromotions() {
		return promotions;
	}
	/**
	 * @param promotions the promotions to set
	 */
	public void setPromotions(List <String> promotions) {
		this.promotions = promotions;
	}
	/**
	 * @return the categories
	 */
	public List<Category> getCategories() {
		return categories;
	}
	/**
	 * @param categories the categories to set
	 */
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	
	
}
