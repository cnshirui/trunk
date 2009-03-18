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
 * Wrapper of <code><j>opensocial.IdSpec</j></code> object in javascript.
 * 
 * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.IdSpec
 *      opensocial.IdSpec
 * 
 * @author yiziwu@google.com (Yizi Wu)
 */
public class IdSpec extends MutableDataType {
  /**
   * <code><j>opensocial.IdSpec.Field</j></code> constants.
   * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.IdSpec.Field
   *      opensocial.IdSpec.Field
   */ 
  public static const Field:ConstType = new ConstType(
      "opensocial.IdSpec.Field", {
          USER_ID           : 'userId',             /* IdSpec.PersonId */
          GROUP_ID          : 'groupId',            /* IdSpec.GroupId */
          NETWORK_DISTANCE  : 'networkDistance'     /* Number */
      });

  /**
   * <code><j>opensocial.IdSpec.PersonId</j></code> constants.
   * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.IdSpec.PersonId
   *      opensocial.IdSpec.PersonId
   */ 
  public static const PersonId:ConstType = new ConstType(
      "opensocial.IdSpec.PersonId", {
          OWNER     : 'OWNER',
          VIEWER    : 'VIEWER'
      });

  /**
   * <code><j>opensocial.IdSpec.GroupId</j></code> constants.
   * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.IdSpec.GroupId
   *      opensocial.IdSpec.GroupId
   */ 
  public static const GroupId:ConstType = new ConstType(
      "opensocial.IdSpec.GroupId", {
          SELF      : 'SELF',
          FRIENDS   : 'FRIENDS',
          ALL       : 'ALL'
      });


  /**
   * Creates an <code>IdSpec</code> object.
   * @param userId The value for <code>IdSpec.Field.USER_ID</code>, which is an 
   *               <code>IdSpec.PersonId</code> value.
   * @param groupId The value for <code>IdSpec.Field.GROUP_ID</code>.which is an 
   *               <code>IdSpec.GroupId</code> value.
   * @param networkDist The value for <code>IdSpec.Field.NETWORK_DISTANCE</code>. which is 
   *                    a number.
   * 
   * @return The new instance.
   */ 
  public static function newInstance(userId:String,
                                     groupId:String = null,
                                     networkDist:Number = Number.NaN):IdSpec {
    var idSpec:IdSpec = new IdSpec(MutableDataType.createRawObject());
    if (PersonId.valueOf(userId) != null) {
      idSpec.setField(Field.USER_ID, userId);
    }
    if (groupId != null && GroupId.valueOf(groupId) != null) {
      idSpec.setField(Field.GROUP_ID, groupId);
    }
    if (!isNaN(networkDist)) {
      idSpec.setField(Field.NETWORK_DISTANCE, networkDist);
    }
    return idSpec;
  }

  /**
   * Constructor.
   * <p>
   * NOTE: This constructor is internally used. You should not call this constructor directly 
   * outside this package.
   * </p>
   * @param rawObj The wrapped object from Js-side passed by the <code>ExternalInterface</code>.
   */
  public function IdSpec(rawObj:Object) {
    super(rawObj);
  }

}
}
