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
 * Wrapper of <code><j>opensocial.Activity</j></code> object in javascript.
 * 
 * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.Activity opensocial.Activity
 * 
 * @author yiziwu@google.com (Yizi Wu)
 */
public class Activity extends MutableDataType {

  /**
   * <code><j>opensocial.Activity.Field</j></code> constants.
   * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.Activity.Field opensocial.Activity.Field
   */ 
  public static const Field:ConstType = new ConstType(
      "opensocial.Activity.Field", {
          TITLE_ID            : 'titleId',
          TITLE               : 'title',
          TEMPLATE_PARAMS     : 'templateParams',     /* Map.<String, Object> */
          URL                 : 'url',
          MEDIA_ITEMS         : 'mediaItems',         /* Array.<MediaItem> */
          BODY_ID             : 'bodyId',
          BODY                : 'body',
          EXTERNAL_ID         : 'externalId',
          STREAM_TITLE        : 'streamTitle',
          STREAM_URL          : 'streamUrl',
          STREAM_SOURCE_URL   : 'streamSourceUrl',
          STREAM_FAVICON_URL  : 'streamFaviconUrl',
          PRIORITY            : 'priority',           /* Number */
          ID                  : 'id',
          USER_ID             : 'userId',
          APP_ID              : 'appId',
          POSTED_TIME         : 'postedTime'
      });


  /**
   * Creates an <code>Activity</code> object. The signature only accepts several common fields. For
   * other fields, use the <code>setField</code> method.
   * @param title The title text.
   * @param body The body text.
   * @return The new instance.
   */ 
  public static function newInstance(title:String,
                                     body:String = null):Activity {
    var activity:Activity = new Activity(MutableDataType.createRawObject());
    activity.setField(Field.TITLE, title);
    if (body != null) {
      activity.setField(Field.BODY, body);
    }
    return activity;
  }

  /**
   * Constructor.
   * <p>
   * NOTE: This constructor is internally used. You should not call this constructor directly 
   * outside this package.
   * </p>
   * @param rawObj The wrapped object from Js-side passed by the <code>ExternalInterface</code>.
   */
  public function Activity(rawObj:Object) {
    super(rawObj);
  }

  /**
   * Gets the ID that can be permanently associated with this activity.
   * @return The id string.
   */
  public function getId():String {
    return getRawProperty("id") as String;
  }

  /**
   * Returns the default activity display string.
   * @return The activity field: <code>TITLE</code>
   */
  override public function toString():String {
    return getFieldString(Field.TITLE);
  }
  
}

} 