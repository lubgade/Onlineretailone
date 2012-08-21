package com.adjointweb.onlineretailone.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

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
public class ShoppingList extends EntityObject { 
	
	private static final Logger logger = Logger.getLogger(ShoppingList.class.getCanonicalName());

	@PrimaryKey
	@Persistent(valueStrategy  = IdGeneratorStrategy.IDENTITY)
	//Long id;
	private Key key;
	
	@Persistent
	String uId;
	
	
	public String getuId() {
		return uId;
	}

	public void setuId(String uId) {
		this.uId = uId;
	}

	public Set<ShoppingListItem> getitems() {
		return items;
	}

	public void setitems(Set<ShoppingListItem> sItems) {
		this.items = sItems;
	}

	@Persistent
	Set<ShoppingListItem> items;
	
	@Persistent
	private String listName;
	
	
	public Key getkey() {
		return key;
	}
	
	public void setKey(Key key) {
		this.key = key;
	}
	
	public String getListName() {
		return listName;
	}
	
	public void setListName(String listName) {
		this.listName = listName;
	}
	
	
	
	public static ShoppingList getShoppingListfromjsonString(String jsonstring){
		ShoppingList list = new ShoppingList();
		 JSONParser parser = new JSONParser();
		 try{
				Map jsonMap = (Map)parser.parse(jsonstring);
				JSONObject listMap = (JSONObject)jsonMap.get("list");
				
				Long id = (	Long)listMap.get("id");
				if (id != null && id.longValue()!=0){
					
					Key listKey  = KeyFactory.createKey(ShoppingList.class.getSimpleName(), id.longValue());
					list.setKey(listKey);
				}
				

				JSONArray items = (JSONArray)listMap.get("items");
				if (items != null){
					Iterator iter = items.iterator();
					Set<ShoppingListItem> sItems = new HashSet<ShoppingListItem>();
					while(iter.hasNext()){
						JSONObject entry =(JSONObject) iter.next();
						ShoppingListItem sItem = new ShoppingListItem();
						Long itemId = (Long)entry.get("id");
						if ( itemId != null && itemId.longValue() != 0){
							sItem.setId(KeyFactory.createKey(ShoppingListItem.class.getSimpleName(), id));	
						}
						String itemName =  (String)entry.get("itemName");
						
						
						String sqty = (String)entry.get("qty");
						double qty = Double.parseDouble(sqty);
						//double qty =  ((Double)entry.get("qty")).doubleValue();
						String uom = (String)entry.get("uom");
						sItem.setItemName(itemName);
						sItem.setQty(qty);
						sItem.setUom(uom);
						sItems.add(sItem);
					}//end while
						String listName = (String)listMap.get("listName");
						list.setListName(listName);
						list.setitems(sItems);
				}//end if items not null
				
		 }//end try
		 catch(ParseException e){
			 logger.warning("Error in Parsing:"+ e.getMessage());
			 
		 } 
		 
	
	return list;
	
}

	@SuppressWarnings("unchecked")
	public JSONObject getJSONObject() {
		JSONObject obj = new JSONObject();
		
		obj.put("uId", this.uId);
		obj.put("id", key.getId());
		obj.put("listName", this.listName);
		JSONArray listItems = new JSONArray();
		if ( this.items != null){
		Iterator<ShoppingListItem> itItems = this.items.iterator(); 
		while(itItems.hasNext()){
			ShoppingListItem sItem = itItems.next();
			JSONObject itemObj = new JSONObject();
			itemObj.put("id",sItem.getId().getId() );
			
			itemObj.put("itemName",sItem.getItemName() );
			itemObj.put("qty",""+ sItem.getQty() );
			itemObj.put("uom",sItem.getUom() );
			listItems.add(itemObj);
		}
			obj.put("items", listItems);
		}
		return obj;	
	}
}