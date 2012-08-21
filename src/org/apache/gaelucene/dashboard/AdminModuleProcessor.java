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

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.gaelucene.auth.GAELuceneAuthException;
import org.apache.gaelucene.auth.GAELuceneOnlineUser;
import org.apache.gaelucene.auth.GAELuceneOnlineUserManager;
import org.apache.gaelucene.config.GAELuceneConfig;

public class AdminModuleProcessor {
  // global logger
  private static Logger log = Logger.getLogger(AdminModuleProcessor.class.getName());

  private HttpServlet adminServlet = null;
  private ServletContext servletContext = null;
  private GAELuceneOnlineUserManager userManager = GAELuceneOnlineUserManager.getInstance();

  GAELuceneIndexWebHandler indexWebHandler = new GAELuceneIndexWebHandler();
  IndexReaderPoolWebHandler poolWebHandler = new IndexReaderPoolWebHandler();
  GAELuceneCacheWebHandler cacheWebHandler = new GAELuceneCacheWebHandler();
  GAELuceneUserWebHandler userWebHandler = new GAELuceneUserWebHandler();

  public AdminModuleProcessor(HttpServlet servlet) {
    adminServlet = servlet;
    servletContext = adminServlet.getServletContext();
    servletContext.getContextPath();
  }

  public String process(HttpServletRequest request, HttpServletResponse response) throws IOException,
      ServletException {
    String userEmail = null;
    long timeStart = 0;
    if (log.isLoggable(Level.INFO)) {
      timeStart = System.currentTimeMillis();
    }
    String pathInfo = request.getPathInfo();
    if (pathInfo == null) {
      pathInfo = "";
    }

    GAELuceneOnlineUser onlineUser = null;
    try {
      onlineUser = userManager.getOnlineUser(request);
    } catch (GAELuceneAuthException ae) {
      response.sendRedirect(userManager.createLoginURL(request));
      return null;
    }
    if (log.isLoggable(Level.INFO)) {
      userEmail = onlineUser.getEmail();
      log.info("AdminModuleProcessor - user(" + userEmail + ") - pathInfo=" + pathInfo);
    }

    String responseURI = null;
    try {
      // list indices files
      if ("/list".equals(pathInfo)) {
        indexWebHandler.processListGAEFiles(request);
        responseURI = "/dashboard/gaelucene/listgaefile.jsp";
      }
      // register new index file
      else if ("/registernewindexfile".equals(pathInfo)) {
        indexWebHandler.processRegisterNewFile(request, response);
      }
      // commit file segments
      else if ("/commitnewindexfile".equals(pathInfo)) {
        indexWebHandler.processCommitNewFile(request, response);
      }
      // active the new index
      else if ("/activatenewindex".equals(pathInfo)) {
        indexWebHandler.processActivateNewIndex(request, response);
      }
      // import packaged indices onto google datastore
      else if ("/importpackagedindex".equals(pathInfo)) {
        indexWebHandler.processImportPrepackagedIndex(request);
        responseURI = GAELuceneConfig.getUrlPattern() + "/list";
      }
      // delete the specified GAEFile entity
      else if ("/deletefileprocess".equals(pathInfo)) {
        indexWebHandler.deleteGAEFile(request);
        responseURI = GAELuceneConfig.getUrlPattern() + "/list";
      }
      // delete the specified GAECategory entity
      else if ("/deletecategoryprocess".equals(pathInfo)) {
        indexWebHandler.deleteGAECategory(request);
        responseURI = GAELuceneConfig.getUrlPattern() + "/list";
      }
      // delete a set of indices batchly
      else if ("/batchdeletefileprocess".equals(pathInfo)) {
        indexWebHandler.batchDeleteGAEFiles(request);
        responseURI = GAELuceneConfig.getUrlPattern() + "/list";
      }
      // show pool information
      else if ("/showpoolstat".equals(pathInfo)) {
        poolWebHandler.processShowPoolStats(request);
        responseURI = "/dashboard/gaelucene/showpoolstats.jsp";
      }
      // show cache statistics
      else if ("/showcachestat".equals(pathInfo)) {
        cacheWebHandler.processShowCacheStats(request);
        responseURI = "/dashboard/gaelucene/showcachestats.jsp";
      }
      // clear cached objects
      else if ("/clearcache".equals(pathInfo)) {
        cacheWebHandler.processShowCacheStats(request);
        responseURI = GAELuceneConfig.getUrlPattern() + "/showcachestat";
      }
      // list users
      else if ("/listusers".equals(pathInfo)) {
        userWebHandler.processListUser(request);
        responseURI = "/dashboard/gaelucene/listgaeuser.jsp";
      }
      // add user process
      else if ("/adduserprocess".equals(pathInfo)) {
        userWebHandler.processAddUser(request);
        responseURI = GAELuceneConfig.getUrlPattern() + "/listusers";
      }
      // edit user permission
      else if ("/edituserpermission".equals(pathInfo)) {
        userWebHandler.prepareEditUserPermission(request);
        responseURI = "/dashboard/gaelucene/edituserpermission.jsp";
      }
      // update user permission
      else if ("/updateuserpermissionprocess".equals(pathInfo)) {
        userWebHandler.processUpdateUserPermission(request);
        responseURI = GAELuceneConfig.getUrlPattern() + "/listusers";
      }
      // add user process
      else if ("/deleteruserprocess".equals(pathInfo)) {
        userWebHandler.processDeleteUser(request);
        responseURI = GAELuceneConfig.getUrlPattern() + "/listusers";
      }
      // show index page
      else {
        responseURI = "/dashboard/gaelucene/index.jsp";
      }
    } catch (GAELuceneAuthException ae) {
      response.sendRedirect(userManager.createLoginURL(request));
      return null;
    } catch (Exception e) {
      request.setAttribute("requestURI", GAELuceneConfig.getUrlPattern() + pathInfo);
      request.setAttribute("exception", e);
      responseURI = "/dashboard/gaelucene/error.jsp";
    }

    if (log.isLoggable(Level.INFO)) {
      long timeUsed = System.currentTimeMillis() - timeStart;
      log.info("AdminModuleProcessor - user(" + userEmail + ") - responseURI=" + responseURI + ", " + timeUsed
          + " ms used.");
    }
    return responseURI;
  }
}
