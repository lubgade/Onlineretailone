package org.apache.lucene.index;

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

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.adjointweb.onlineretail.Dao.PMF;

import org.apache.lucene.store.GAEDirectory;
import org.apache.lucene.store.GAEIndexCategory;

/**
 * Pool for {@link GAEIndexReader}
 * $Id: GAEIndexReaderPool.java 26 2009-09-14 02:11:18Z lhelper $
 */
public class GAEIndexReaderPool {
  // global logger
  private static Logger log = Logger.getLogger(GAEIndexReaderPool.class.getName());

  /*
  * the complex index readers, whose key is the mount-point
  */
  HashMap<String, GAEIndexReader> cachedReaders;

  HashMap<String, GAEIndexCategory> cachedCategories;

  HashMap<String, GAEDirectory> cachedDirectories;

  /*
   * the singleton index reader pool instance
   */
  private static GAEIndexReaderPool pool = new GAEIndexReaderPool();

  private GAEIndexReaderPool() {
    init();
  }

  private void init() {
    reinit();
  }

  public void reinit() {
    if (cachedReaders != null) {
      cachedReaders.clear();
    } else {
      cachedReaders = new HashMap<String, GAEIndexReader>();
    }
    if (cachedCategories != null) {
      cachedCategories.clear();
    } else {
      cachedCategories = new HashMap<String, GAEIndexCategory>();
    }
    if (cachedDirectories != null) {
      cachedDirectories.clear();
    } else {
      cachedDirectories = new HashMap<String, GAEDirectory>();
    }
  }

  public static GAEIndexReaderPool getInstance() {
    return pool;
  }

  private static SimpleDateFormat logDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  // instance a index reader and keep it in a hashtable
  public synchronized GAEIndexReader borrowReader(String category) throws IOException {
    log.info("trying to verify index '" + category + "' version");
    PersistenceManager pm = PMF.get().getPersistenceManager();
    Query query = pm.newQuery(GAEIndexCategory.class);
    query.setFilter("cat == category");
    query.declareParameters("String category");
    query.setOrdering("lastModified desc");
    List<GAEIndexCategory> entities = (List<GAEIndexCategory>) query.execute(category);
    if (entities.size() <= 0) {
      return null;
    }
    GAEIndexCategory dbCategory = entities.get(0);
    Long version = dbCategory.getVer();
    GAEIndexCategory cachedCategory = cachedCategories.get(category);
    if (cachedCategory == null || cachedCategory.lessThan(dbCategory)) {
      if (cachedCategory == null) {
        log.info("trying to init index reader for '" + category + "', version=" + version);
      } else {
        log.info("trying to reinit index reader for '" + category + "' version=" + version);
      }

      long timeStart = System.currentTimeMillis();
      //System.out.println(GAEIndexReaderPool.class.getClassLoader().getResource("indices/" + category + "/index").getFile());
      //File index = new File(GAEIndexReaderPool.class.getClassLoader().getResource("indices/" + category + "/index").getFile());
      GAEDirectory directory = cachedDirectories.get(category);
      if (directory == null) {
        log.warning("trying to instance a directory for '" + category + "' version=" + version);
        directory = new GAEDirectory(category, version);
        cachedDirectories.put(category, directory);
      }

      try {
        GAEIndexReader reader = GAEIndexReader.getReader(directory);
        long timeEnd = System.currentTimeMillis();
        log.warning("'" + category + "' index reader (re)inited, " + (timeEnd - timeStart) + " ms used.");
        cachedReaders.put(category, reader);
        cachedCategories.put(category, dbCategory);
      } catch (Exception e) {
        log.warning("failed to (re)init reader for '" + category + ", version=" + version + ", because:" + e);
        /*StackTraceElement[] stes = e.getStackTrace();
        for (int i = 0; i < stes.length; i++) {
          log.warning(stes[i].toString());
        }*/
        e.printStackTrace();
        return null;
      }
    }

    GAEIndexReader reader = cachedReaders.get(category);
    reader.borrow();
    return reader;
  }

