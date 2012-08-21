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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.gaelucene.auth.GAELuceneAuthException;
import org.apache.gaelucene.auth.GAELuceneOnlineUser;
import org.apache.gaelucene.auth.GAELuceneOnlineUserManager;
import org.apache.gaelucene.auth.GAELucenePermission;
import org.apache.lucene.index.GAEIndexReaderPool;

public class IndexReaderPoolWebHandler {

  // index readers pool
  private static GAEIndexReaderPool readerPool = GAEIndexReaderPool.getInstance();

  private GAELuceneOnlineUserManager userManager = GAELuceneOnlineUserManager.getInstance();

  public void processShowPoolStats(HttpServletRequest request) throws GAELuceneAuthException, IOException {
    GAELuceneOnlineUser onlineUser = userManager.getOnlineUser(request);
    GAELucenePermission permission = onlineUser.getPermission();
    if (!permission.isAuthenticated()) {
      throw new GAELuceneAuthException(0);
    }

    long freeMemory = Runtime.getRuntime().freeMemory();
    long totalMemory = Runtime.getRuntime().totalMemory();
    long maxMemory = Runtime.getRuntime().maxMemory();
    request.setAttribute("Runtime.totalMemory", totalMemory);
    request.setAttribute("Runtime.maxMemory", maxMemory);
    request.setAttribute("Runtime.freeMemory", freeMemory);
    if (readerPool == null) {
      request.setAttribute("Pool.stat",
          "The pool HAS NOT BEEN initialized! Please check your log for detail!");
    } else {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      readerPool.showPoolStats(out);
      request.setAttribute("Pool.stat", new String(out.toByteArray(), "UTF-8"));
    }
  }
}
