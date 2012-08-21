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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Reserved users that defined in 'gaelucene-users.txt'. The reserved users act
 * as System Administrator.
 * 
 * $Id: GAELuceneReservedUsers.java 23 2009-09-14 02:06:48Z lhelper $
 */
public class GAELuceneReservedUsers {
  private static HashSet<String> reservedUsers = new HashSet<String>();

  static {
    File gaeluceneUsersFile = null;
    BufferedReader reader = null;

    try {
      gaeluceneUsersFile = new File(GAELuceneReservedUsers.class.getClassLoader().getResource(
          "gaelucene-users.txt").getFile());
      reader = new BufferedReader(new InputStreamReader(new FileInputStream(gaeluceneUsersFile), "UTF-8"));

      for (String line = reader.readLine(); line != null; line = reader.readLine()) {
        line = line.trim();

        if (line.startsWith("#")) {
          continue;
        }

        reservedUsers.add(line.toLowerCase());
      }
    } catch (IOException ioe) {
      System.err.println("[ERROR] - GAELuceneReservedUsers - FAILED to load reserved user list from '"
          + gaeluceneUsersFile.getAbsolutePath() + "', because:" + ioe);
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (Exception e) {
          // ignore
        }
      }
    }
  }

  public static boolean isReservedUser(String userEmail) {
    if (userEmail == null) {
      return false;
    }

    userEmail = userEmail.toLowerCase();

    return reservedUsers.contains(userEmail);
  }

  public static List<String> getReservedUsers() {
    ArrayList<String> users = new ArrayList<String>();

    for (Iterator<String> iter = reservedUsers.iterator(); iter.hasNext();) {
      users.add(iter.next());
    }

    return users;
  }
}
