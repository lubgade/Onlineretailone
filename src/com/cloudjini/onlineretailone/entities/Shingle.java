package com.cloudjini.onlineretailone.entities;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
 
import org.json.simple.JSONObject;


@PersistenceCapable
public class Shingle extends EntityObject {
	@PrimaryKey
	@Persistent
	private String shingle;

	 
	 
	 
	
	@Persistent
	private Set<String> ngrams = new LinkedHashSet<String>();
	
	

	public void add(String ngram){
		ngrams.add(ngram);
	}
	public void remove(String ngram){
		ngrams.remove(ngram);
	}
	public void addAll(Set<String> ngramsSet){
		ngrams.addAll(ngramsSet);
	}
	
	public Set<String> getNgrams() {
		return ngrams;
	}

	public void setNgrams(Set<String> ngrams) {
		this.ngrams = ngrams;
	}

	public String getShingle() {
		return shingle;
	}

	public void setShingle(String shingle) {
		this.shingle = shingle;
	}

	@SuppressWarnings("unchecked")
	public JSONObject getJSONObject() {
		JSONObject obj = new JSONObject();
		obj.put(Messages.getString("Shingle.0"), this.shingle.trim()); //$NON-NLS-1$
		return obj;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Shingle)) {
			return false;
		}
		Shingle other = (Shingle) obj;
		if (shingle == null) {
			if (other.shingle != null) {
				return false;
			}
		} else if (!shingle.equals(other.shingle)) {
			return false;
		}
		return true;
	}
	
	

	
}
