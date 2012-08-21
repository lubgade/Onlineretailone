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

import javax.servlet.http.HttpServletRequest;

import org.apache.gaelucene.config.GAELuceneConfig;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * Permission/Authentication framework learned from Mvnforum 
 * @see http://www.mvnforum.com/
 * 
 * $Id: GAELuceneOnlineUserManager.java 23 2009-09-14 02:06:48Z lhelper $
 */
public class GAELuceneOnlineUserManager {
  private static GAELuceneOnlineUserManager instance = new GAELuceneOnlineUserManager();

  private GAELuceneOnlineUserManager() {
  }

  public static GAELuceneOnlineUserManager getInstance() {
    return instance;
  }

  public GAELuceneOnlineUser getOnlineUser(HttpServletRequest request) throws GAELuceneAuthException {
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();

    if (user == null) {
      throw new GAELuceneAuthException(GAELuceneAuthException.NOT_LOGIN);
    }

    GAELuceneOnlineUser onlineUser = getAuthenticatedUser(request, user, user.getEmail());

    // reserved user
    if (onlineUser == null) {
      if (GAELuceneReservedUsers.isReservedUser(user.getEmail())) {
        onlineUser = getReservedUser(request, user, user.getEmail());
      }
    }

    // not an authenticated user
    if (onlineUser == null) {
      if (GAELuceneConfig.enableGuest()) {
        onlineUser = getGuestUser(request, user, user.getEmail());
      } else {
        throw new GAELuceneAuthException(GAELuceneAuthException.NOT_LOGIN);
      }
    }

    return onlineUser;
  }

  private GAELuceneOnlineUser getAuthenticatedUser(HttpServletRequest request, User user, String email) {
    GAELuceneUser gaeuser = GAELuceneUserJDO.get(email);

    if (gaeuser == null) {
      return null;
    }

    GAELuceneOnlineUser authenticatedUser = new GAELuceneOnlineUser(request, false);
    authenticatedUser.setEmail(user.getEmail());
    authenticatedUser.setAuthDomain(user.getAuthDomain());
    authenticatedUser.setNickName(user.getNickname());

    GAELucenePermissionImpl permission = getAuthenticatedPermission(gaeuser);
    authenticatedUser.setPermission(permission);

    return authenticatedUser;
  }

  private GAELucenePermissionImpl getAuthenticatedPermission(GAELuceneUser gaeuser) {
    GAELucenePermissionImpl permission = new GAELucenePermissionImpl();
    ArrayList<Integer> permList = gaeuser.getPermissions();

    if (permList != null) {
      for (int i = 0, n = permList.size(); i < n; i++) {
        permission.setPermission(permList.get(i));
      }
    }

    permission.setPermission(GAELucenePermission.PERMISSION_AUTHENTICATED);

    return permission;
  }

  private GAELuceneOnlineUser getReservedUser(HttpServletRequest request, User user, String email) {
    GAELuceneOnlineUser reservedUser = new GAELuceneOnlineUser(request, false);
    reservedUser.setEmail(user.getEmail());
    reservedUser.setAuthDomain(user.getAuthDomain());
    reservedUser.setNickName(user.getNickname());

    GAELucenePermissionImpl permission = getResercedUserPermission();
    reservedUser.setPermission(permission);

    return reservedUser;
  }

  private GAELucenePermissionImpl getResercedUserPermission() {
    GAELucenePermissionImpl permission = new GAELucenePermissionImpl();
    permission.setPermission(GAELucenePermission.PERMISSION_SYSTEM_ADMIN);
    permission.setPermission(GAELucenePermission.PERMISSION_AUTHENTICATED);

    return permission;
  }

  private GAELuceneOnlineUser getGuestUser(HttpServletRequest request, User user, String email) {
    GAELuceneOnlineUser guestUser = new GAELuceneOnlineUser(request, true);
    guestUser.setEmail(user.getEmail());
    guestUser.setAuthDomain(user.getAuthDomain());
    guestUser.setNickName(user.getNickname());

    GAELucenePermissionImpl permission = getGuestUserPermission();
    guestUser.setPermission(permission);

    return guestUser;
  }

  private GAELucenePermissionImpl getGuestUserPermission() {
    GAELucenePermissionImpl permission = new GAELucenePermissionImpl();
    permission.setPermission(GAELucenePermission.PERMISSION_AUTHENTICATED);

    return permission;
  }

  public String createLoginURL(HttpServletRequest request) {
    return UserServiceFactory.getUserService().createLoginURL(request.getRequestURI());
  }
}
