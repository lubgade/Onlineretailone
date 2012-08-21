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

/**
 * Permission/Authentication framework learned from Mvnforum 
 * @see http://www.mvnforum.com/
 * 
 * $Id: GAELuceneAuthException.java 20 2009-09-14 02:02:57Z lhelper $
 */
public class GAELuceneAuthException extends Exception {

  public static final long serialVersionUID = 2009083000002l;

  public static final int NOT_LOGIN = 1;
  public static final int WRONG_MEMBER_NAME = 2;
  public static final int WRONG_MEMBER_PASSWORD = 3;
  public static final int ACCOUNT_DISABLED = 4;
  public static final int NOT_ENOUGH_RIGHTS = 5;

  private int exceptionReason = NOT_LOGIN;

  public GAELuceneAuthException(String msg) {
    super(msg);
  }

  public GAELuceneAuthException(int reason) {
    exceptionReason = reason;
  }

  public GAELuceneAuthException(int reason, String msg) {
    super(msg);
    exceptionReason = reason;
  }

  public int getReason() {
    return exceptionReason;
  }

  public String getReasonString() {
    switch (exceptionReason) {
    case NOT_LOGIN:
      return "NOT LOGIN";
    case WRONG_MEMBER_NAME:
      return "INVALID USER NAME";
    case WRONG_MEMBER_PASSWORD:
      return "INVALID PASSWORD";
    case ACCOUNT_DISABLED:
      return "ACCOUNT HAS BEEN DISABLED";
    case NOT_ENOUGH_RIGHTS:
      return "NOT ENOUGH RIGHTS";
    }

    return "UNKNOWN REASON";
  }

  public String getMessage() {
    if (super.getMessage() == null) {
      return getReasonString();
    }
    return super.getMessage();
  }
}
