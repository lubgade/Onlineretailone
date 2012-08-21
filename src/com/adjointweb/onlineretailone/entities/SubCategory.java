package com.adjointweb.onlineretailone.entities;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.json.simple.JSONObject;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class SubCategory {
	
	

	@PrimaryKey
	@Persistent(valueStrategy  = IdGeneratorStrategy.IDENTITY)
	private Key id ;

	@Persistent
	private Date creationDate;
	
	
	
	@Persistent
	private Date modifiedDate;
	@Persistent
	private String createdBy;
	@Persistent
	private String modifiedBy;

	@Persistent
	private String categoryName;
	
	@Persistent 
	private String categoryDesc;
	
	@Persistent
	private Category category;
	

	public long getId() {
		return id.getId();
	}

	public void setId(long id) {
		this.id = KeyFactory.createKey(SubCategory.class.getSimpleName(), id);
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

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryDesc() {
		return categoryDesc;
	}

	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}
	public JSONObject getJSONObject(){
		JSONObject obj = new JSONObject();
		obj.put(Messages.getString("SubCategory.id"), id.getId()); //$NON-NLS-1$
		obj.put(Messages.getString("SubCategory.categoryName"), categoryName); //$NON-NLS-1$
		obj.put(Messages.getString("SubCategory.categoryDesc"), categoryDesc); //$NON-NLS-1$
		
		return obj;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	

}
