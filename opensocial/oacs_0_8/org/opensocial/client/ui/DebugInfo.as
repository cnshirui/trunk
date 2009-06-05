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

package org.opensocial.client.ui {

import flash.display.DisplayObjectContainer;
import flash.text.TextField;
import flash.text.TextFormat;

/**
 * A debugging output box for Adobe Flash IDE developers. 
 * Apps can create it and add to wherelse visible.
 * 
 * @author yiziwu@google.com (Yizi Wu)
 */
public class DebugInfo extends TextField {

  private static var instance:DebugInfo = null;

  /**
   * The constructor. Since we use factory create method to instantialize the 
   * box, it shouldn't be called outside. 
   * @param color The text color.
   */  
  public function DebugInfo(color:uint = 0xFFFFFF) {
    multiline = true;
    mouseWheelEnabled = true;
    selectable = true; 
    wordWrap = true;
    var txtfmt:TextFormat = new TextFormat();
    txtfmt.size = 12;
    txtfmt.font = "courier";
    txtfmt.color = color;
    this.defaultTextFormat = txtfmt;
  }

  /**
   * Creates a info box in the stage.
   * @param container The parent container of thix box.
   * @param x The x position, default = 0.
   * @param y The y position, default = 0.
   * @param width The width dimension, default = 400.
   * @param height The height dimension, default = 120.
   * @param color The text color, default = white.
   */  
  public static function create(container:DisplayObjectContainer, 
                                x:Number = 0, y:Number = 0,
                                width:Number = 400, height:Number = 120,
                                color:uint = 0xFFFFFF):void {
    if (instance != null) {
      throw new DefinitionError("Singleton class cannot creat instance twice.");
    }
    instance = new DebugInfo(color);
    instance.x = x;
    instance.y = y; 
    instance.width = width;
    instance.height = height;
    container.addChild(instance);
  }

  /**
   * Return the box is created or not. 
   * @return True if it's created.
   */  
  public static function isCreated():Boolean {
    return instance != null;
  }

  /**
   * Prints the text to the info box. 
   * @param text The text to output. 
   */  
  public static function print(text:String):void {
    if (!isCreated()) {
      // The app instance never creates it, nothing will be printed.
      return;
    }
    instance.appendText(text);
    instance.scrollV = instance.maxScrollV;
  }
}
}
