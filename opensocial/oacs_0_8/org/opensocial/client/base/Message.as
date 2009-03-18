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
 * Wrapper of <code><j>opensocial.Message</j></code> in javascript.
 *
 * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.Message
 *      opensocial.Message
 * 
 * @author yiziwu@google.com (Yizi Wu)
 */
public class Message extends MutableDataType {
  /**
   * <code><j>opensocial.Message.Field</j></code> constants.
   * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.Message.Field
   *      opensocial.Message.Field
   */ 
  public static const Field:ConstType = new ConstType(
      "opensocial.Message.Field", {
          TYPE      : 'type',       /* Message.Type */
          TITLE     : 'title',
          BODY      : 'body',
          TITLE_ID  : 'titleId',
          BODY_ID   : 'bodyId'
      });

  /**
   * <code><j>opensocial.Message.Type</j></code> constants.
   * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.Message.Type
   *      opensocial.Message.Type
   */ 
  public static const Type:ConstType = new ConstType(
      "opensocial.Message.Type", {
          EMAIL               : 'email',
          NOTIFICATION        : 'notification',
          PRIVATE_MESSAGE     : 'privateMessage',
          PUBLIC_MESSAGE      : 'publicMessage'
      });



  /**
   * Creates a <code>Message</code> object. The signature only accepts several common fields. For
   * other fields, use the <code>setField</code> method. 
   * @param body The body text.
   * @param title The title text.
   * @param type The message type, which is a <code>Message.Type</code> value.
   * 
   * @return The new instance.
   */ 
  public static function newInstance(body:String,
                                     title:String = null,
                                     type:String = null):Message {
    var message:Message = new Message(MutableDataType.createRawObject());
    message.setField(Field.BODY, body);
    if (title != null) {
      message.setField(Field.TITLE, title);
    }
    if (type != null && Type.valueOf(type) != null) {
      message.setField(Field.TYPE, type);
    }
    return message;
  }


  /**
   * Constructor.
   * <p>
   * NOTE: This constructor is internally used. You should not call this constructor directly 
   * outside this package.
   * </p>
   * @param rawObj The wrapped object from Js-side passed by the <code>ExternalInterface</code>.
   */
  public function Message(rawObj:Object) {
    super(rawObj);
  }

  /**
   * Returns the default message display string.
   * @return The message field: <code>TITLE</code>.
   */
  override public function toString():String {
    return getFieldString(Field.TITLE);
  }

}

}