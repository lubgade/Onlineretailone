package com.cloudjini.onlineretailone.entities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;


@PersistenceCapable
public class Product extends EntityObject {

	@PrimaryKey
	@Persistent(valueStrategy  = IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	@Persistent
	private Date creationDate;
	 
	
	@Persistent
	private Date modifiedDate;
	@Persistent
	private String createdBy;
	@Persistent
	private String modifiedBy;

	@Persistent
	private String productName;
	
	@Persistent 
	private String prodDesc;
	
	@Persistent
	private String brandName;
	
	@Persistent
	private String manufactName;
	
	@Persistent
	private Double mrp;
	
	@Persistent
	private String UOM;
	
	@Persistent
	private String smallImgUrl;
	
	@Persistent
	private String mdmImgUrl;
	
	@Persistent
	private String lrgImgUrl;
	
	@Persistent
	private List<String> prodImgs;
	
	
	@Persistent
	private Double sellPrice;
	
	
	@Persistent
	private Double cost;
	
	@Persistent
	private TaxType taxType;
	
	@Persistent
	private Set<String> fts;
	
	@Persistent 
	private Date manufactureDate;
	
	@Persistent
	private Long primaryCategoryId;
	
	@Persistent
	private Set<Long> categoryMembership;
	
	public void addCategoryMemberShip(Long categoryId){
		this.categoryMembership.add(categoryId);
	}
	
	public void addCategoryMemberShips(Set<Long> categoryIds){
		this.categoryMembership.addAll(categoryIds);
	}
	
	
	/**
	 * @return the manufactureDate
	 */
	public Date getManufactureDate() {
		return manufactureDate;
	}

	/**
	 * @param manufactureDate the manufactureDate to set
	 */
	public void setManufactureDate(Date manufactureDate) {
		this.manufactureDate = manufactureDate;
	}

	/**
	 * @return the expiryDate
	 */
	public Date getExpiryDate() {
		return expiryDate;
	}

	/**
	 * @param expiryDate the expiryDate to set
	 */
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	@Persistent
	private Date expiryDate;
	

	
	public Product(){
		fts = new HashSet<String>();
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
	 * @return the manufactName
	 */
	public String getManufactName() {
		return manufactName;
	}

	/**
	 * @param manufactName the manufactName to set
	 */
	public void setManufactName(String manufactName) {
		this.manufactName = manufactName;
	}

	/**
	 * @return the mrp
	 */
	public Double getMrp() {
				
		return mrp;
	}
    
	/**
	 * @param mrp the mrp to set
	 */
	public void setMrp(Double mrp) {
		this.mrp = mrp;
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

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProdDesc() {
		return prodDesc;
	}

	public void setProdDesc(String prodDesc) {
		this.prodDesc = prodDesc;
	}
	
	public String getUOM() {
		return UOM;
	}

	public void setUOM(String UOM) {
		this.UOM = UOM;
	}
		
	
	@SuppressWarnings("unchecked")
	public JSONObject getJSONObject(){
		JSONObject obj = new JSONObject();
		JSONArray imgs = new JSONArray();
		JSONArray categoryIds = new JSONArray();
		Iterator<String> itProdImgs = this.prodImgs.iterator();
		while(itProdImgs.hasNext()){
			JSONObject prodimgObj = new JSONObject();
			prodimgObj.put("img", itProdImgs.next());
			imgs.add(prodimgObj);
		} 
		
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		String manufactDate;
		String expiryDate;
		if ( this.manufactureDate == null){
			manufactDate = "";
		}else {
			manufactDate =  format.format( this.manufactureDate);
		}
		if ( this.expiryDate == null){
			expiryDate ="";
		}else {
			expiryDate = format.format(this.expiryDate);
		}
		Iterator<Long> categoryIdsIt = this.categoryMembership.iterator();
		while(categoryIdsIt.hasNext()){
			JSONObject catid = new JSONObject();
			catid.put("categoryId", categoryIdsIt.next());
			categoryIds.add(catid);
		}
		
		obj.put("id", this.getId());
		obj.put("productName", this.productName);
		obj.put("prodDesc", this.prodDesc);
		obj.put("brandName", this.brandName);
		obj.put("taxType", this.taxType);
		obj.put("manufactName", this.manufactName);
		obj.put("mrp", this.mrp);
		obj.put("UOM", this.UOM);
		obj.put("smallImgUrl", this.smallImgUrl);
		obj.put("mdmImgUrl", this.mdmImgUrl);
		obj.put("lrgImgUrl", this.lrgImgUrl);
		obj.put("prodImgs",imgs);
		obj.put("sellPrice", this.sellPrice);
		obj.put("cost", this.cost);
		obj.put("manufactDate",manufactDate );
		obj.put("expiryDate",expiryDate);
		obj.put("categoryMemberShips", categoryIds);
		
		return obj;
	}	
	
	public static Product getProductfromjsonString(String jsonstring){
		Product product = new Product();
		 JSONParser parser = new JSONParser();
		 try{
				Map jsonMap = (Map)parser.parse(jsonstring);
				JSONObject productMap = (JSONObject)jsonMap.get("product");
				Long id = (	Long)productMap.get("id");
				if (id != null && id!=0){
					//Key productKey  = KeyFactory.createKey(Product.class.getSimpleName(), id.longValue());
					product.setId(id);
					}

				
				String productName = (String)productMap.get("productName");
				product.setProductName(productName);
				String prodDesc = (String)productMap.get("prodDesc");
				product.setProdDesc(prodDesc);
				//String taxType = (String)productMap.get("taxType");
				//product.setTaxType(taxType);
				String brandName = (String)productMap.get("brandName");
				product.setBrandName(brandName);
				String manufactName = (String)productMap.get("manufactName");
				product.setManufactName(manufactName);
				String UOM = (String)productMap.get("UOM");
				product.setUOM(UOM);
				Object mrpObj = productMap.get("mrp");
				if (  mrpObj instanceof Double){
					Double mrp = (Double)mrpObj;
					product.setMrp(mrp);
				}else if (mrpObj instanceof Long){
					Long mrp = (Long)mrpObj;
					product.setMrp(mrp.doubleValue());
				}else if (mrpObj instanceof String){
					String mrps = (String)mrpObj;
					Double mrp = Double.parseDouble(mrps);
					product.setMrp(mrp);
				}
				Object sellPriceobj  = productMap.get("sellPrice");
				if ( sellPriceobj instanceof Double){
					Double sellPrice = (Double)sellPriceobj;
					product.setSellPrice(sellPrice);
				}else if (sellPriceobj instanceof Long){
					Long sellPrice = (Long)sellPriceobj;
					product.setSellPrice(sellPrice.doubleValue());
				}else if (sellPriceobj instanceof String){
					String sellprices = (String)sellPriceobj;
					Double sellPrice = Double.parseDouble(sellprices) ;
					product.setSellPrice(sellPrice);
				}
				Object costObj = productMap.get("cost");
				if ( costObj instanceof String){
					String costs = (String)costObj;	
					Double cost = Double.parseDouble(costs);
					product.setCost(cost);
				}else if ( costObj instanceof Double){
					Double cost = (Double)costObj;
					product.setCost(cost);
				}else if ( costObj instanceof Long){
					Long costl = (Long)costObj;
					product.setCost(costl.doubleValue());
				}
				
				String smallImgUrl = (String)productMap.get("smallImgUrl");
				product.setSmallImgUrl(smallImgUrl);
				String mdmImgUrl = (String)productMap.get("mdmImgUrl");
				product.setMdmImgUrl(mdmImgUrl);
				String lrgImgUrl = (String)productMap.get("lrgImgUrl");
				product.setLrgImgUrl(lrgImgUrl);				
				JSONArray imgs = (JSONArray)productMap.get("prodImgs");
				List<String> images =new ArrayList<String>();
				Iterator<JSONObject> itImgs = imgs.iterator();
				while(itImgs.hasNext()){
					JSONObject imgObj = itImgs.next();
					images.add((String)imgObj.get("img"));
				}
				
		        product.setProdImgs(images);
		        String manufactDate =   (String)productMap.get("manufactDate");
		        String expiryDate =   (String)productMap.get("expiryDate");
		        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		        product.setManufactureDate(dateFormat.parse(manufactDate));
		        product.setExpiryDate(dateFormat.parse(expiryDate));
		 
		 }
		 catch(ParseException e){
			 System.out.println("Error in Parsing:"+ e.getMessage());
		 } catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 product.setCreatedBy("Administrator");
		 product.setCreationDate(new Date(System.currentTimeMillis()));
	return product;
	
}

	public String getSmallImgUrl() {
		return smallImgUrl;
	}

	public void setSmallImgUrl(String smallImgUrl) {
		this.smallImgUrl = smallImgUrl;
	}

	public String getMdmImgUrl() {
		return mdmImgUrl;
	}

	public void setMdmImgUrl(String mdmImgUrl) {
		this.mdmImgUrl = mdmImgUrl;
	}

	public String getLrgImgUrl() {
		return lrgImgUrl;
	}

	public void setLrgImgUrl(String lrgImgUrl) {
		this.lrgImgUrl = lrgImgUrl;
	}

	public List<String> getProdImgs() {
		return prodImgs;
	}

	public void setProdImgs(List<String> prodImgs) {
		this.prodImgs = prodImgs;
	}

	public Double getSellPrice() {
		
		return sellPrice;
	}

	public void setSellPrice(Double sellPrice) {
		this.sellPrice = sellPrice;
	}

	public Double getCost() {
			
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}
	 @Override 	
  	 public Set<String> getFts() {
		return fts;
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
		return this.getProductName();
	}

	/**
	 * @return the primaryCategoryId
	 */
	public Long getPrimaryCategoryId() {
		return primaryCategoryId;
	}

	/**
	 * @param primaryCategoryId the primaryCategoryId to set
	 */
	public void setPrimaryCategoryId(Long primaryCategoryId) {
		this.primaryCategoryId = primaryCategoryId;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	public Set<Long> getCategoryMembership() {
		return categoryMembership;
	}

	public void setCategoryMembership(Set<Long> categoryMembership) {
		this.categoryMembership = categoryMembership;
	}

	/**
	 * @return the taxType
	 */
	public TaxType getTaxType() {
		return taxType;
	}

	/**
	 * @param taxType the taxType to set
	 */
	public void setTaxType(TaxType taxType) {
		this.taxType = taxType;
	}

	

			
				
				
				
				
}