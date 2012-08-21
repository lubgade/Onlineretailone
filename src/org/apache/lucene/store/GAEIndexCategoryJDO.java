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

import com.cloudjini.onlineretail.Dao.PMF;

public class GAEIndexCategoryJDO {
  public static void saveOrUpdate(String category, Long version, Long lastModified) {
    PersistenceManager pm = PMF.get().getPersistenceManager();
    Query query = pm.newQuery(GAEIndexCategory.class);
    query.setFilter("cat == category");
    query.declareParameters("String category");
    List<GAEIndexCategory> categories = (List<GAEIndexCategory>) query.execute(category);

    if (categories.size() == 0) {
      GAEIndexCategory entity = new GAEIndexCategory();
      entity.setCat(category);
      entity.setVer(version);
      entity.setLastModified(lastModified);
      pm.makePersistent(entity);
    } else {
      GAEIndexCategory entity = categories.get(0);
      entity.setVer(version);
      entity.setLastModified(lastModified);
    }
    pm.close();
  }

  public static void delete(Long catId) {
    PersistenceManager pm = PMF.get().getPersistenceManager();
    Query query = pm.newQuery(GAEIndexCategory.class);
    query.setFilter("id == catId");
    query.declareParameters("Long catId");
    List<GAEIndexCategory> categories = (List<GAEIndexCategory>) query.execute(new Long(catId));
    for (int i = 0; i < categories.size(); i++) {
      GAEIndexCategory category = categories.get(i);
      pm.deletePersistent(category);
    }
    pm.close();
  }
}
