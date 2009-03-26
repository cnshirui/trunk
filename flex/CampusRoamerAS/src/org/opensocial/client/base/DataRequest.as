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
package org.opensocial.client.base {

/**
 * Wrapper of <code><j>opensocial.DataRequest</j></code> object in javascript.
 * <p>
 * This class is just used as a namespace for these constants.
 * Different from the javascript API, the data request real logic are implemented seperately in
 * <code>org.opensocial.client.jswrapper.OpensocialClient</code> class and restful client 
 * class (not exist yet).
 * </p>
 * <p>
 * TODO In future, batch fetch should be supported. So this class needs to be expanded.
 * </p>
 * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.DataRequest
 *      opensocial.DataRequest
 * 
 * @author yiziwu@google.com (Yizi Wu)
 */
public class DataRequest extends BaseType {
  /**
   * <code><j>opensocial.DataRequest.SortOrder</j></code> constants.
   * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.DataRequest.SortOrder
   *      opensocial.DataRequest.SortOrder
   */ 
  public static const SortOrder:ConstType = new ConstType(
      "opensocial.DataRequest.SortOrder", {
          TOP_FRIENDS       : 'topFriends',
          NAME              : 'name'
      });

  /**
   * <code><j>opensocial.DataRequest.FilterType</j></code> constants.
   * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.DataRequest.FilterType
   *      opensocial.DataRequest.FilterType
   */ 
  public static const FilterType:ConstType = new ConstType(
      "opensocial.DataRequest.FilterType", {
          ALL               : 'all',
          HAS_APP           : 'hasApp',
          TOP_FRIENDS       : 'topFriends',
          IS_FRIENDS_WITH   : 'isFriendsWith'
      });

  /**
   * <code><j>opensocial.DataRequest.DataRequestFields</j></code> constants.
   * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.DataRequest.DataRequestFields
   *      opensocial.DataRequest.DataRequestFields
   */ 
  public static const DataRequestFields:ConstType = new ConstType(
      "opensocial.DataRequest.DataRequestFields", {
          ESCAPE_TYPE     : 'escapeType'
      });

  /**
   * <code><j>opensocial.DataRequest.PeopleRequestFields</j></code> constants.
   * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.DataRequest.PeopleRequestFields
   *      opensocial.DataRequest.PeopleRequestFields
   */ 
  public static const PeopleRequestFields:ConstType = new ConstType(
      "opensocial.DataRequest.PeopleRequestFields", {
          PROFILE_DETAILS   : 'profileDetail',      /* Array.<Person.Field> */
          SORT_ORDER        : 'sortOrder',          /* DataRequest.SortOrder */
          FILTER            : 'filter',             /* DataRequest.FilterType */
          FILTER_OPTIONS    : 'filterOptions',      /* Map.<String, Object> */
          FIRST             : 'first',              /* Number */
          MAX               : 'max'                 /* Number */
      });

  /**
   * <code><j>opensocial.DataRequest.ActivityRequestFields</j></code> constants.
   * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.DataRequest.ActivityRequestFields
   *      opensocial.DataRequest.ActivityRequestFields
   */ 
  public static const ActivityRequestFields:ConstType = new ConstType(
      "opensocial.DataRequest.ActivityRequestFields", {
          APP_ID            : 'appId',
          FIRST             : 'first',              /* Number */
          MAX               : 'max'                 /* Number */
      });


  /**
   * Prepare a people request parameter object. Keys are defined in 
   * <code>DataRequest.PeopleRequestFields</code>.
   * 
   * @param first The value for <code>DataRequest.PeopleRequestFields.FIRST</code>, default is 0.
   * @param max The value for <code>DataRequest.PeopleRequestFields.MAX</code>, default is 20.
   * @param details The value for <code>DataRequest.PeopleRequestFields.PROFILE_DETAILST</code>, 
   *                which is an array of <code>Person.Field</code>. The default in javascript api 
   *                are the person's id, name and thumbnail url. So no need to pass these three 
   *                fields here.
   * @param sortOrder The value for <code>DataRequest.PeopleRequestFields.SORT_ORDERT</code>.
   * @param filter The value for <code>DataRequest.PeopleRequestFields.FILTERT</code>.
   * @return The param object.
   */
  public static function newPeopleRequestParams(first:int = 0,
                                                max:int = 20,
                                                details:Array = null,
                                                sortOrder:String = null,
                                                filter:String = null):Object {
    var params:Object = {};
    params[PeopleRequestFields.FIRST] = first;
    params[PeopleRequestFields.MAX] = max;
    
    if (details) {
      params[PeopleRequestFields.PROFILE_DETAILS] = details;
    }

    if (sortOrder) {
      params[PeopleRequestFields.SORT_ORDER] = sortOrder;
    }

    if (filter) {
      params[PeopleRequestFields.FILTER] = filter;
    }

    return params;
  }
  
  
  /**
   * Prepare an activity request parameter object. Keys are defined in 
   * <code>DataRequest.ActivityRequestFields</code>.
   * 
   * @param first The value for <code>DataRequest.ActivityRequestFields.FIRST</code>, default is 0.
   * @param max The value for <code>DataRequest.ActivityRequestFields.MAX</code>, default is 20.
   * @return The param object.
   */
  public static function newActivityRequestParams(first:int = 0,
                                                  max:int = 20):Object {
    var params:Object = {};
    params[ActivityRequestFields.FIRST] = first;
    params[ActivityRequestFields.MAX] = max;
    
    return params;
  }
  
    /**
   * Prepare an data request parameter object. Keys are defined in 
   * <code>DataRequest.DataRequestFields</code>.
   * 
   * @param escapeType The value for <code>DataRequest.DataRequestFields.ESCAPE_TYPE</code>, 
   *        defined in <code>Globals.EscapeType</code>.
   * @return The param object.
   */
  public static function newDataRequestParams(escapeType:String = null):Object {
    var params:Object = {};
    if (escapeType) {
      params[DataRequestFields.ESCAPE_TYPE] = escapeType;
    }
    return params;
  }

}
}
