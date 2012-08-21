package com.adjointweb.onlineretailone.entities;

import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Transactional;

import com.beoui.geocell.annotations.Geocells;
import com.beoui.geocell.annotations.Latitude;
import com.beoui.geocell.annotations.Longitude;
import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Key;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true") 

public class OrderAddress {
	@PrimaryKey
	@Persistent(valueStrategy  = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private String name;
	
	@Persistent 
	private String phonenumber;
	
	@Persistent
	private  Date creationDate ;
	@Persistent
	private Date modifiedDate;
	@Persistent
	private String createdBy;
	@Persistent
	private String modifiedBy;
	
	
	@Persistent
	private String addressline1;
	
	@Persistent
	private String addressline2;
	
	@Persistent 
	private String city;
	
	@Persistent
	private String state;
	
	@Persistent
	private String zip;
	
	@Persistent
	private String country;
	
	@Persistent
	@Longitude
	private double lng;
	
	@Persistent
	@Latitude
	private double lat;
	
	@Persistent
	private GeoPt geographicalPoint;
	
	@Transactional
	private int distance;
	
	@Persistent
	@Geocells
	private List<String> geoCells;
	
	@Persistent
	private int primaryAddress =0;

	
	@Persistent
	private Order order;
	
	
	public static OrderAddress getAddressfromjsonstring(String json){
		OrderAddress address = new OrderAddress();
		address = new JSONDeserializer<OrderAddress>().deserialize(json, OrderAddress.class);
		
		return address;
	}
	
	public  String toJSON(){
	       JSONSerializer serializer = new JSONSerializer();
	       return serializer.exclude("*.class").serialize(this);
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getAddressline1() {
		return addressline1;
	}

	public void setAddressline1(String addressline1) {
		this.addressline1 = addressline1;
	}

	public String getAddressline2() {
		return addressline2;
	}

	public void setAddressline2(String addressline2) {
		this.addressline2 = addressline2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public GeoPt getGeographicalPoint() {
		return geographicalPoint;
	}

	public void setGeographicalPoint(GeoPt geographicalPoint) {
		this.geographicalPoint = geographicalPoint;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public List<String> getGeoCells() {
		return geoCells;
	}

	public void setGeoCells(List<String> geoCells) {
		this.geoCells = geoCells;
	}

	public int getPrimaryAddress() {
		return primaryAddress;
	}

	public void setPrimaryAddress(int primaryAddress) {
		this.primaryAddress = primaryAddress;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
}
