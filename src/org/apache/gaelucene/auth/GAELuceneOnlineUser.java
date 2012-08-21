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

import javax.servlet.http.HttpServletRequest;

/**
 * Permission/Authentication framework learned from Mvnforum 
 * @see http://www.mvnforum.com/
 * 
 * $Id: GAELuceneOnlineUser.java 22 2009-09-14 02:04:55Z lhelper $
 */
public class GAELuceneOnlineUser {
  boolean isGuest;
  String email;
  String authDomain;
  String nickName;

  GAELucenePermission permission = null;

  GAELuceneOnlineUser(HttpServletRequest request, boolean isGuest) {
  }

  public boolean isGuest() {
    return isGuest;
  }

  public void setGuest(boolean isGuest) {
    this.isGuest = isGuest;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getAuthDomain() {
    return authDomain;
  }

  public void setAuthDomain(String authDomain) {
    this.authDomain = authDomain;
  }

  public String getNickName() {
    return nickName;
  }

  public void setNickName(String nickName) {
    this.nickName = nickName;
  }

  public GAELucenePermission getPermission() {
    return permission;
  }

  public void setPermission(GAELucenePermission permission) {
    this.permission = permission;
  }
}
