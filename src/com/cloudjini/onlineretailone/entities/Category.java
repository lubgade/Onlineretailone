/**
 * 
 */
package com.cloudjini.onlineretailone.entities;

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

import com.cloudjini.onlineretail.Dao.CategoryDAO;
import com.cloudjini.onlineretail.Dao.DAOFactory;
import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PrimaryKey;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

 
  
/**
 * @author leena_ghanekar
 *  
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Category extends EntityObject {
	private static final Logger logger = Logger.getLogger(Category.class.getCanonicalName());
 
	@PrimaryKey
	@Persistent(valueStrategy  = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	private Date creationDate;
	
    @Persistent(mappedBy = "category")
    @Element(dependent = "true")
	private List <SubCategory> subCategories; 
	
	
	
	
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
	private Set<Long> productIds;
	
	@Persistent
	private Set<Long> categoryIds;
	
	public Category(){
		this.productIds = new HashSet();
		this.categoryIds = new HashSet();
	}
	
	public long getId(){
		return this.getKey().getId();
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
	
	public void setCreationDate(Date createDate){
		this.creationDate =createDate;
		
	}
	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
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
	 * @return the name
	 */
	public String getCategoryName() {
		return categoryName;
	}
	/**
	 * @param name the name to set
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	/**
	 * @return the desc
	 */
	public String getCategoryDesc() {
		return categoryDesc;
	}
	/**
	 * @param desc the desc to set
	 */
	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}
	
	
	public List<Category> getChildCategories(){
		CategoryDAO categoryDao = DAOFactory.getCategoryDAOInstance();
		List<Category> categories  = categoryDao.getChildCategories(this);
		return categories;
	}
	
 /*   @SuppressWarnings("unchecked")
	public JSONObject getJSONObject(){
		JSONObject obj = new JSONObject();

		obj.put(Messages.getString("Category.id"), this.getKey().getId()); //$NON-NLS-1$
		obj.put(Messages.getString("Category.categoryName"), this.categoryName); //$NON-NLS-1$
		obj.put(Messages.getString("Category.categoryDesc"), this.categoryDesc); //$NON-NLS-1$
		JSONArray subArray = new JSONArray();
		if (null != this.childCategories){
			Iterator<SubCategory> itsub = this.childCategories.iterator();
			while(itsub.hasNext()){
				SubCategory subcat =(SubCategory)itsub.next();
				subArray.add(subcat.getJSONObject());
			}
		
			obj.put("subcategories", subArray);
		}
		return obj;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public JSONObject getJSONObject(Long CatId){
		JSONObject obj = new JSONObject();
		JSONArray categoriesChilderen = new JSONArray();

		obj.put(Messages.getString("Category.id"), this.getKey().getId()); //$NON-NLS-1$
		obj.put(Messages.getString("Category.categoryName"), this.categoryName); //$NON-NLS-1$
		obj.put(Messages.getString("Category.categoryDesc"), this.categoryDesc); //$NON-NLS-1$
		JSONArray subArray = new JSONArray();
		if (null != this.childCategories){
			Iterator<SubCategory> itsub = this.childCategories.iterator();
			while(itsub.hasNext()){
				SubCategory subcat =(SubCategory)itsub.next();
				subArray.add(subcat.getJSONObject());
			}
		
			obj.put("subcategories", subArray);
		}
		/*List<Category> categories  = this.getChildCategories();
		if ( categories != null && !categories.isEmpty()){
			Iterator<Category> itCats  = categories.iterator();
			while(itCats.hasNext()){
				Category category = itCats.next();
				if ( this.getKey().getId() != category.getKey().getId()){
						if ( category.getCategoryIds().remove(CatId)){
							logger.info("Removed3:"+ CatId);
						}
					
						if (category.getCategoryIds().remove(new Long(this.getKey().getId()))){
					 		logger.info("Removed2:"+ this.getKey().getId());
					 	}
					 	
					 	categoriesChilderen.add(category.getJSONObject(CatId));

					}

				}
				
			
		}
		
		
		obj.put("childcategories",categoriesChilderen );
		
		return obj;
}	*/
	
	public static Category getCategoryfromjsonString(String jsonstring){
			Category category = new Category();
			 JSONParser parser = new JSONParser();
			 try{
					Map jsonMap = (Map)parser.parse(jsonstring);
					JSONObject categoryMap = (JSONObject)jsonMap.get(Messages.getString("Category.category")); //$NON-NLS-1$
					Long id = (	Long)categoryMap.get(Messages.getString("Category.id")); //$NON-NLS-1$
					if (id != null){
						Key categoryKey  = KeyFactory.createKey(Category.class.getSimpleName(), id.longValue());
						category.setKey(categoryKey);
					}

					
					String categoryName = (String)categoryMap.get(Messages.getString("Category.categoryName")); //$NON-NLS-1$
					category.setCategoryName(categoryName);
					String categoryDesc = (String)categoryMap.get(Messages.getString("Category.categoryDesc")); //$NON-NLS-1$
					category.setCategoryDesc(categoryDesc);
					
					//multiple categories
					JSONArray children = (JSONArray)categoryMap.get(Messages.getString("Category.subcategories")); //$NON-NLS-1$
					if (children != null){
					Iterator iter = children.iterator();
					List<SubCategory> childcategories = new ArrayList<SubCategory>();
					while(iter.hasNext()){
		
						JSONObject entry =(JSONObject) iter.next();
						SubCategory cate = new SubCategory();
						Long subcatid = (Long)entry.get(Messages.getString("Category.id")); //$NON-NLS-1$
						if ( subcatid != null){
							cate.setId(subcatid.longValue());	
						}
						String categoryNamechild =  (String)entry.get(Messages.getString("Category.categoryName")); //$NON-NLS-1$
						String categoryDescchild =  (String)entry.get(Messages.getString("Category.categoryDesc")); //$NON-NLS-1$
						
						cate.setCategoryName(categoryNamechild);
						cate.setCategoryDesc(categoryDescchild);
						childcategories.add(cate);
					
					}
 
					category.setSubCategories(childcategories);
					}
			 }
			 catch(ParseException e){
				 logger.severe(Messages.getString("Category.ErrorParsing")+e.getMessage()); //$NON-NLS-1$
			 }
			 category.setCreatedBy(Messages.getString("Category.createdBy")); //$NON-NLS-1$
			 category.setCreationDate(new Date(System.currentTimeMillis()));
		return category;
		
	}
/*	public List <Category> getChildren() {
		return children;
	}
	public void setChildren(List <Category> children) {
		this.children = children;
	}
	*/
	public Set<Long> getProductIds() {
		return productIds;
	}
	public void setProductIds(Set<Long> productIds) {
		this.productIds = productIds;
	}
	
 public void addProducts(Set<Long> prodIds){
	 this.productIds.addAll(prodIds);
 }

 public void addchildIds(Set<Long> catIds){
	 this.categoryIds.addAll(catIds);
 }
public Set<Long> getCategoryIds() {
	return categoryIds;
}

public void setCategoryIds(Set<Long> categoryIds) {
	this.categoryIds = categoryIds;
}
public List<SubCategory> getSubCategories(){
	return subCategories;
}
public void setSubCategories(List<SubCategory> subcategories){
	this.subCategories = subcategories;
}
public static Category getCategoryfromjson(String jsonstring){
	Category category = new Category();
	category = new JSONDeserializer<Category>().deserialize(jsonstring,Category.class);
	return category;
}
public String toJSON(){
	JSONSerializer serializer = new JSONSerializer();
    return serializer.exclude("*.class","*.key").include("subCategories","childCategories").serialize(this);
}

}

