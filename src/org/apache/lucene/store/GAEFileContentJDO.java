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

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.adjointweb.onlineretail.Dao.PMF;

import com.google.appengine.api.datastore.Blob;

public class GAEFileContentJDO {
  //	public static GAEFileContent get(Long cId) {
  //		PersistenceManager pm = PMF.get().getPersistenceManager();
  //		Query query = pm.newQuery(GAEFileContent.class);
  //		query.setFilter("id == cId");
  //		query.declareParameters("Long cId");
  //		List<GAEFileContent> gaeContents = (List<GAEFileContent>)query.execute(cId);
  //		GAEFileContent gaeContent = null;
  //		if (gaeContents.size() > 0) {
  //			gaeContent = gaeContents.get(0);
  //		}
  //		pm.close();
  //		return gaeContent;
  //	}

  public static GAEFileContent get(Long fileId, Integer segmentNo) {
    PersistenceManager pm = PMF.get().getPersistenceManager();
    Query query = pm.newQuery(GAEFileContent.class);
    query.setFilter("fileId == fId && segmentNo == sNo");
    query.declareParameters("Long fId, Integer sNo");
    List<GAEFileContent> gaeContents = (List<GAEFileContent>) query.execute(fileId, segmentNo);
    GAEFileContent gaeContent = new GAEFileContent();
    if (gaeContents.size() > 0) {
      gaeContent = gaeContents.get(0);
      pm.retrieve(gaeContent);
    }
    pm.close();
    return gaeContent;
  }

  //	@Deprecated
  //	public static void saveOrUpdate(Long cId, Blob content) {
  //    	PersistenceManager pm = PMF.get().getPersistenceManager();
  //    	if (cId == null) {
  //			GAEFileContent gaeContent = new GAEFileContent();
  //			gaeContent.setContent(content);
  //			pm.makePersistent(gaeContent);
  //    	}
  //    	else {
  //    		Query query = pm.newQuery(GAEFileContent.class);
  //    		query.setFilter("id == cId");
  //    		query.declareParameters("Long cId");
  //    		List<GAEFileContent> gaeContents = (List<GAEFileContent>)query.execute(cId);
  //    		GAEFileContent gaeContent = null;
  //    		if (gaeContents.size() > 0) {
  //    			gaeContent = gaeContents.get(0);
  //    			gaeContent.setContent(content);
  //    		}
  //    	}
  //		pm.close();
  //	}

  public static void saveOrUpdate(Long fileId, Integer segmentNo, Long segmentLength, byte[] content) {
    PersistenceManager pm = PMF.get().getPersistenceManager();
    Query query = pm.newQuery(GAEFileContent.class);
    query.setFilter("fileId == fId && segmentNo == sNo");
    query.declareParameters("Long fId, Integer sNo");
    List<GAEFileContent> segments = (List<GAEFileContent>) query.execute(fileId, segmentNo);
    if (segments.size() == 0) {
      GAEFileContent gaeContent = new GAEFileContent();
      gaeContent.setFileId(fileId);
      gaeContent.setSegmentNo(segmentNo);
      gaeContent.setSegmentLength(segmentLength);
      gaeContent.setContent(new Blob(content));
      pm.makePersistent(gaeContent);
    } else {
      GAEFileContent gaeContent = segments.get(0);
      gaeContent.setSegmentLength(segmentLength);
      gaeContent.setContent(new Blob(content));
    }
    pm.close();
    ///String cacheId = gaeFile.getCacheId();
    ///log.info("trying to put byte data to memcache for '" + gaeFile.getName() + "' with id '" + cacheId + "'");
    ///try {
    ///	CF.get().createCache(Collections.emptyMap()).put(cacheId, content);
    ///	CF.getCache().put(cacheId, content);
    ///} catch (CacheException e) {
    ///	log.warning("failed to put byte data to memcache for '" + gaeFile.getName() + "' with id '" + cacheId + "', because:" + e);
    ///	e.printStackTrace();
    ///}
  }

  //	@Deprecated
  //	public static void delete(Long cId) {
  //    	PersistenceManager pm = PMF.get().getPersistenceManager();
  //		Query query = pm.newQuery(GAEFileContent.class);
  //		query.setFilter("id == cId");
  //		query.declareParameters("Long cId");
  //		List<GAEFileContent> gaeContents = (List<GAEFileContent>)query.execute(cId);
  //		GAEFileContent gaeContent = null;
  //		if (gaeContents.size() > 0) {
  //			gaeContent = gaeContents.get(0);
  //			pm.deletePersistent(gaeContent);
  //		}
  //		pm.close();
  //	}

  public static void delete(Long fileId, Integer segmentNo) {
    PersistenceManager pm = PMF.get().getPersistenceManager();
    Query query = pm.newQuery(GAEFileContent.class);
    query.setFilter("fileId == fId && segmentNo == sNo");
    query.declareParameters("Long fId, Integer sNo");
    List<GAEFileContent> gaeContents = (List<GAEFileContent>) query.execute(fileId, segmentNo);
    GAEFileContent gaeContent = null;
    if (gaeContents.size() > 0) {
      gaeContent = gaeContents.get(0);
      pm.deletePersistent(gaeContent);
    }
    pm.close();
  }
}
