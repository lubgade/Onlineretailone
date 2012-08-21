package org.apache.gaelucene.auth;

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

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.cloudjini.onlineretail.Dao.PMF;

/**
 * User Entity JDO
 * 
 * $Id: GAELuceneUserJDO.java 23 2009-09-14 02:06:48Z lhelper $
 */
public class GAELuceneUserJDO {
  public static GAELuceneUser get(Long uId) {
    PersistenceManager pm = PMF.get().getPersistenceManager();
    Query query = pm.newQuery(GAELuceneUser.class);
    query.setFilter("uId == UID");
    query.declareParameters("Long UID");

    List<GAELuceneUser> users = (List<GAELuceneUser>) query.execute(uId);
    GAELuceneUser user = null;

    if (users.size() > 0) {
      user = users.get(0);
      pm.retrieve(user);
    }

    pm.close();

    return user;
  }

  public static GAELuceneUser get(String email) {
    PersistenceManager pm = PMF.get().getPersistenceManager();
    Query query = pm.newQuery(GAELuceneUser.class);
    query.setFilter("email == EMAIL");
    query.declareParameters("String EMAIL");

    List<GAELuceneUser> users = (List<GAELuceneUser>) query.execute(email);
    GAELuceneUser user = null;

    if (users.size() > 0) {
      user = users.get(0);
    }

    pm.close();

    return user;
  }

  public static List<GAELuceneUser> getUsers() {
    PersistenceManager pm = PMF.get().getPersistenceManager();
    Query query = pm.newQuery(GAELuceneUser.class);
    List<GAELuceneUser> users = (List<GAELuceneUser>) query.execute();
    pm.detachCopyAll(users);
    pm.close();

    return users;
  }

  public static void saveOrUpdate(String email, ArrayList<Integer> permissions) {
    if (GAELuceneReservedUsers.isReservedUser(email)) {
      return;
    }

    PersistenceManager pm = PMF.get().getPersistenceManager();
    Query query = pm.newQuery(GAELuceneUser.class);
    query.setFilter("email == EMAIL");
    query.declareParameters("String EMAIL");

    List<GAELuceneUser> users = (List<GAELuceneUser>) query.execute(email);

    if (users.size() == 0) {
      GAELuceneUser user = new GAELuceneUser();
      user.setEmail(email);
      user.setPermissions(permissions);
      pm.makePersistent(user);
    }

    pm.close();
  }

  public static void updatePermission(Long uId, ArrayList<Integer> permissions) {
    PersistenceManager pm = PMF.get().getPersistenceManager();
    Query query = pm.newQuery(GAELuceneUser.class);
    query.setFilter("uId == UID");
    query.declareParameters("Long UID");

    List<GAELuceneUser> users = (List<GAELuceneUser>) query.execute(uId);

    if (users.size() > 0) {
      GAELuceneUser user = users.get(0);
      user.setPermissions(permissions);
    }

    pm.close();
  }

  public static void delete(Long uId) {
    PersistenceManager pm = PMF.get().getPersistenceManager();
    Query query = pm.newQuery(GAELuceneUser.class);
    query.setFilter("uId == UID");
    query.declareParameters("Long UID");

    List<GAELuceneUser> users = (List<GAELuceneUser>) query.execute(uId);
    GAELuceneUser user = null;

    if (users.size() > 0) {
      user = users.get(0);
      pm.deletePersistent(user);
    }

    pm.close();
  }
}
