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
 * Wrapper of <code><j>opensocial.Name</j></code> object in javascript.
 * 
 * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.Name opensocial.Name
 * 
 * @author yiziwu@google.com (Yizi Wu)
 */
public class Name extends DataType {
  /**
   * <code><j>opensocial.Name.Field</j></code> constants.
   * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.Name.Field
   *      opensocial.Name.Field
   */ 
  public static const Field:ConstType = new ConstType(
      "opensocial.Name.Field", {
          FAMILY_NAME       : 'familyName',
          GIVEN_NAME        : 'givenName',
          ADDITIONAL_NAME   : 'additionalName',
          HONORIFIC_PREFIX  : 'honorificPrefix',
          HONORIFIC_SUFFIX  : 'honorificSuffix',
          UNSTRUCTURED      : 'unstructured'
      });

  /**
   * Constructor.
   * <p>
   * NOTE: This constructor is internally used. You should not call this constructor directly 
   * outside this package.
   * </p>
   * @param rawObj The wrapped object from Js-side passed by the <code>ExternalInterface</code>.
   */
  public function Name(rawObj:Object) {
    super(rawObj);
  }

  /**
   * Returns the default name display string.
   * @return The name field: <code>UNSTRUCTURED</code>.
   */
  override public function toString():String {
    return getFieldString(Field.UNSTRUCTURED);
  }
}
}
