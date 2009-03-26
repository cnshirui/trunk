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
 * Wrapper of <code><j>opensocial.ResponseItem</j></code> object in javascript.
 * <p>
 * This class is just used as a namespace for these constants.
 * Real logic are implemented seperately.
 * </p>
 * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.ResponseItem
 *      opensocial.ResponseItem
 * 
 * @author yiziwu@google.com (Yizi Wu)
 */
public class ResponseItem extends BaseType {

  /**
   * <code><j>opensocial.ResponseItem.Error</j></code> constants.
   * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.ResponseItem.Error
   *      opensocial.ResponseItem.Error
   */ 
  public static const Error:ConstType = new ConstType(
      "opensocial.ResponseItem.Error", {
          NOT_IMPLEMENTED     : 'notImplemented',
          UNAUTHORIZED        : 'unauthorized',
          FORBIDDEN           : 'forbidden',
          BAD_REQUEST         : 'badRequest',
          INTERNAL_ERROR      : 'internalError',
          LIMIT_EXCEEDED      : 'limitExceeded'
      });
      
   /**
    * A constant item for success request and response but no data return from server. 
    */ 
   public static const SIMPLE_SUCCESS:ResponseItem = new ResponseItem();

   /**
    * Holds the data object.
    * @private 
    */
   private var data_:* = null;

   /**
    * Holds the error code.
    * @private 
    */
   private var errorCode_:String = null;
   
   /**
    * Holds the error string.
    * @private 
    */
   private var errorMessage_:String = null;

   
   /**
    * Constructor to create a response item in client class.
    * @param data The data object fetched from container. Can be null if no returns.
    * @param errorCode A value of <code>ResponseItem.Error</code>. Null if no errors.
    * @param errorMessage A string to describe the error.
    */
   public function ResponseItem(data:* = null,
                                errorCode:String = null,
                                errorMessage:String = null) {
     data_ = data;
     errorCode_ = errorCode;
     errorMessage_ = errorMessage;
   }


  /**
   * Gets the error code from this response item.
   * @return The error code. 
   */
   public function getErrorCode():String {
     return errorCode_;
   }


  /**
   * Gets the error message from this response item.
   * @return The error message. 
   */
   public function getErrorMessage():String {
     return errorMessage_;
   }

   
  /**
   * Check it the response item had error.
   * @return Ture if it had error. 
   */
   public function hadError():Boolean {
     return errorMessage_ != null;
   }

   
  /**
   * Gets the data.
   * @return The data object. 
   */
   public function getData():* {
     return data_;
   }
}
}
