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
 * Wrapper of <code><j>opensocial.Enum</j></code> object in javascript.
 * 
 * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.Enum opensocial.Enum
 * 
 * @author yiziwu@google.com (Yizi Wu)
 */
public class Enum extends DataType {
  /**
   * <code><j>opensocial.Enum.Smoker</j></code> constants.
   * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.Enum.Smoker
   *      opensocial.Enum.Smoker
   */
  public static const Smoker:ConstType = new ConstType(
      "opensocial.Enum.Smoker", {
          NO              : 'NO',
          YES             : 'YES',
          SOCIALLY        : 'SOCIALLY',
          OCCASIONALLY    : 'OCCASIONALLY',
          REGULARLY       : 'REGULARLY',
          HEAVILY         : 'HEAVILY',
          QUITTING        : 'QUITTING',
          QUIT            : 'QUIT'
      });

  /**
   * <code><j>opensocial.Enum.Drinker</j></code> constants.
   * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.Enum.Drinker
   *      opensocial.Enum.Drinker
   */
  public static const Drinker:ConstType = new ConstType(
      "opensocial.Enum.Drinker", {
          NO            : 'NO',
          YES           : 'YES',
          SOCIALLY      : 'SOCIALLY',
          OCCASIONALLY  : 'OCCASIONALLY',
          REGULARLY     : 'REGULARLY',
          HEAVILY       : 'HEAVILY',
          QUITTING      : 'QUITTING',
          QUIT          : 'QUIT'
      });

  /**
   * <code><j>opensocial.Enum.LookingFor</j></code> constants.
   * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.Enum.LookingFor
   *      opensocial.Enum.LookingFor
   */
  public static const LookingFor:ConstType = new ConstType(
      "opensocial.Enum.LookingFor", {
          DATING              : 'DATING',
          FRIENDS             : 'FRIENDS',
          RELATIONSHIP        : 'RELATIONSHIP',
          NETWORKING          : 'NETWORKING',
          ACTIVITY_PARTNERS   : 'ACTIVITY_PARTNERS',
          RANDOM              : 'RANDOM'
      });

  /**
   * <code><j>opensocial.Enum.Presence</j></code> constants.
   * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.Enum.Presence
   *      opensocial.Enum.Presence
   */
  public static const Presence:ConstType = new ConstType(
      "opensocial.Enum.Presence", {
          AWAY        : 'AWAY',
          CHAT        : 'CHAT',
          DND         : 'DND',
          OFFLINE     : 'OFFLINE',
          ONLINE      : 'ONLINE',
          XA          : 'XA'
      });


  /**
   * <code><j>opensocial.Enum.Gender</j></code> constants.
   * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.Enum.Gender
   *      opensocial.Enum.Gender
   */
  public static const Gender:ConstType = new ConstType(
      "opensocial.Enum.Gender", {
          MALE      : 'MALE',
          FEMALE    : 'FEMALE'
      });

  /**
   * Constructor.
   * <p>
   * NOTE: This constructor is internally used. You should not call this constructor directly 
   * outside this package.
   * </p>
   * @param rawObj The wrapped object from Js-side passed by the <code>ExternalInterface</code>.
   */
  public function Enum(rawObj:Object) {
    super(rawObj);
  }

  /**
   * Gets the key string for the enum.
   * @return The key string.
   */
  public function getKey():String {
    return getRawProperty("key") as String;
  }

  /**
   * Gets the displa value string for the enum.
   * @return The display value string.
   */
  public function getDisplayValue():String {
    return getRawProperty("displayValue") as String;
  }

  /**
   * Returns the string for displaying this enum.
   * @return The string for display. 
   */
  override public function toString():String {
    return getDisplayValue();
  }

}
}