  public synchronized void returnReader(GAEIndexReader indexReader) throws IOException {
    indexReader.returnBack();
  }

  // reinstance the index reader and update the hashtable entity
  public synchronized void reloadReader(String category, long version) throws IOException {
    GAEIndexReader oldReader = cachedReaders.get(category);

    GAEDirectory directory = new GAEDirectory(category, new Long(version));
    GAEIndexReader newReader = GAEIndexReader.getReader(directory);
    cachedReaders.put(category, newReader);

    if (oldReader != null) {
      oldReader.destory();
    }
    return;
  }

  static SimpleDateFormat lastUsedDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  public void showPoolStats(OutputStream out) throws IOException {
    log.info("print pooled readers stats ...");
    if (cachedReaders.size() == 0) {
      out.write("The pool is empty!".getBytes());
      return;
    }
    int i = 0;
    for (Iterator<String> iter = cachedReaders.keySet().iterator(); iter.hasNext();) {
      i++;
      String category = iter.next();
      GAEIndexReader indexReader = cachedReaders.get(category);
      StringBuffer statsInfo = new StringBuffer(128);
      statsInfo.append("cat:").append(category).append(";");
      if (indexReader == null) {
        statsInfo.append("\tNA\n");
      } else {
        long createdTime = indexReader.getCreatedTime();
        boolean isClosed = indexReader.isClosed();
        int refCurCount = indexReader.getRefCurCount();
        int refTotalCount = indexReader.getRefTotalCount();
        long lastUsed = indexReader.getLastUsed();
        statsInfo.append("\tisClosed=").append(isClosed).append(";");
        statsInfo.append("\trefCount=").append(refTotalCount).append("/").append(refCurCount).append(";");
        statsInfo.append("\tcreatedTime=").append(lastUsedDateFormat.format(new Date(createdTime))).append(
            ";");
        statsInfo.append("\tlastUsed=").append(
            (lastUsed == 0) ? "NA" : (lastUsedDateFormat.format(new Date(lastUsed))));
        statsInfo.append("\n");
      }
      out.write(statsInfo.toString().getBytes());
    }
  }

  //	// 检查线程的运行状态
  //	class IndexChangeTask extends TimerTask {
  //		private GAEIndexReaderPool readerPool;
  //		private String category;
  //		private Long version;
  //		private Long lastModified;
  //		public IndexChangeTask(GAEIndexReaderPool readerPool, String category, Long version, Long lastModified) {
  //			this.readerPool = readerPool;
  //			this.category = category;
  //			this.version = version;
  //			this.lastModified = lastModified;
  //		}
  //
  //		@Override
  //		public void run() {
  //			System.out.println("check whether the index '" + this.category + "' changed?");
  //	        PersistenceManager pm = PMF.get().getPersistenceManager();
  //	        Query query = pm.newQuery(GAEIndexCategory.class);
  //	        query.setFilter("cat == category");
  //	        query.declareParameters("String category");
  //	        query.setOrdering("lastModified desc");
  //	        List<GAEIndexCategory> indexEntities = (List<GAEIndexCategory>) query.execute(category);
  //	        if (indexEntities.size() <= 0) {
  //	        	return;
  //	        }
  //	        pm.close();
  //	        Long version = indexEntities.get(0).getVer();
  //	        Long lastModified = indexEntities.get(0).getLastModified();
  //	        
  //	        if (lastModified.longValue() > this.lastModified.longValue()) {
  //	        	System.out.println("yes, the index '" + this.category + "' was changed.");
  //	        	try {
  //					this.readerPool.reloadReader(category, version);
  //				} catch (IOException e) {
  //					e.printStackTrace();
  //					return;
  //				}
  //	        	this.version = version;
  //	        	this.lastModified = lastModified;
  //	        }
  //	        else {
  //	        	System.out.println("no, the index '" + this.category + "' was not changed.");
  //	        }
  //		}
  //	}
}
