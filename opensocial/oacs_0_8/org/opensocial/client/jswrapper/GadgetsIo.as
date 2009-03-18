/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.opensocial.client.jswrapper {

import org.opensocial.client.base.ConstType;

/**
 * Wrapper of <code><j>gadgets.io</j></code> namespace in javascript. It contains some useful static
 * functions for handling the io package.
 * 
 * @see http://code.google.com/apis/opensocial/docs/0.8/reference/gadgets/#gadgets.io gadgets.io
 * @author yiziwu@google.com (Yizi Wu)
 */
final public class GadgetsIo {
  /**
   * <code><j>gadgets.io.RequestParameters</j></code> constants.
   * @see http://code.google.com/apis/opensocial/docs/0.8/reference/gadgets/#gadgets.io.RequestParameters
   *      gadgets.io.RequestParameters
   */ 
  public static const RequestParameters:ConstType = new ConstType(
      "gadgets.io.RequestParameters", {
          METHOD                        : "METHOD",             /* GadgetsIo.MethodType */
          CONTENT_TYPE                  : "CONTENT_TYPE",       /* GadgetsIo.ContentType */
          POST_DATA                     : "POST_DATA",
          HEADERS                       : "HEADERS",
          REFRESH_INTERVAL              : "REFRESH_INTERVAL",   /* Number */
          AUTHORIZATION                 : "AUTHORIZATION",      /* GadgetsIo.AuthorizationType */
          NUM_ENTRIES                   : "NUM_ENTRIES",        /* Number */
          GET_SUMMARIES                 : "GET_SUMMARIES"      /* Boolean */
      });

  /**
   * <code><j>gadgets.io.MethodType</j></code> constants.
   * @see http://code.google.com/apis/opensocial/docs/0.8/reference/gadgets/#gadgets.io.MethodType 
   *      gadgets.io.MethodType
   */ 
  public static const MethodType:ConstType = new ConstType(
      "gadgets.io.MethodType", {
          GET     : "GET",
          POST    : "POST",
          PUT     : "PUT",
          DELETE  : "DELETE",
          HEAD    : "HEAD"
      });
      
  /**
   * <code><j>gadgets.io.ContentType</j></code> constants.
   * No DOM type in Actionscript.
   * @see http://code.google.com/apis/opensocial/docs/0.8/reference/gadgets/#gadgets.io.ContentType
   *      gadgets.io.ContentType
   */ 
  public static const ContentType:ConstType = new ConstType(
      "gadgets.io.ContentType", {
          TEXT     : "TEXT",
          JSON     : "JSON",
          FEED     : "FEED"
      });

  /**
   * <code><j>gadgets.io.AuthorizationType</j></code> constants.
   * @see http://code.google.com/apis/opensocial/docs/0.8/reference/gadgets/#gadgets.io.AuthorizationType
   *      gadgets.io.AuthorizationType
   */ 
  public static const AuthorizationType:ConstType = new ConstType(
      "gadgets.io.AuthorizationType", {
          NONE      : "NONE",
          SIGNED    : "SIGNED",
          OAUTH     : "OAUTH"
      });

  /**
   * Prepare a makeRequest parameter object of GET method. Keys are defined in 
   * <code>GadgetsIo.RequestParameters</code>.
   * 
   * @param contentType The value for <code>GadgetsIo.RequestParameters.CONTENT_TYPE</code>, 
   *                    default is <code>GadgetsIo.ContentType.TEXT</code>.
   * @param authType The value for <code>GadgetsIo.RequestParameters.AUTHORIZATION</code>, 
   *                 default is <code>GadgetsIo.ContentType.NONE</code>.
   * @param headers The header string.
   * @return The param object.
   */
  public static function newGetRequestParams(contentType:String = null,
                                             authType:String = null,
                                             headers:String = null):Object {
    var params:Object = {};
    params[RequestParameters.METHOD] = MethodType.GET;
    
    if (contentType != null && ContentType.valueOf(contentType) != null) {
      params[RequestParameters.CONTENT_TYPE] = contentType;
    }
    if (authType != null && AuthorizationType.valueOf(authType) != null) {
      params[RequestParameters.AUTHORIZATION] = authType;
    }
    if (headers != null) {
      params[RequestParameters.HEADERS] = headers;
    }
    return params;
  }
  
  /**
   * Prepare a makeRequest parameter object of POST method. Keys are defined in 
   * <code>GadgetsIo.RequestParameters</code>.
   * 
   * @param postData An object to post. Will be encoded on JS-side.
   * @param contentType The value for <code>GadgetsIo.RequestParameters.CONTENT_TYPE</code>, 
   *                    default is <code>GadgetsIo.ContentType.TEXT</code>.
   * @param authType The value for <code>GadgetsIo.RequestParameters.AUTHORIZATION</code>, 
   *                 default is <code>GadgetsIo.ContentType.NONE</code>.
   * @param headers The header string.
   * @return The param object.
   */
  public static function newPostRequestParams(postData:Object,
                                              contentType:String = null,
                                              authType:String = null,
                                              headers:String = null):Object {
    var params:Object = {};
    params[RequestParameters.METHOD] = MethodType.POST;
    params[RequestParameters.POST_DATA] = postData;
    
    if (contentType != null && ContentType.valueOf(contentType) != null) {
      params[RequestParameters.CONTENT_TYPE] = contentType;
    }
    if (authType != null && AuthorizationType.valueOf(authType) != null) {
      params[RequestParameters.AUTHORIZATION] = authType;
    }
    if (headers != null) {
      params[RequestParameters.HEADERS] = headers;
    }
    return params;
  }
}
}
