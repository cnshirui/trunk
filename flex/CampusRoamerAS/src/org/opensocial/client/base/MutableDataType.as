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
 * The base type for those writable data types.
 * <p> 
 * Including these types: <code>Activity</code>, <code>IdSpec</code>, <code>MediaItem</code>, 
 * <code>Message</code>, <code>NavigationParameters</code>.
 * </p>
 * <p>
 * Inheriting from <code>DataType</code>, they both have the 'field' property which holds the data
 * and <code>getField</code> accessors. Additionally they have a <code>setField</code> method to 
 * set the value.
 * </p>
 * 
 * @author yiziwu@google.com (Yizi Wu)
 */
public class MutableDataType extends DataType {
  /**
   * Constructor.
   * <p>
   * NOTE: This constructor is internally used. You should not call this constructor directly 
   * outside this package.
   * </p>
   * @param rawObj The wrapped object from Js-side passed by the <code>ExternalInterface</code>.
   */
  public function MutableDataType(rawObj:Object) {
    super(rawObj);
  }
  
  /**
   * Sets the field value.
   * @param key The field name.
   * @param value The value to be set. Can be another <code>DataType</code> object. 
   */
  public function setField(key:String, value:Object):void {
    getFields()[key] = value;
  }

  /**
   * Returns the raw object of the <code>DataType</code> object. Because this mutable types will
   * send back to JS-side, send the underlying raw object instead of itself.
   * @return The fattened raw object.
   */ 
  public function toRawObject():Object {
    return getRawObj();
  }
  
  /**
   * A helper funcion to creates an empty instance.
   * @return An empty wrapped raw object.
   */ 
  public static function createRawObject():Object {
    return {'fields': {}};
  }
}

}