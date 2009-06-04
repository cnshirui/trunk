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
 * Wrapper of <code><j>opensocial.Address</j></code> object in javascript.
 * 
 * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.Address opensocial.Address
 * 
 * @author yiziwu@google.com (Yizi Wu)
 */
public class Address extends DataType {

  /**
   * <code><j>opensocial.Address.Field</j></code> constants.
   * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.Address.Field opensocial.Address.Field
   */ 
  public static const Field:ConstType = new ConstType(
      "opensocial.Address.Field", {
          TYPE                  : 'type',
          UNSTRUCTURED_ADDRESS  : 'unstructuredAddress',
          PO_BOX                : 'poBox',
          STREET_ADDRESS        : 'streetAddress',
          EXTENDED_ADDRESS      : 'extendedAddress',
          REGION                : 'region',
          LOCALITY              : 'locality',
          POSTAL_CODE           : 'postalCode',
          COUNTRY               : 'country',
          LATITUDE              : 'latitude',             /* Number */
          LONGITUDE             : 'longitude'             /* Number */
      });

  /**
   * Constructor.
   * <p>
   * NOTE: This constructor is internally used. You should not call this constructor directly 
   * outside this package.
   * </p>
   * @param rawObj The wrapped object from Js-side passed by the <code>ExternalInterface</code>.
   */
  public function Address(rawObj:Object) {
    super(rawObj);
  }

  /**
   * Returns the default address display string.
   * 
   * TODO: add locale namespace support.
   * 
   * @return The address text with fields: <code>COUNTRY LOCALITY</code>.
   */
  override public function toString():String {
    var country:String = getFieldString(Field.COUNTRY);
    var locality:String = getFieldString(Field.LOCALITY);
    if (locality != null) {
      return country == null ? locality : country + "  " + locality;
    } else {
      return country;
    }
  }
  
}
}
