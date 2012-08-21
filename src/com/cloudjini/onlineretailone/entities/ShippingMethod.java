package com.cloudjini.onlineretailone.entities;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class ShippingMethod extends EntityObject {
	@PrimaryKey
	@Persistent(valueStrategy  = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private String shippingMethod;
}
