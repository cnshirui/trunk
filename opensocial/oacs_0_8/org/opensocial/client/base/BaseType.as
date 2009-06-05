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
 * The base type interface for most of opensocial related types in this package.
 * <p>
 * This is the root class for some reflection usage except the <code>ConstType</code> and 
 * <code>ArrayType</code> series.
 * </p>
 * <p>
 * Here is the graph to represent the relation between these opensocial data types:
 * </p>
 * 
 * <listing>
 * 
 *    As-side: org.opensocial.client.base               Js-side: opensocial namespace
 *   ====================================================================================
 *    
 *    BaseType 
 *      + Collection                                    opensocial.Collection
 *      + DataRequest                                   opensocial.DataRequest
 *      + ResponseItem                                  opensocial.ResponseItem
 * 
 *      + DataType(abstract)
 *          + Address                                   opensocial.Address
 *          + BodyType                                  opensocial.BodyType
 *          + Email                                     opensocial.Email
 *          + Enum                                      opensocial.Enum
 *          + Name                                      opensocial.Name
 *          + Orgnaization                              opensocial.Orgnaization
 *          + Person                                    opensocial.Person
 *          + Phone                                     opensocial.Phone
 *          + Url                                       opensocial.Url
 * 
 *          + MutableDataType(absctract)           
 *              + Activity                              opensocial.Activity
 *              + IdSpec                                opensocial.IdSpec
 *              + MediaItem                             opensocial.MediaItem
 *              + Message                               opensocial.Message
 *              + NavigationParameters                  opensocial.NavigationParameters
 * 
 *      + Globals
 * 
 *    + ArrayType
 * 
 *    + ConstType
 *        + [all constants and enum objects]
 * 
 * </listing>
 *
 * 
 * @author yiziwu@google.com (Yizi Wu)
 */
public class BaseType {
  

}

}