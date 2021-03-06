package com.adjointweb.onlineretailone.entities;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class TaxType extends EntityObject {
	@PrimaryKey
	@Persistent(valueStrategy  = IdGeneratorStrategy.IDENTITY)
	private String TaxType;
	@Persistent 
	private double percentageTax;
}
