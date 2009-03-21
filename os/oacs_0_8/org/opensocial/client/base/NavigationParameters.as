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
 * Wrapper of <code><j>opensocial.NavigationParameters</j></code> object in javascript.
 * 
 * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.NavigationParameters
 *      opensocial.NavigationParameters
 * 
 * @author yiziwu@google.com (Yizi Wu)
 */
public class NavigationParameters extends MutableDataType {
  /**
   * <code><j>opensocial.NavigationParameters.Field</j></code> constants.
   * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.NavigationParameters.Field
   *      opensocial.NavigationParameters.Field
   */ 
  public static const Field:ConstType = new ConstType(
      "opensocial.NavigationParameters.Field", {
          VIEW          : 'view',           /* View */ 
          OWNER         : 'owner',
          PARAMETERS    : 'parameters'      /* Map.<String, String> */
      });
      
  /**
   * <code><j>opensocial.NavigationParameters.DestinationType</j></code> constants.
   * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.NavigationParameters.DestinationType
   *      opensocial.NavigationParameters.DestinationType
   */ 
  public static const DestinationType:ConstType = new ConstType(
      "opensocial.NavigationParameters.DestinationType", {
          VIEWER_DESTINATION      : "viewerDestination",
          RECIPIENT_DESTINATION   : "recipientDestination"
      });


  /**
   * Creates an <code>NavigationParameters</code> object.
   * @param view The view name.
   * @param owner The user id for the owner to navigate.
   * @param parameters A map of parameters to be passed to the new view.
   * 
   * @return The new instance.
   */ 
  public static function newInstance(view:String,
                                     owner:String = null,
                                     parameters:Object = null):NavigationParameters {
    var naviParams:NavigationParameters = 
        new NavigationParameters(MutableDataType.createRawObject());
    
    naviParams.setField(Field.VIEW, view);
    
    if (owner != null) {
      naviParams.setField(Field.OWNER, owner);
    }
    if (parameters != null) {
      naviParams.setField(Field.PARAMETERS, parameters);
    }
    return naviParams;
  }

  /**
   * Constructor.
   * <p>
   * NOTE: This constructor is internally used. You should not call this constructor directly 
   * outside this package.
   * </p>
   * @param rawObj The wrapped object from Js-side passed by the <code>ExternalInterface</code>.
   */
  public function NavigationParameters(rawObj:Object) {
    super(rawObj);
  }

}

}
