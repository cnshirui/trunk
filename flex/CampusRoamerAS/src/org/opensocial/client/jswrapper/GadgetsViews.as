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

import flash.external.ExternalInterface;

import org.opensocial.client.base.ConstType;

/**
 * Wrapper of <code><j>gadgets.views</j></code> namespace in javascript. It contains some useful
 * static functions for handling the views.
 * 
 * @see http://code.google.com/apis/opensocial/docs/0.8/reference/gadgets/#gadgets.views
 *      gadgets.views
 * 
 * @author yiziwu@google.com (Yizi Wu)
 */
final public class GadgetsViews {
  
  /**
   * <code><j>gadgets.views.ViewType</j></code> constants.
   * @see http://code.google.com/apis/opensocial/docs/0.8/reference/gadgets/#gadgets.views.ViewType
   *      gadgets.views.ViewType
   */ 
  public static const ViewType:ConstType = new ConstType(
      "gadgets.views.ViewType", {
         CANVAS   : "CANVAS",
         HOME     : "HOME",
         PREVIEW  : "PREVIEW",
         PROFILE  : "PROFILE"
      });
  
  /**
   * Calls the <code><j>gadgets.views.getCurrentView</j></code> to get the current view
   * name. Null if the 'views' feature is not enabled.
   * @return The view name, defined in <code>GadgetsViews.ViewType</code>.
   * @see http://code.google.com/apis/opensocial/docs/0.8/reference/gadgets/#gadgets.views.getCurrentView
   *      gadgets.views.getCurrentView
   * @see http://code.google.com/apis/opensocial/docs/0.8/reference/gadgets/#gadgets.views.View.getName
   *      gadgets.views.View.getName 
   */ 
  public static function getCurrentView():String {
    return ExternalInterface.call("gadgets.views.getCurrentView().getName");
  }

  /**
   * Calls the <code><j>gadgets.views.View.isOnlyVisibleGadget</j></code> to check if 
   * it is the only app on the page. Returns true if the 'views' feature is not enabled. 
   * @return True if it's the only one visible app.
   * @see http://code.google.com/apis/opensocial/docs/0.8/reference/gadgets/#gadgets.views.View.isOnlyVisibleGadget 
   *      gadgets.views.View.isOnlyVisibleGadget
   * 
   */
  public static function isOnlyVisible():Boolean {
    return ExternalInterface.call("gadgets.views.getCurrentView().isOnlyVisibleGadget");
  }

  /**
   * Calls the <code><j>gadgets.views.getParams</j></code> to get the params from current view.
   * @return The params object.
   * @see http://code.google.com/apis/opensocial/docs/0.8/reference/gadgets/#gadgets.views.getParams
   *      gadgets.views.getParams
   */ 
  public static function getParams():Object {
    return ExternalInterface.call("gadgets.views.getParams");
  }

}
}
