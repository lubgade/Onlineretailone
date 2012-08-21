package org.apache.gaelucene.dashboard;

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

import javax.cache.CacheStatistics;
import javax.servlet.http.HttpServletRequest;

import org.apache.gaelucene.auth.GAELuceneAuthException;
import org.apache.gaelucene.auth.GAELuceneOnlineUser;
import org.apache.gaelucene.auth.GAELuceneOnlineUserManager;
import org.apache.gaelucene.auth.GAELucenePermission;

import com.adjointweb.onlineretail.Dao.CF;

public class GAELuceneCacheWebHandler {

  private GAELuceneOnlineUserManager userManager = GAELuceneOnlineUserManager.getInstance();

  public void processShowCacheStats(HttpServletRequest request) throws GAELuceneAuthException {
    GAELuceneOnlineUser onlineUser = userManager.getOnlineUser(request);
    GAELucenePermission permission = onlineUser.getPermission();
    if (!permission.isAuthenticated()) {
      throw new GAELuceneAuthException(0);
    }

    javax.cache.Cache cache = CF.getCache();
    CacheStatistics stats = cache.getCacheStatistics();
    int cacheSize = cache.size();
    int hits = stats.getCacheHits();
    int misses = stats.getCacheMisses();

    request.setAttribute("Cache.Size", cacheSize);
    request.setAttribute("CacheStatistics.Hits", hits);
    request.setAttribute("CacheStatistics.Misses", misses);
  }

  public void processClearCache(HttpServletRequest request) throws GAELuceneAuthException {
    GAELuceneOnlineUser onlineUser = userManager.getOnlineUser(request);
    GAELucenePermission permission = onlineUser.getPermission();
    if (!permission.isAuthenticated()) {
      throw new GAELuceneAuthException(0);
    }

    javax.cache.Cache cache = CF.getCache();
    cache.clear();
  }
}
