package com.adjointweb.onlineretail.json;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.cloudjini.onlineretailone.entities.EntityObject;

import java.util.ArrayList;
import java.util.Map;

import java.util.HashSet;



public class JSONHelper {
	
public static String getJSON(EntityObject ent){
	JSONObject objectjson = new JSONObject();
	try {
		objectjson = writeJSON(ent);
	} catch (IllegalArgumentException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IllegalAccessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
	return objectjson.toJSONString();
}
	
 private static JSONObject writeJSON(EntityObject ent) throws IllegalArgumentException, IllegalAccessException{	
	JSONObject obj = new JSONObject();
	Method[] methods = ent.getClass().getMethods();

	for (int i =0 ; i< methods.length;i++){
		Method method = methods[i];
		
	}
	return obj;
	}
	/*
	if( field.getType().isPrimitive() || field.getType().isInstance(new java.lang.String())){
		obj.put(field.getName(),field.get(ent));
	} else if ( field.getType().isInstance(new java.util.ArrayList())){
		JSONArray array = new JSONArray();
		array.addAll((ArrayList) field.get(ent));
		obj.put(field.getName(), array);
	}else if ( field.getType().isInstance(new java.util.HashSet())){
		JSONArray array = new JSONArray();
		array.addAll((HashSet) field.get(ent));
		obj.put(field.getName(), array);
	}else if (field.getType().isInstance(new java.util.HashMap())){
		JSONObject map = new JSONObject();
		map.putAll((Map) field.get(ent));
		obj.put(field.getName(), map);
	}else if (field.getType().isArray()){
	    Object array = field.get(ent);  
	    int length = Array.getLength(array);
	    JSONArray jarray = new JSONArray();
	    for (int j =0; j< length;j++){
	    	Object element = Array.get(array, i);
	    	if ( element instanceof EntityObject){
	    	  JSONObject newobj = writeJSON((EntityObject)element);
	    	  jarray.add(newobj);
	    	} else {
	    	jarray.add(element);
	    	}
	    }
	    obj.put(field.getName(),jarray );
	} else if (field.getType().isInstance(ent)){
		JSONObject entObj = writeJSON((EntityObject)field.get(ent));
		obj.put(field.getName(), entObj);
	} 
}*/	

	
}
