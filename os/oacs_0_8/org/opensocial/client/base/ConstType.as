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

import flash.external.ExternalInterface;
import flash.utils.Proxy;
import flash.utils.flash_proxy;

/**
 * Base type for all constants and enum objects defined in <code><j>opensocial</j></code> namespace 
 * in JS-side. Each constant type can read the real javacript values runtime if available. 
 * Otherwise each type can use its default value set respectively. 
 * 
 * @author yiziwu@google.com (Yizi Wu)
 */
public dynamic class ConstType extends Proxy {
  
  private static var useDefault_:Boolean = false;
  
  /**
   * A global setting here to determine if the constant/enum types should use default set or 
   * dynamically load from javascript.
   * @param useDefault True to use the default hard coded values.
   */  
  public static function setUseDefault(useDefault:Boolean):void {
    useDefault_ = useDefault;
  }
  
  /**
  * Real value set.
   * @private 
   */  
  private var values_:Object = null;
  
  /**
   * Default value set 
   * @private 
   */  
  private var defaultValues_:Object = null;
  
  /**
   * The javascript name of this type.
   * @private 
   */  
  private var typeName_:String = null;

  /**
   * Initialize a constant/enum type with it's javascript name and default value set. 
   * @param typeName The javascript name of this type.
   * @param defaultValues The default value set of <code>Map.&lt;String, String&gt;</code>.
   */    
  public function ConstType(typeName:String, defaultValues:Object) {
    typeName_ = typeName;
    defaultValues_ = defaultValues;
  }

  /**
   * Get the value of a enum object for the name.
   * @param name The enum name.
   * @return The enum object, which is a property of this proxy.
   * @private 
   */    
  private function getValue(name:String):* {
    var value:Object;
    
    if (useDefault_ || !ExternalInterface.available) {
      value = defaultValues_[name];
    } else {
      if (!values_) {
        values_ = ExternalInterface.call("function() {return " + typeName_ + ";}");
        if (!values_) {
          values_ = defaultValues_;
        }
      }
      value = values_[name];
    }
    return value;
  }
  
  /**
   * Proxy method, checks if this enum type has the specific enum item. 
   * @param name The name of the enum item.
   * @return True if it exists.
   */  
  override flash_proxy function hasProperty(name:*):Boolean {
    if (getValue(name as String)) {
      return true;
    } else {
      return false;
    }
  }
  
  /**
   * Proxy method, returns the specific enum item. 
   * @param name The name of the enum item.
   * @return The enum item.
   */  
  override flash_proxy function getProperty(name:*):* {
    var value:Object = getValue(name);
    if (value) {
      return value;
    } else {
      throw new OpensocialError("Undefined constant in " + typeName_ + ":" + name);
    }
  }
  
  /**
   * Proxy method, checks if this enum type has the specific enum item and return if exists. 
   * @param name The name of the enum item.
   * @return Return null if it doesn't exist.
   */  
  public function valueOf(name:String):* {
    var value:Object = getValue(name.toUpperCase());
    if (value) {
      return value;
    } else {
      return null;
    }
  }
  
}
}