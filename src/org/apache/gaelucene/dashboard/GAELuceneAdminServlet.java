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

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GAELuceneAdminServlet extends HttpServlet {
  public static final long serialVersionUID = 1l;

  // global logger
  private static Logger log = Logger.getLogger(GAELuceneAdminServlet.class.getName());

  private static final long START_TIME = System.currentTimeMillis();

  private AdminModuleProcessor adminModuleProcessor = null;

  private int processCount = 0;

  public static final long getStartTime() {
    return START_TIME;
  }

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    adminModuleProcessor = new AdminModuleProcessor(this);
    log.info("<---- " + GAELuceneAdminServlet.class.getName() + " has been inited. ---->");
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException,
      ServletException {
    process(request, response);
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException,
      ServletException {
    process(request, response);
  }

  public void process(HttpServletRequest request, HttpServletResponse response) throws IOException,
      ServletException {
    processCount++;
    long timeStart = 0;
    if (log.isLoggable(Level.INFO)) {
      timeStart = System.currentTimeMillis();
    }
    try {
      String responseURI = adminModuleProcessor.process(request, response);
      if (responseURI != null && !response.isCommitted()) {
        if (responseURI.endsWith(".jsp")) {
          request.getRequestDispatcher(responseURI).forward(request, response);
        } else {
          response.sendRedirect(responseURI);
        }
      }
    } catch (Exception e) {
      log.warning("Unhandled exception:" + e);
    }
    if (log.isLoggable(Level.INFO)) {
      long timeUsed = System.currentTimeMillis() - timeStart;
      log.info("GAELuceneAdminServlet - no." + processCount + ", " + timeUsed + " ms used.");
    }
  }

  @Override
  public void destroy() {
    super.destroy();
    log.info("<---- " + GAELuceneAdminServlet.class.getName() + " has been destroied. ---->");
  }

}