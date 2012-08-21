package com.adjointweb.onlineretail.Dao;

import java.util.List;
import java.util.Set;

import com.adjointweb.onlineretailone.entities.Shingle;

public interface SearchDAO {

	Set<String> getShingles(String query);
	void addShingles(Set<Shingle> shingles );
	void deleteShingles(String query);
	 List<Object> search(String query, Class entity);


}
