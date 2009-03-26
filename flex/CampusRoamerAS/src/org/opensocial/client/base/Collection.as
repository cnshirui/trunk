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
 * Wrapper of <code><j>opensocial.Collection</j></code> object in javascript.
 * 
 * <p>
 * The collection object is a subset of the full set in container.
 * It's useful for friends fetching from a person who has 1000+ friends.
 * </p>
 * 
 * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.Collection opensocial.Collection
 * 
 * @author yiziwu@google.com (Yizi Wu)
 */
public class Collection extends BaseType {
  /**
   * The wrapped object from Js-side passed by the <code>ExternalInterface</code>.
   * @private
   */
  protected var obj_:Object;
  
  /**
   * The item's type.
   * @private 
   */
  private var elementType_:Class;
  
  /**
   * The array container to hold the items.
   * @private 
   */
  private var array_:Array;
  
  /**
   * The offset of this collection within a larger result set.
   * @private 
   */
  private var offset_:Number;
  
  /**
   * The total size of the larger result set that this collection belongs to.
   * @private 
   */
  private var totalSize_:Number;
  
  /**
   * The size of this collection, which is equal to or less than the total 
   * size of the result. 
   * @private 
   */
  private var size_:Number;

  /**
   * Constructor.
   * <p>
   * NOTE: This constructor is internally used. You should not call this constructor directly 
   * outside this package.
   * </p>
   * @param rawObj The wrapped object from Js-side passed by the <code>ExternalInterface</code>.
   * @param elementType The type of the items in this collection.
   * @internal
   */
  public function Collection(rawObj:Object, elementType:Class) {
    this.elementType_ = elementType;
    this.array_ = new ArrayType(rawObj["array"], elementType);
    this.offset_ = rawObj["offset"];
    this.totalSize_ = rawObj["totalSize"];
    this.size_ = rawObj["size"];
    if (this.array_ == null || this.size_ != this.array_.length) {
      // error!
    }
  }

  /**
   * Gets the item's type.
   * @return The items type.
   */
  public function getElementType():Class {
    return elementType_;
  }

  /**
   * Gets the items in the colletion.
   * @return The items in the colletion. 
   */
  public function getArray():Array {
    return array_;
  }

  /**
   * Gets the offset of this collection within a larger result set. 
   * @return The offset. 
   */
  public function getOffset():Number {
    return offset_;
  }

  /**
   * Gets the total size of the larger result set that this collection 
   * belongs to.
   * @return The total size.
   */
  public function getTotalSize():Number {
    return totalSize_;
  }

  /**
   * Gets the size of this collection, which is equal to or less than the total 
   * size of the result. 
   * @return The size.
   */
  public function getSize():Number {
    return size_;
  }

  /**
   * Gets the remaining number of item of the larger result set that this 
   * collection belongs to, which is equal to (totalSize - size - offset). 
   * If the number is negative, will return zero. 
   * @return The remaining size.
   */
  public function getRemainingSize():Number {
    return Math.max(totalSize_ - size_ - offset_, 0); 
  }

  /**
   * Gets the next offset in the larger result set that this collection belongs to, which is 
   * equal to (offset + size). If the number is greater than totalSize, will return totalSize and 
   * no more next item.
   * @return The next offset.
   */
  public function getNextOffset():Number {
    return Math.min(offset_ + size_, totalSize_); 
  }

  /**
   * Appends another collection to this collection.
   * @param another Another <code>Collection</code> object.
   * @return False if the two collection are not from the same larger result 
   *         set or the they are not adjacent.
   */
  public function append(another:Collection):Boolean {
    if (this.elementType_ != another.elementType_) {
      // Collections with different element types cannot be merged. 
      return false;
    }
    if (another.offset_ != this.getNextOffset() ||
        another.totalSize_ != this.totalSize_) {
      // cannot be concated if the offsets and size don't match
      return false;
    }
    this.array_ = this.array_.concat(another.array_);
    this.size_ = this.size_ + another.size_;
    return true;
  }

  /**
   * Outputs a debug string for this collection. 
   * @return All parameters joined in one string.
   */
  public function toDebugString():String {
    return getTotalSize() + "|" + getSize() + "|" + getOffset() + "|" + 
           getNextOffset() + "|" + getRemainingSize();
      
  }
}
}
