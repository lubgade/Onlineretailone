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

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * User Entity
 * 
 * $Id: GAELuceneUser.java 23 2009-09-14 02:06:48Z lhelper $
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class GAELuceneUser {
  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  private Long uId;

  @Persistent
  private String email;

  @Persistent
  private ArrayList<Integer> permissions;

  public Long getUId() {
    return uId;
  }

  public void setUId(Long uId) {
    this.uId = uId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public ArrayList<Integer> getPermissions() {
    return permissions;
  }

  public void setPermissions(ArrayList<Integer> permissions) {
    this.permissions = permissions;
  }
}
