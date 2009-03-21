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

import flash.utils.Dictionary;

/**
 * Manager for the callback queue.
 * The dictionary stores <Number, Function> pairs.
 * 
 * @author yiziwu@google.com (Yizi Wu)
 */
public dynamic class CallbackManager extends Dictionary {
  
  private var uuid_:uint = 0; 
  
  /**
   * Constructs the request manager to store callbacks.
   */
  public function CallbackManager() {
     
  }
  
  /**
   * Returns an unique Id for each request. Uses as a key to register each callback.
   * @private
   */
  private function getReqID():String {
    // Use timestamp millseconds and a natural number for uuid.
    // NOTE: actionscript is single-threaded. So it is for sure thread-safe.
    ++uuid_;
    return new String(new Date().getTime() + uuid_);
  }
  
  
  /**
   * Pushes a callback to this callback buffer map.
   * @param callback The callback function.
   * @return the key for this callback.
   */
  public function push(callback:Function = null):String {
    var reqID:String = getReqID();
    this[reqID] = callback;
    return reqID;
  }

  /**
   * Pops a callback from this buffer map and excutes it.
   * @param reqID The key to access the stored callback function.
   * @param dataObjs Multiple data objects pass to the callback.
   */
  public function pop(reqID:String, ...dataObjs:*):void {
    var callback:Function = this[reqID];
    if (callback is Function) {
      callback.apply(null, dataObjs);
    }
    delete this[reqID];
  }
  
  /**
   * Simply drops the failed callback from this buffer map.
   * @param reqID The key to access the stored callback function.
   */
  public function drop(reqID:String):void {
    delete this[reqID];
  }
  
}

}