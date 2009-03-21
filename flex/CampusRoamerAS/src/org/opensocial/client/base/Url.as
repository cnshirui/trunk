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
 * Wrapper of <code><j>opensocial.Url</j></code> object in javascript.
 * 
 * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.Url opensocial.Url
 * 
 * @author yiziwu@google.com (Yizi Wu)
 */
public class Url extends DataType {
  /**
   * <code><j>opensocial.Url.Field</j></code> constants.
   * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.Url.Field
   *      opensocial.Url.Field
   */ 
  public static const Field:ConstType = new ConstType(
      "opensocial.Url.Field", {
          TYPE        : 'type',
          LINK_TEXT   : 'linkText',
          ADDRESS     : 'address'
      });

  /**
   * Constructor.
   * <p>
   * NOTE: This constructor is internally used. You should not call this constructor directly 
   * outside this package.
   * </p>
   * @param rawObj The wrapped object from Js-side passed by the <code>ExternalInterface</code>.
   */
  public function Url(rawObj:Object) {
    super(rawObj);
  }

  /**
   * Returns the default url display string.
   * @return The url fields: 
   *         <code>&lt;a href="ADDRESS" target=_blank&gt;LINK_TEXT&lt;/a&gt;</code>
   */
  override public function toString():String {
    var text:String = getFieldString(Field.LINK_TEXT);
    var address:String = getFieldString(Field.ADDRESS);
    var type:String = getFieldString(Field.TYPE);
    if (text != null) {
      return "<a href=\"" + address + "\" target=_blank>" + text + "</a>";
    } else if (type != null) {
      return "<a href=\"" + address + "\" target=_blank>" + type + "</a>";
    } else {
      return "<a href=\"" + address + "\" target=_blank>URL</a>";
    }
  }
}
}
