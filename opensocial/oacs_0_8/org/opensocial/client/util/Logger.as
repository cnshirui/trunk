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

package org.opensocial.client.util {

import flash.utils.getQualifiedClassName;

/**
 * A reference logger funtionality class.
 * Developers can extend this class to do more customize.
 * @author yiziwu@google.com (Yizi Wu)
 */
public class Logger {

  /**
   * A hook function to handle info output. It accepts one parameter of string.
   * If it's not set, by default do nothing.
   * @private
   */ 
  private var printFunc:Function = null;

  /**
   * Constructor for the logger.
   */ 
  public function Logger(printer:Function = null) {
    printFunc = printer;
  }

  /**
   * Output the content as text.
   */
  public function info(obj:Object):void {
    if (printFunc != null) {
      printFunc(inspect(obj != null ? obj.toString() : null));
    }
  }

  /**
   * Log down the detail of an object.
   */
  public function log(obj:Object):void {
    if (printFunc != null) {
      printFunc(inspect(obj));
    }
  }

  /**
   * Log as ERROR for the error object.
   */
  public function error(error:Error):void {
    if (printFunc != null) {
      printFunc(inspect(error));
    }
  }
  
  /**
   * For the app instance to set an error callback.
   * It will output error trace for debugging.
   * @param handler The error handler function.
   */
  public function setPrinter(printer:Function):void {
    printFunc = printer;
  }
  
  /**
   * An object inpetcor function. It's very useful to look at values inside a target. 
   * It will recursively go into each public fields in this object.
   * @param obj The object to be inspected.
   * @return The output string. 
   */  
  public static function inspect(obj:Object):String {
    var buffer:Array = [];
    buffer.push("[", new Date().toLocaleTimeString(), "]  ");
    var inspect:Function = function(obj:Object, buffer:Array, opt_prefix:String = ""):void {
      if (obj == null) {
        buffer.push("NULL\n");
      } else if (obj is String || 
                 obj is Boolean || 
                 obj is Number || 
                 obj is Date || 
                 obj is Function) {
        buffer.push(obj, "\n");
      } else if (obj is Error) {
        buffer.push("#", (obj as Error).errorID, ":", (obj as Error).message, "\n");
      } else {
        buffer.push("<", getQualifiedClassName(obj), ">\n");
        for(var key:String in obj) {
          buffer.push(opt_prefix, "\t", key, "\t  ");
          
          var value:* = obj[key];
          buffer.push("<", getQualifiedClassName(value), ">\t : ");
          
          inspect(obj[key], buffer, opt_prefix + "\t")
        }
      }
    };
    inspect(obj, buffer);
    return buffer.join("");
  }
}

}
