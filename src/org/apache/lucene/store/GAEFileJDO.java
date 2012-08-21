package org.apache.lucene.store;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.cloudjini.onlineretail.Dao.PMF;

public class GAEFileJDO {
  // global logger
  private static Logger log = Logger.getLogger(GAEFileJDO.class.getName());

  //    @Deprecated
  //    public static void saveOrUpdate(String category, Long version, String fileName, byte[] content, Long fileLength, Long lastModified) {
  //		PersistenceManager pm = PMF.get().getPersistenceManager();
  //		Query query = pm.newQuery(GAEFile.class);
  //		query.setFilter("cat == category && ver == version && name == fileName");
  //		query.declareParameters("String category, Long version, String fileName");
  //		List<GAEFile> files = (List<GAEFile>) query.execute(category, version, fileName);
  //		GAEFile gaeFile = null;
  //		if (files.size() > 0) {
  //    		gaeFile = files.get(0);
  //            ///gaeFile.setContent(content);
  //    		gaeFile.setLength(fileLength);
  //    		gaeFile.setLastModified(lastModified);
  //    		gaeFile.setDeleted(new Boolean(false));
  //    		GAEFileContentJDO.saveOrUpdate(gaeFile.getCId(), new Blob(content));
  //		} else {
  //			// instance GAEFileContent entity
  //			GAEFileContent gaeContent = new GAEFileContent();
  //			gaeContent.setContent(new Blob(content));
  //			pm.makePersistent(gaeContent);
  //			log.info("make GAEFileContent persistent for '" + category + "." + fileName + "." + version + "', new instance id is '" + gaeContent.getId() + "'");
  //
  //			// instance GAEFile entity
  //        	gaeFile = new GAEFile();
  //        	gaeFile.setCat(category);
  //        	gaeFile.setVer(version);
  //        	gaeFile.setName(fileName);
  //            ///gaeFile.setContent(content);
  //        	gaeFile.setCId(gaeContent.getId());
  //            gaeFile.setLength(fileLength);
  //            gaeFile.setLastModified(lastModified);
  //            gaeFile.setDeleted(false);
  //            
  //            // create persistence data
  //            pm.makePersistent(gaeFile);
  //		}
  //        pm.close();
  //		
  //		String cacheId = gaeFile.getCacheId();
  //		log.info("trying to put byte data to memcache for '" + gaeFile.getName() + "' with id '" + cacheId + "'");
  //		///try {
  //		///	CF.get().createCache(Collections.emptyMap()).put(cacheId, content);
  //			CF.getCache().put(cacheId, content);
  //		///} catch (CacheException e) {
  //		///	log.warning("failed to put byte data to memcache for '" + gaeFile.getName() + "' with id '" + cacheId + "', because:" + e);
  //		///	e.printStackTrace();
  //		///}
  //	}

  public static Long saveOrUpdate(String category, Long version, String fileName, Long fileLength,
      Long lastModified, Integer segmentCount) {
    PersistenceManager pm = PMF.get().getPersistenceManager();
    Query query = pm.newQuery(GAEFile.class);
    query.setFilter("cat == category && ver == version && name == fileName");
    query.declareParameters("String category, Long version, String fileName");
    List<GAEFile> files = (List<GAEFile>) query.execute(category, version, fileName);
    GAEFile gaeFile = null;
    if (files.size() > 0) {
      gaeFile = files.get(0);
      gaeFile.setLength(fileLength);
      gaeFile.setLastModified(lastModified);
      gaeFile.setSegmentCount(segmentCount);
      gaeFile.setDeleted(new Boolean(false));
    } else {
      // instance GAEFile entity
      gaeFile = new GAEFile();
      gaeFile.setCat(category);
      gaeFile.setVer(version);
      gaeFile.setName(fileName);
      gaeFile.setLength(fileLength);
      gaeFile.setLastModified(lastModified);
      gaeFile.setSegmentCount(segmentCount);
      gaeFile.setDeleted(false);

      // create persistence data
      pm.makePersistent(gaeFile);
    }
    pm.close();

    return gaeFile.getId();
  }

  public static void delete(Long fileId) {
    PersistenceManager pm = PMF.get().getPersistenceManager();
    Query query = pm.newQuery(GAEFile.class);
    query.setFilter("id == fileId");
    query.declareParameters("Long fileId");
    List<GAEFile> files = (List<GAEFile>) query.execute(fileId);
    for (int i = 0; i < files.size(); i++) {
      GAEFile file = files.get(i);
      for (int sNo = 0; sNo < file.getSegmentCount(); sNo++) {
        GAEFileContentJDO.delete(file.getId(), sNo);
      }
      pm.deletePersistent(file);
    }
    pm.close();
  }

  public static void batchDelete(String category, Long version) {
    PersistenceManager pm = PMF.get().getPersistenceManager();
    Query query = pm.newQuery(GAEFile.class);
    query.setFilter("cat == category && ver == version");
    query.declareParameters("String category, Long version");
    List<GAEFile> files = (List<GAEFile>) query.execute(category, version);
    for (int i = 0; i < files.size(); i++) {
      GAEFile file = files.get(i);
      for (int sNo = 0; sNo < file.getSegmentCount(); sNo++) {
        GAEFileContentJDO.delete(file.getId(), sNo);
      }
      pm.deletePersistent(file);
    }
  }
}
