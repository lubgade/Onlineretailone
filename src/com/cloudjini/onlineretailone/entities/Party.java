package com.cloudjini.onlineretailone.entities;

import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Party extends EntityObject {
	@PrimaryKey
	@Persistent
	private String userId;
	
	@Persistent
	private String partyName;
	
	

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the partyName
	 */
	public String getPartyName() {
		return partyName;
	}

	/**
	 * @param partyName the partyName to set
	 */
	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the phones
	 */
	public List<String> getPhones() {
		return phones;
	}

	/**
	 * @param phones the phones to set
	 */
	public void setPhones(List<String> phones) {
		this.phones = phones;
	}

	/**
	 * @return the partyType
	 */
	public String getPartyType() {
		return partyType;
	}

	/**
	 * @param partyType the partyType to set
	 */
	public void setPartyType(String partyType) {
		this.partyType = partyType;
	}

	/**
	 * @return the partyAddress
	 */
	public List<PartyAddress> getPartyAddresses() {
		return partyAddresses;
	}
 
	/**
	 * @param partyAddress the partyAddress to set
	 */
	public void setPartyAddresses(List<PartyAddress> partyAddress) {
		this.partyAddresses = partyAddress;
	}

	@Persistent
	private String firstName;
	
	@Persistent
	private String lastName;
	
	@Persistent
	private List<String> phones;
	
	@Persistent
	private String partyType;
	
	@Persistent(mappedBy = "party")
	private List<PartyAddress> partyAddresses;



public String toJSON()
{
	JSONSerializer serializer = new JSONSerializer();
    return serializer.exclude("*.class","partyAddresses.key").include("phones","partyAddresses").serialize(this);
}

public static Party getPartyfromjsonString(String jsonstring)
{
	Party party = new Party();
	party = new JSONDeserializer<Party>().deserialize(jsonstring,Party.class);
	
	return party;
}
}