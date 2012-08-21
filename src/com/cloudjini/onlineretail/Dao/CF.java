package com.cloudjini.onlineretail.Dao;

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

import java.util.HashMap;
import java.util.logging.Logger;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;

import com.google.appengine.api.memcache.stdimpl.GCacheFactory;

public class CF {
  // global logger
  private static Logger log = Logger.getLogger(CF.class.getName());

  private static CacheFactory cacheFactory = null;
  private static Cache cache = null;
  static {
    try {
      cacheFactory = CacheManager.getInstance().getCacheFactory();

      HashMap props = new HashMap();
      props.put(GCacheFactory.EXPIRATION_DELTA, 3600 * 24 * 7);
      ///props.put(MemcacheService.SetPolicy.SET_ALWAYS, true);
      //System.out.println("cacheFactory:" + cacheFactory.getClass().getName());
      cache = cacheFactory.createCache(props);
    } catch (CacheException e) {
      log.warning("FAILED TO CREATE CACHE INSTANCE!, BECAUSE:" + e);
      e.printStackTrace();
    }
  }

  private CF() {
  }

  public static CacheFactory get() {
    return cacheFactory;
  }

  public static Cache getCache() {
    return cache;
  }
}
