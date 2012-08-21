/**
 * 
 */
package com.adjointweb.onlineretailone.entities;
import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Rating;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PrimaryKey;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
/**
 * @author rushikesh_ubgade
 *
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Store extends EntityObject{
	private static final Logger logger = Logger.getLogger(Store.class.getCanonicalName());

	@PrimaryKey
	@Persistent(valueStrategy  = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	
	
	
	
	@Persistent
    private Set<String> fts;
	
	
	
	@Persistent
	private String storeName;
	
	@Persistent 
	private String storeDesc;
	
	
	@Persistent 
	private String managerName;
	
	@Persistent
	private List <Email> eMailaddreses;
	
	
	private Address address;
	
	@Persistent
	private List <String > phoneNumbers;
	
	@Persistent 
	private boolean allowStorePayment;
	
	@Persistent
	private boolean allowOrderModification;
	
	@Persistent 
	private boolean multipleStores = false;
	
	@Persistent 
	private int deliveryDistance = 2;
	
	@Persistent
	private String deliveryDistanceUnit;
	
	@Persistent
	private Rating rating;
	
	private String id;
	
	@Persistent
	private  Date creationDate ;
	@Persistent
	private Date modifiedDate;
	@Persistent
	private String createdBy;
	@Persistent
	private String modifiedBy;
	
	@Persistent
	private Long categoryId;
	
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
	
	
	public Store(){
		
		this.fts = new HashSet<String>();
        
       
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
	 * @return the storeName
	 */
	public String getStoreName() {
		return storeName;
	}
	/**
	 * @param storeName the storeName to set
	 */
	public void setStoreName(String storeName) {
		this.storeName = storeName;
		 // SearchJanitor.updateFTSStuffForStore(this);
	}
	/**
	 * @return the address
	 */
	public Address getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(Address address) {
		this.address = address;
	}
	/**
	 * @return the managerName
	 */
	public String getManagerName() {
		return managerName;
	}
	/**
	 * @param managerName the managerName to set
	 */
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
	/**
	 * @return the phoneNumbers
	 */
	public List <String > getPhoneNumbers() {
		return phoneNumbers;
	}
	/**
	 * @param phoneNumbers the phoneNumbers to set
	 */
	public void setPhoneNumbers(List <String > phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}
	/**
	 * @return the eMailaddreses
	 */
	public List <Email> geteMailaddreses() {
		return eMailaddreses;
	}
	/**
	 * @param eMailaddreses the eMailaddreses to set
	 */
	public void seteMailaddreses(List <Email> eMailaddreses) {
		this.eMailaddreses = eMailaddreses;
	}
	/**
	 * @return the allowStorePayment
	 */
	public boolean isAllowStorePayment() {
		return allowStorePayment;
	}
	/**
	 * @param allowStorePayment the allowStorePayment to set
	 */
	public void setAllowStorePayment(boolean allowStorePayment) {
		this.allowStorePayment = allowStorePayment;
	}
	/**
	 * @return the allowOrderModification
	 */
	public boolean isAllowOrderModification() {
		return allowOrderModification;
	}
	/**
	 * @param allowOrderModification the allowOrderModification to set
	 */
	public void setAllowOrderModification(boolean allowOrderModification) {
		this.allowOrderModification = allowOrderModification;
	}
	/**
	 * @return the multipleStores
	 */
	public boolean isMultipleStores() {
		return multipleStores;
	}
	/**
	 * @param multipleStores the multipleStores to set
	 */
	public void setMultipleStores(boolean multipleStores) {
		this.multipleStores = multipleStores;
	}
	/**
	 * @return the deliveryDistance
	 */
	public int getDeliveryDistance() {
		return deliveryDistance;
	}
	/**
	 * @param deliveryDistance the deliveryDistance to set
	 */
	public void setDeliveryDistance(int deliveryDistance) {
		this.deliveryDistance = deliveryDistance;
	}
	/**
	 * @return the storeDesc
	 */
	public String getStoreDesc() {
		return storeDesc;
	}
	/**
	 * @param storeDesc the storeDesc to set
	 */
	public void setStoreDesc(String storeDesc) {
		this.storeDesc = storeDesc;
	}
	/**
	 * @return the deliveryDistanceUnit
	 */
	public String getDeliveryDistanceUnit() {
		return deliveryDistanceUnit;
	}
	/**
	 * @param deliveryDistanceUnit the deliveryDistanceUnit to set
	 */
	public void setDeliveryDistanceUnit(String deliveryDistanceUnit) {
		this.deliveryDistanceUnit = deliveryDistanceUnit;
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
	
	public static Store getStorefromjsonstring(String json){
		 Store store = new Store();
		 JSONParser parser = new JSONParser();
		 try{
			 	
				Map jsonMap = (Map)parser.parse(json);
							
				JSONObject storeMap = (JSONObject)jsonMap.get(Messages.getString("Store.Store")); //$NON-NLS-1$
				Long id = (	Long)storeMap.get(Messages.getString("Store.id")); //$NON-NLS-1$
				if (id != null){
					store.setId(id.toString());
					Key storeKey  = KeyFactory.createKey(Store.class.getSimpleName(), id.longValue());
					store.setKey(storeKey);
				}

				store.setStoreName((String)storeMap.get(Messages.getString("Store.storeName"))); //$NON-NLS-1$
				store.setStoreDesc((String)storeMap.get(Messages.getString("Store.storeDesc"))); //$NON-NLS-1$
				String distance = (String)storeMap.get(Messages.getString("Store.deliveryDistance")); //$NON-NLS-1$
				store.setDeliveryDistance(Integer.valueOf(distance).intValue());
				store.setDeliveryDistanceUnit(Messages.getString("Store.UOM")); //$NON-NLS-1$
				store.setManagerName((String)storeMap.get(Messages.getString("Store.managerName"))); //$NON-NLS-1$
				JSONArray emails = (JSONArray)storeMap.get(Messages.getString("Store.emails")); //$NON-NLS-1$
				Iterator iter = emails.iterator();
				List<Email> emailaddresses = new ArrayList<Email>();
				while(iter.hasNext()){
	
					JSONObject  entry =(JSONObject) iter.next();
					Email email = new Email((String)entry.get(Messages.getString("Store.emailaddress"))); //$NON-NLS-1$
					emailaddresses.add(email);	
				}
				store.seteMailaddreses(emailaddresses);
				JSONArray phones = (JSONArray)storeMap.get(Messages.getString("Store.phones")); //$NON-NLS-1$
				Iterator iterPhone = phones.iterator();
				List<String> numbers = new ArrayList<String>();
				while(iterPhone.hasNext()){
					
					JSONObject entry =(JSONObject) iterPhone.next();
					numbers.add((String)entry.get(Messages.getString("Store.number"))); //$NON-NLS-1$
				}
				store.setPhoneNumbers(numbers);
				JSONArray addresses = (JSONArray)storeMap.get(Messages.getString("Store.addresses")); //$NON-NLS-1$
				Iterator addressIter = addresses.iterator();
				while(addressIter.hasNext()){
				 JSONObject addressMap =	(JSONObject)addressIter.next();
				 Address address = Address.getAddress(addressMap);
				 List<Address> listAddress =  new ArrayList<Address>();
				 store.setAddress(address);
				}
				Boolean storePayment = (Boolean)storeMap.get(Messages.getString("Store.allowStorePayment")); //$NON-NLS-1$
				store.setAllowStorePayment(storePayment.booleanValue());
				Boolean orderModify = (Boolean)storeMap.get(Messages.getString("Store.allowOrderModification")); //$NON-NLS-1$
				store.setAllowOrderModification(orderModify.booleanValue());
				Boolean multistore = (Boolean)storeMap.get(Messages.getString("Store.multipleStores")); //$NON-NLS-1$
				store.setMultipleStores(multistore.booleanValue());
		 }
		 catch(ParseException pe){
			 logger.severe(Messages.getString("Store.ErrorParsing")+pe.getMessage()); //$NON-NLS-1$
		 }
		 store.setCreatedBy(Messages.getString("Store.createdBy")); //$NON-NLS-1$
		 store.setCreationDate(new Date(System.currentTimeMillis()));
		return store;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getJSONObject(){
		JSONObject obj = new JSONObject();
		if ( this.getKey().getName() == null) {
			obj.put(Messages.getString("Store.id"),this.getKey().getId()); //$NON-NLS-1$
		} else {
			obj.put(Messages.getString("Store.id"),this.getKey().getName()); //$NON-NLS-1$
		}
		obj.put(Messages.getString("Store.storeName"),getStoreName()); //$NON-NLS-1$
		obj.put(Messages.getString("Store.storeDesc"),getStoreDesc()); //$NON-NLS-1$
		obj.put(Messages.getString("Store.managerName"),getManagerName()); //$NON-NLS-1$
	
		JSONArray phonesArray = new JSONArray();
		Iterator phonesit = getPhoneNumbers().iterator();
		while(phonesit.hasNext()){
			JSONObject phone = new JSONObject();
			phone.put(Messages.getString("Store.number"),(String)phonesit.next()); //$NON-NLS-1$
			phonesArray.add(phone);
		}
		obj.put(Messages.getString("Store.phones"), phonesArray); //$NON-NLS-1$
		
		List <Email> emails = geteMailaddreses();
		JSONArray emailsArray = new JSONArray();
		Iterator emailsIt = emails.iterator();
		while(emailsIt.hasNext()){
			JSONObject emailobj = new JSONObject();
			Email email = (Email)emailsIt.next();
			emailobj.put(Messages.getString("Store.emailaddress"),email.getEmail()); //$NON-NLS-1$
			emailsArray.add(emailobj);
		}
		
		obj.put(Messages.getString("Store.emails"),emailsArray); //$NON-NLS-1$
		//obj.put("rating", getRating());
		obj.put(Messages.getString("Store.deliveryDistance"),String.valueOf(getDeliveryDistance())); //$NON-NLS-1$
		//obj.put("deliveryDistanceUnit",getDeliveryDistanceUnit());
		JSONArray addresses = new JSONArray();
		if (this.getAddress() != null){
				addresses.add(this.getAddress().getJSONObject());
		
		}
		obj.put(Messages.getString("Store.addresses"),addresses ); //$NON-NLS-1$
		obj.put(Messages.getString("Store.allowStorePayment"), this.isAllowStorePayment()); //$NON-NLS-1$
		obj.put(Messages.getString("Store.allowOrderModification"), this.isAllowOrderModification()); //$NON-NLS-1$
		obj.put(Messages.getString("Store.multipleStores"), this.isMultipleStores()); //$NON-NLS-1$
		obj.put(Messages.getString("Store.categoryId"), this.getCategoryId()); //$NON-NLS-1$

		return obj;
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	 public void setFts(Set<String> fts) {
         this.fts = fts;
 }

	 /* (non-Javadoc)
		 * @see com.cloudjini.onlineretailone.entities.EntityObject#getStringtoBeIndex()
		 */
		@Override
		public String getStringtoBeIndex() {
			// TODO Auto-generated method stub
			return this.storeName;
		}

	 
	 
 public Set<String> getFts() {
         return fts;
 }
public Long getCategoryId() {
	return categoryId;
}
public void setCategoryId(Long categoryId) {
	this.categoryId = categoryId;
}

}
