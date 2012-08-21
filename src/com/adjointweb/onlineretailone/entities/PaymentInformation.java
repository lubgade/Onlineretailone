package com.adjointweb.onlineretailone.entities;

import java.util.Currency;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class PaymentInformation extends EntityObject {

	@PrimaryKey
	@Persistent(valueStrategy  = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private Double paymentAmount;
	
	@Persistent
	private Currency paymentCurrency;
	
	@Persistent
	private String paymentType;
	
	@Persistent
	private String paymentCardLast4digits;
	
	@Persistent
	private String paymentStatus;
	
	@Persistent
	private String paymentRemarks;
}
