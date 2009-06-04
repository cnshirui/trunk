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

import flash.utils.getQualifiedClassName;

/**
 * Extends from <code>Array</code> to handle wrapped objects array conversion.
 * 
 * @author yiziwu@google.com (Yizi Wu)
 */
public dynamic class ArrayType extends Array {
  /**
   * Convert an array of raw object to an array primitives or DataType instances.
   * <p>
   * NOTE: This constructor is internally used. You should not call this constructor directly 
   * outside this package.
   * </p>
   * @param raw The object which is an array from Js-side.
   * @param type The type of each item in this array. Null for primitives, otherwise the type 
   *             should be subtype of DataType.
   */
  public function ArrayType(rawObj:Object, type:Class = null) {
    if (rawObj == null) {
      throw new OpensocialError("Null raw object in type '" + getQualifiedClassName(this) + "'.");
    }

    if (type != null && !DataType.checkType(type)) {
      throw new OpensocialError("Element type '" + getQualifiedClassName(type) + 
                                "' mismatched when creating an array.");
    }

    var rawArray:Array = rawObj as Array;
    for each (var item:Object in rawArray) {
      if (type != null) {
        this.push(new type(item));
      } else {
        this.push(item);
      }
    }
  }
  
  /**
   * Gets an array by the field key and join all items to a flat string. Use
   * comma as a delim by default.
   * @param delim The delim string.
   * @return A joined string.
   */  
  public function toFlatString(delim:String = ", "):String {
    return this.join(delim);
  }
}

}