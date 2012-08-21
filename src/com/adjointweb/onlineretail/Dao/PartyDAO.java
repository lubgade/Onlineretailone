package com.adjointweb.onlineretail.Dao;

import com.adjointweb.onlineretailone.entities.Party;
import com.google.appengine.api.datastore.Key;

public interface PartyDAO {
 void createParty(Party party);
 void updateParty(Party party);
 Party getParty(String userId);
 void deleteParties(String partyName);
 
 
}
