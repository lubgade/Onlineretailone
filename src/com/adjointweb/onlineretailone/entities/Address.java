/**
 * 
 */
package com.adjointweb.onlineretailone.entities;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.Transactional;

import java.util.Date;
import java.util.Map;
import java.util.List;
import java.util.logging.Logger;

import com.beoui.geocell.annotations.Geocells;
import com.beoui.geocell.annotations.Latitude;
import com.beoui.geocell.annotations.Longitude;
import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Key;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PrimaryKey;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
	
/**
 * @author Leena Ubgade
 *
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Address extends EntityObject{
	private static final Logger logger = Logger.getLogger(Address.class.getCanonicalName());

	
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
	
	public static Address getAddressfromjsonstring(String json){
		Address address = new Address();
		JSONParser parser = new JSONParser();
		 try{
				Map jsonMap = (Map)parser.parse(json);
				address.setAddressline1((String)jsonMap.get(Messages.getString("Address.Line1"))); //$NON-NLS-1$
				address.setAddressline2((String)jsonMap.get(Messages.getString("Address.Line2"))); //$NON-NLS-1$
				
				Map city = (Map)jsonMap.get(Messages.getString("Address.City"));
				
				address.setCity((String)city.get(Messages.getString("Address.City.Value"))); //$NON-NLS-1$
				
				address.setState((String)city.get(Messages.getString("Address.City.State"))); //$NON-NLS-1$
				address.setZip((String)jsonMap.get(Messages.getString("Address.zip"))); //$NON-NLS-1$
				address.setCountry((String)jsonMap.get(Messages.getString("Address.country"))); //$NON-NLS-1$
				double lat = ((Double)jsonMap.get(Messages.getString("Address.Lat"))).doubleValue(); //$NON-NLS-1$
				double lng = ((Double)jsonMap.get(Messages.getString("Address.Lng"))).doubleValue(); //$NON-NLS-1$
				address.setLat(lat);
				address.setLng(lng);
			}
		 catch(ParseException pe){
			 logger.severe(Messages.getString("Address.ErrorParsing")+pe.getMessage()); //$NON-NLS-1$
		 }	 
		return address;
	}

	public static Address getAddress( JSONObject jsonMap){
		Address address = new Address();
		address.setAddressline1((String)jsonMap.get(Messages.getString("Address.Line1"))); //$NON-NLS-1$
		address.setAddressline2((String)jsonMap.get(Messages.getString("Address.Line2"))); //$NON-NLS-1$
		address.setCity((String)jsonMap.get(Messages.getString("Address.City"))); //$NON-NLS-1$
		address.setState((String)jsonMap.get(Messages.getString("Address.State"))); //$NON-NLS-1$
		address.setZip((String)jsonMap.get(Messages.getString("Address.zip"))); //$NON-NLS-1$
		address.setCountry((String)jsonMap.get(Messages.getString("Address.country"))); //$NON-NLS-1$
		address.setLat(((Double)jsonMap.get(Messages.getString("Address.lat"))).doubleValue()); //$NON-NLS-1$
		address.setLng(((Double)jsonMap.get(Messages.getString("Address.lng"))).doubleValue()); //$NON-NLS-1$
		return address;
	}
	
	
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
	 * @return the addressline1
	 */
	public String getAddressline1() {
		return addressline1;
	}
	/**
	 * @param addressline1 the addressline1 to set
	 */
	public void setAddressline1(String addressline1) {
		this.addressline1 = addressline1;
	}
	/**
	 * @return the addressline2
	 */
	public String getAddressline2() {
		return addressline2;
	}
	/**
	 * @param addressline2 the addressline2 to set
	 */
	public void setAddressline2(String addressline2) {
		this.addressline2 = addressline2;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return the zip
	 */
	public String getZip() {
		return zip;
	}
	/**
	 * @param zip the zip to set
	 */
	public void setZip(String zip) {
		this.zip = zip;
	}
	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	/**
	 * @return the lng
	 */
	public double getLng() {
		return lng;
	}
	/**
	 * @param lng the lng to set
	 */
	public void setLng(double lng) {
		this.lng = lng;
	}
	/**
	 * @return the lat
	 */
	public double getLat() {
		return lat;
	}
	/**
	 * @param lat the lat to set
	 */
	public void setLat(double lat) {
		this.lat = lat;
	}
	/**
	 * @return the geographicalPoint
	 */
	public GeoPt getGeographicalPoint() {
		return geographicalPoint;
	}
	/**
	 * @param geographicalPoint the geographicalPoint to set
	 */
	public void setGeographicalPoint(GeoPt geographicalPoint) {
		this.geographicalPoint = geographicalPoint;
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
	
	@SuppressWarnings("unchecked")
	public JSONObject getJSONObject(){
		JSONObject obj = new JSONObject();
		obj.put(Messages.getString("Address.id"), this.getKey().getId()); //$NON-NLS-1$
		obj.put(Messages.getString("Address.Line1"), this.getAddressline1()); //$NON-NLS-1$
		obj.put(Messages.getString("Address.Line2"), this.getAddressline2()); //$NON-NLS-1$
		obj.put(Messages.getString("Address.City"), this.getCity()); //$NON-NLS-1$
		obj.put(Messages.getString("Address.State"), this.getState()); //$NON-NLS-1$
		obj.put(Messages.getString("Address.zip"), this.getZip()); //$NON-NLS-1$
		obj.put(Messages.getString("Address.country"), this.getCountry()); //$NON-NLS-1$
		obj.put(Messages.getString("Address.lat"),this.getLat() ); //$NON-NLS-1$
		obj.put(Messages.getString("Address.lng"),this.getLng()); //$NON-NLS-1$
			
		return obj;
	}

	public int getDistance(double newlat,double newlng) {
		int distance = (int) ( 6371 * Math.acos( Math.cos( Math.toRadians(newlat)) * Math.cos( Math.toRadians(lat) ) * Math.cos( Math.toRadians( lng ) - Math.toRadians(newlng) ) + Math.sin( Math.toRadians(newlat) ) * Math.sin(Math.toRadians(lat)) )) ; 
		return distance;
	}

	public List<String> getGeoCells() {
		return geoCells;
	}

	public void setGeoCells(List<String> geoCells) {
		this.geoCells = geoCells;
	}
	
	

	
}
