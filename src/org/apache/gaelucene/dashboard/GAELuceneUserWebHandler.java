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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.gaelucene.auth.GAELuceneAuthException;
import org.apache.gaelucene.auth.GAELuceneReservedUsers;
import org.apache.gaelucene.auth.GAELuceneOnlineUser;
import org.apache.gaelucene.auth.GAELuceneOnlineUserManager;
import org.apache.gaelucene.auth.GAELucenePermission;
import org.apache.gaelucene.auth.GAELuceneUser;
import org.apache.gaelucene.auth.GAELuceneUserJDO;

import com.liferay.util.ParamUtil;

public class GAELuceneUserWebHandler {
  private GAELuceneOnlineUserManager userManager = GAELuceneOnlineUserManager.getInstance();

  public void processListUser(HttpServletRequest request) throws GAELuceneAuthException {
    GAELuceneOnlineUser onlineUser = userManager.getOnlineUser(request);
    GAELucenePermission permission = onlineUser.getPermission();
    if (!permission.isAuthenticated()) {
      throw new GAELuceneAuthException(0);
    }

    List<GAELuceneUser> users = GAELuceneUserJDO.getUsers();
    request.setAttribute("users", users);

    List<String> reservedUsers = GAELuceneReservedUsers.getReservedUsers();
    request.setAttribute("reservedUsers", reservedUsers);
  }

  public void processAddUser(HttpServletRequest request) throws GAELuceneAuthException {
    GAELuceneOnlineUser onlineUser = userManager.getOnlineUser(request);
    GAELucenePermission permission = onlineUser.getPermission();
    if (!permission.canAdminSystem()) {
      throw new GAELuceneAuthException(0);
    }
    String email = ParamUtil.getString(request, "email");
    GAELuceneUserJDO.saveOrUpdate(email, new ArrayList<Integer>());
  }

  public void prepareEditUserPermission(HttpServletRequest request) throws GAELuceneAuthException {
    GAELuceneOnlineUser onlineUser = userManager.getOnlineUser(request);
    GAELucenePermission permission = onlineUser.getPermission();
    if (!permission.canAdminSystem()) {
      throw new GAELuceneAuthException(0);
    }
    long uId = ParamUtil.getLong(request, "uid", 0);
    GAELuceneUser oneUser = GAELuceneUserJDO.get(uId);
    request.setAttribute("user", oneUser);
  }

  public void processUpdateUserPermission(HttpServletRequest request) throws GAELuceneAuthException {
    GAELuceneOnlineUser onlineUser = userManager.getOnlineUser(request);
    GAELucenePermission permission = onlineUser.getPermission();
    if (!permission.canAdminSystem()) {
      throw new GAELuceneAuthException(0);
    }
    long uId = ParamUtil.getLong(request, "uid", 0);
    ArrayList<Integer> permissions = (ArrayList<Integer>) ParamUtil.getIntegers(request, "perm");
    GAELuceneUserJDO.updatePermission(uId, permissions);
  }

  public void processDeleteUser(HttpServletRequest request) throws GAELuceneAuthException {
    GAELuceneOnlineUser onlineUser = userManager.getOnlineUser(request);
    GAELucenePermission permission = onlineUser.getPermission();
    if (!permission.canAdminSystem()) {
      throw new GAELuceneAuthException(0);
    }
    long uId = ParamUtil.getLong(request, "uid", 0);
    GAELuceneUserJDO.delete(new Long(uId));
  }
}
