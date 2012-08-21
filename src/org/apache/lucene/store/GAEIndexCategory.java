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

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * GAEIndexCategory is the identifier of different indices.
 * 
 * $Id: GAEIndexCategory.java 25 2009-09-14 02:10:51Z lhelper $
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class GAEIndexCategory {
  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  private Long id;

  @Persistent
  private String cat;

  @Persistent
  private Long ver;

  @Persistent
  private Long lastModified;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCat() {
    return cat;
  }

  public void setCat(String cat) {
    this.cat = cat;
  }

  public Long getVer() {
    return ver;
  }

  public void setVer(Long ver) {
    this.ver = ver;
  }

  public Long getLastModified() {
    return lastModified;
  }

  public void setLastModified(Long lastModified) {
    this.lastModified = lastModified;
  }

  public boolean lessThan(GAEIndexCategory obj) {
    if (this.ver.longValue() < obj.ver.longValue()) {
      return true;
    }
    if (this.lastModified.longValue() < obj.lastModified.longValue()) {
      return true;
    }
    return false;
  }
}
