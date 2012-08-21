package com.adjointweb.onlineretail.Dao;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.index.CorruptIndexException;

import com.cloudjini.onlineretailone.entities.Shingle;
import com.cloudjini.onlineretailone.entities.Store;
import com.google.appengine.api.datastore.Key;

public interface StoreDAO {
   void addStore(Store store);
   void updateStore(Store store);
   void deleteStore(long id);
   List<Store> getStores(String storeName);
   List<Store> searchStores(String storeName);
   Store getStore(long id);
   List<Store> listStores();
   void indexall() throws CorruptIndexException, IOException;
   void deleteStore(Key store);
   Set<String> searchStoreShingles(String query);
   List<Store> getStoresNearLocation(double alat, double alng, int distance);
   List<Store> getStoresByAddressField(String fieldName, String value);
   List<Store> getStoresByAddressFields(Map<String,String> addressInfo);
   
  void updateStoreCategory(long storeId, long categoryId);
   
   
}
