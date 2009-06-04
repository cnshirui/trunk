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

import flash.events.EventDispatcher;
import flash.events.TimerEvent;
import flash.external.ExternalInterface;
import flash.system.Security;
import flash.utils.Timer;

import org.opensocial.client.base.*;
import org.opensocial.client.util.*;

/**
 * Opensocial Actionscript Client SDK - Javascript Wrapper Client.
 * <p>
 * This JSWrapper SDK is used for developers who want to develop flash opensocial apps in 
 * ActionScript 3.0.
 * </p>
 * <p>
 * It's aimed to have no dependency on which container it runs on. It only depents on the 
 * Standard Opensocial Javascript API. It is developed on Shindig based container. So it works best
 * on Shindig. 
 * </p>
 * <p>
 * The main idea for this javascript wrapper client is to setup a interface passing wrapped and 
 * unwrapped opensocial data type between flash and javascript. 
 * </p> 
 * <p>
 * All opensocial data I/O and app control can be handled by this client. An app can simply include 
 * the <code>org.opensocial.client</code> package to the flash project and use this client to make
 * the app social.
 * </p>
 * <p>
 * A typical usage of this client is listing below:
 * </p>
 * @example
 * <listing version="3.0">
 *  
 *   funcion init():void {
 *     displaySomeStuff();
 * 
 *     var client:OpensocialClient = new OpensocialClient();
 *     client.addEventListener(OpensocialEvent.READY, onReady);
 *     client.start();
 *   }
 * 
 *   //...
 *   
 *   function onReady(event:OpensocialEvent):void {
 *     displayOtherStuff();
 * 
 *     // start your logic
 *     client.fetchPerson(...);
 *
 *   }
 * </listing>
 *  
 * @author yiziwu@google.com (Yizi Wu)
 */
public class OpensocialClient extends EventDispatcher {
  
  /**
   * The Opensocial Actionscript Client SDK namespace on JS-side.
   * @private
   */ 
  private var jsNamespace_:String = "opensocial.flash";

  /**
   * The callback manager to buffer all the ongoing request callback handlers.
   * @private
   */ 
  private var callbacks_:CallbackManager;

  /**
   * The logger instance.
   * @private
   */ 
  private var logger_:Logger;
  
  /**
   * Indicating if the API is already started to check the javascript.
   * @default false
   * @private
   */ 
  private var isStarted_:Boolean;


  /**
   * Indicating if the API is already initialized and ready for requests.
   * @default false
   * @private
   */ 
  private var isReady_:Boolean;
  
  // ---------------------------------------------------------------------------
  //     Initializing
  // ---------------------------------------------------------------------------
  /**
   * Opensocial Client constructor, initializing some empty collections and values.
   */
  public function OpensocialClient() {
    // TODO: The security need to be configured.
    Security.allowDomain("*");
    callbacks_ = new CallbackManager();
    isStarted_ = false;
    isReady_ = false;
  }

  /**
   * Gets the logger instance
   * @return The logger.
   */  
  public function get logger():Logger {
    if (logger_ == null) {
      logger_ = new Logger();
    }
    return logger_;
  }
  
  /**
   * Sets the new logger instance for this client.
   * @param newLogger a new logger. 
   */ 
  public function set logger(newLogger:Logger):void {
    logger_ = newLogger;
  }

  /**
   * Starts the main process.
   * 
   * <p>
   * The main process will first check the availability of <code>ExternalInterface</code> of the 
   * flash player and the javascript in browser.
   * Customized client can override this method.
   * </p>
   */
  public function start():void {
    if (isStarted_ || isReady_) return;
    isStarted_ = true;

    if (!ExternalInterface.available) {
      logger.error(new OpensocialError("ExternalInterface is not available."));
    } else {
      // Register external callbacks
      registerExternalCallbacks();
      
      // Check if the javascript is ready in DOM
      checkJavascriptReady();
    }
  }

  /**
   * Checks and waits for the javascript environment ready. 
   * <p>
   * Because the javascript may not be loaded when this flash is loaded so 
   * <code>ExternalInterface</code> may not work correctly. This method will check the 
   * <code><j>opensocial.flash.jsReady()</j></code> function define in Js-side to make sure that 
   * all opensocial/gadgets api javascript codes are loaded.
   * </p>
   * <p>
   * When javascript is ready, an event will be dispatched.
   * </p>
   * @private
   */
  private function checkJavascriptReady():void {
    var timer:Timer = new Timer(100, 10);
    timer.addEventListener(TimerEvent.TIMER, function(event:TimerEvent):void {
      logger.info("Checking JavaScript status...");
      var jsReady:Boolean = ExternalInterface.call(jsNamespace_ + ".jsReady", 
                                                   ExternalInterface.objectID);
      if (jsReady) {
        logger.info("JavaScript is ready.");
        Timer(event.target).stop();
        
        isReady_ = true;
        
        dispatchEvent(
            new OpensocialEvent(OpensocialEvent.READY, 
                                false, false, event.target.currentCount));
      }
    });
    
    timer.addEventListener(TimerEvent.TIMER_COMPLETE, function(event:TimerEvent):void {
      logger.error(new OpensocialError("Retried " + event.target.currentCount + 
                                       " time(s) and failed."));
    });

    timer.start();
  }
  
  /**
   * Registers the external interface callbacks. The names are used in the javascript.
   * This method can be overridden by customized client.
   */
  protected function registerExternalCallbacks():void {
    
    // gadgets.io.makeRequest callbacks
    ExternalInterface.addCallback("handleMakeRequest", handleMakeRequest);

    // opensocial.request* callbacks 
    ExternalInterface.addCallback("handleRequestCreateActivity", handleRequestCreateActivity);
    ExternalInterface.addCallback("handleRequestSendMessage", handleRequestSendMessage);
    ExternalInterface.addCallback("handleRequestShareApp", handleRequestShareApp);
    ExternalInterface.addCallback("handleRequestPermission", handleRequestPermission);

    // opensocial.newDataRequest callbacks    
    ExternalInterface.addCallback("handleFetchPerson", handleFetchPerson);
    ExternalInterface.addCallback("handleFetchPeople", handleFetchPeople);
    ExternalInterface.addCallback("handleFetchPersonAppData", handleFetchPersonAppData);
    ExternalInterface.addCallback("handleUpdatePersonAppData", handleUpdatePersonAppData);
    ExternalInterface.addCallback("handleRemovePersonAppData", handleRemovePersonAppData);
    ExternalInterface.addCallback("handleFetchActivities", handleFetchActivities);
    
    // handle errors from javascript
    ExternalInterface.addCallback("handleError", handleError);

    // allow javascript to call the flash client logger for debugging.
    ExternalInterface.addCallback("trace", logger.log);
  }


  // ---------------------------------------------------------------------------
  //  Javascript IFPC functons
  // ---------------------------------------------------------------------------
  /**
   * Sets the app's witdh. This will resize the swf object's width.
   * @param width The new width.
   * @return False if the feature is not supported.
   */ 
  public function setStageWidth(width:Number):Boolean {
    if (!isReady_) return false;
    return ExternalInterface.call(jsNamespace_ + ".setStageWidth", width);
  }
  
  /**
   * Sets the app's height. 
   * This will resize the swf object's height. It will also adjust the iframe's
   * height if the 'dynamicHeight' feature is required.
   * @param height The new height.
   * @return False if the feature is not supported.
   */
  public function setStageHeight(height:Number):Boolean {
    if (!isReady_) return false;
    return ExternalInterface.call(jsNamespace_ + ".setStageHeight", height);
  }
  

  // ---------------------------------------------------------------------------
  //  Data RPC functions
  // ---------------------------------------------------------------------------
  /**
   * Sends request to fetch a person.
   * @param id An <code>IdSpec.PersonId</code> value, can be <code>VIEWER</code> or 
   *           <code>OWNER</code>.
   * @param callback A fucntion with a parameter of <code>ResponseItem</code>.
   *                 The underlying data in the response item is <code>Person</code>.
   * @param params A <code>Map.&lt;DataRequest.PeopleRequestField, Object&gt;</code> object.
   * @return True if the request is successfully sent.
   * 
   * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.DataRequest.newFetchPersonRequest 
   *      opensocial.DataRequest.newFetchPersonRequest
   */
  public function fetchPerson(
      id:String,
      callback:Function = null,
      params:/* Map.<DataRequest.PeopleRequestField, Object> */Object = null):Boolean {
    if (!isReady_) return false;
    var reqID:String = callbacks_.push(callback);
    return ExternalInterface.call(jsNamespace_ + ".fetchPerson",
                                  reqID, id, params);
  }

  /**
   * Callback of fetch person response.
   * @param reqID Request UID.
   * @param rawPerson A wrapped <code><j>opensocial.Person</j></code> object from Js-side.
   */
  protected function handleFetchPerson(reqID:String, rawPerson:Object):void {
    var person:Person = new Person(rawPerson);
    callbacks_.pop(reqID, new ResponseItem(person));
  }

  /**
   * Sends request to fetch friends.
   * @param idSpec An <code>IdSpec</code> object.
   * @param callback A fucntion with a parameter of <code>ResponseItem</code>.
   *                 The underlying data in the response item is <code>Collection</code>.
   * @param params A <code>Map.&lt;DataRequest.PeopleRequestField, Object&gt;</code> object.
   * @return True if the request is successfully sent.
   * 
   * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.DataRequest.newFetchPeopleRequest 
   *      opensocial.DataRequest.newFetchPeopleRequest
   */
  public function fetchPeople(
      idSpec:IdSpec,
      callback:Function = null,
      params:/* Map.<DataRequest.PeopleRequestField, Object> */Object = null):Boolean {
    if (!isReady_) return false;
    var reqID:String = callbacks_.push(callback);
    return ExternalInterface.call(jsNamespace_ + ".fetchPeople",
                                  reqID, idSpec.toRawObject(), params);
  }

  /**
   * Callback of fetch people response.
   * @param reqID request UID.
   * @param rawPeople A wrapped <code><j>opensocial.Collection.&lt;opensocial.Person&gt;</j></code>
   *                  object from Js-side.
   */
  protected function handleFetchPeople(reqID:String, rawPeople:Object):void {
    var people:Collection = new Collection(rawPeople, Person);
    callbacks_.pop(reqID, new ResponseItem(people));
  }

  /**
   * Sends request to fetch person app data.
   * @param idSpec An <code>IdSpec</code> object.
   * @param keys Array of key names, '*' to represent all.
   * @param callback A fucntion with a parameter of <code>ResponseItem</code>.
   *                 The underlying data in the response item is 
   *                 <code>Map.&lt;String, Map.&lt;String, Object&gt;&gt;</code>.
   * @param params A <code>Map.&lt;DataRequest.DataRequestField, Object&gt;</code> object.
   * @return True if the request is successfully sent.
   * 
   * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.DataRequest.newFetchPersonAppDataRequest 
   *      opensocial.DataRequest.newFetchPersonAppDataRequest
   */
  public function fetchPersonAppData(
      idSpec:IdSpec,
      keys:/* String */Array,
      callback:Function = null,
      params:/* Map.<DataRequest.DataRequestField, Object> */Object = null):Boolean {
    if (!isReady_) return false;
    var reqID:String = callbacks_.push(callback);
    return ExternalInterface.call(jsNamespace_ + ".fetchPersonAppData", 
                                  reqID, idSpec.toRawObject(), keys, params);
  }

  /**
   * Callback of fetch person app data response.
   * @param reqID Request UID.
   * @param rawDataSet <code>Map.&lt;String, Map.&lt;String, Object&gt;&gt;</code> object 
   *                   from Js-side.
   */
  protected function handleFetchPersonAppData(reqID:String, rawDataSet:Object):void {
    callbacks_.pop(reqID, new ResponseItem(rawDataSet));
  }

  /**
   * Sends request to update the person app data.
   * @param id A <code>IdSpec.PersonId</code> value, can only be <code>VIEWER</code>.
   * @param key One key name of the data.
   * @param value The value to be store, must be a json format.
   * @param callback A fucntion with a parameter of <code>ResponseItem</code>.
   *                 The underlying data in the response item is null.
   * @return True if the request is successfully sent.
   * 
   * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.DataRequest.newUpdatePersonAppDataRequest 
   *      opensocial.DataRequest.newUpdatePersonAppDataRequest
   */
  public function updatePersonAppData(
      id:String,
      key:String,
      value:Object,
      callback:Function = null):Boolean {
    if (!isReady_) return false;
    var reqID:String = callbacks_.push(callback);
    return ExternalInterface.call(jsNamespace_ + ".updatePersonAppData",
                                  reqID, id, key, value);
  }

  /**
   * Callback of update person app data response.
   * @param reqID Request UID.
   */
  protected function handleUpdatePersonAppData(reqID:String):void {
    callbacks_.pop(reqID, ResponseItem.SIMPLE_SUCCESS);
  }

  /**
   * Sends request to remove the person app data.
   * @param id A <code>IdSpec.PersonId</code> value, can only be <code>VIEWER</code>.
   * @param keys Array of key names, '*' to represent all.
   * @param callback A fucntion with a parameter of <code>ResponseItem</code>.
   *                 The underlying data in the response item is null.
   * @return True if the request is successfully sent.
   * 
   * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.DataRequest.newRemovePersonAppDataRequest 
   *      opensocial.DataRequest.newRemovePersonAppDataRequest
   */
  public function removePersonAppData(
      id:String,
      keys:/* String */Array,
      callback:Function = null):Boolean {
    if (!isReady_) return false;
    var reqID:String = callbacks_.push(callback);
    return ExternalInterface.call(jsNamespace_ + ".removePersonAppData",
                                  reqID, id, keys);
  }

  /**
   * Callback of update person app data response.
   * @param reqID Request UID.
   */
  protected function handleRemovePersonAppData(reqID:String):void {
    callbacks_.pop(reqID, ResponseItem.SIMPLE_SUCCESS);
  }

  /**
   * Sends request to fetch activities for people. 
   * Js-side.
   * @param idSpec An <code>IdSpec</code> object.
   * @param callback A fucntion with a parameter of <code>ResponseItem</code>.
   *                 The underlying data in the response item is <code>Collection</code>.
   * @param params A <code>Map.&lt;DataRequest.ActivityRequestFields, Object&gt;</code> object.
   * @return True if the request is successfully sent.
   * 
   * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.DataRequest.newFetchActivitiesRequest 
   *      opensocial.DataRequest.newFetchActivitiesRequest
   */
  public function fetchActivities(
      idSpec:IdSpec,
      callback:Function = null,
      params:/* Map.<DataRequest.ActivityRequestField, Object> */Object = null):Boolean {
    if (!isReady_) return false;
    var reqID:String = callbacks_.push(callback);
    return ExternalInterface.call(jsNamespace_ + ".fetchActivities",
                                  reqID, idSpec.toRawObject, params);
  }

  /**
   * Callback of fetch person activities.
   * @param reqID Request UID.
   * @param rawActivities A wrapped 
   *               <code><j>opensocial.Collection.&lt;opensocial.Activity&gt;</j></code> 
   *               object from Js-side.
   */
  protected function handleFetchActivities(reqID:String, rawActivities:Object):void {
    var activities:Collection = new Collection(rawActivities, Activity);
    callbacks_.pop(reqID, new ResponseItem(activities));
  }
  
  
  /**
   * Sends request to create an activity.
   * @param activity An <code>Activity</code> object.
   * @param priority A value of <code>Globals.CreateActivityPriority</code>.
   * @param callback A fucntion with a parameter of <code>ResponseItem</code>.
   *                 The underlying data in the response item is null.
   * @return True if the request is successfully sent.
   * 
   * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.requestCreateActivity
   *      opensocial.requestCreateActivity
   */
  public function requestCreateActivity(
      activity:Activity,
      priority:String,
      callback:Function = null):Boolean {
    if (!isReady_) return false;
    var reqID:String = callbacks_.push(callback);
    return ExternalInterface.call(jsNamespace_ + ".requestCreateActivity",
                                  reqID, activity.toRawObject(), priority);
  }

  /**
   * Callback of create activities request.
   * @param reqID Request UID.
   */
  protected function handleRequestCreateActivity(reqID:String):void {
    callbacks_.pop(reqID, ResponseItem.SIMPLE_SUCCESS);
  }

  /**
   * Sends request to send a message.
   * @param recipients An array of ids, such as OWNER, VIEWER, or person ids in reachable groups.
   * @param message An <code>Message</code> object.
   * @param callback A fucntion with a parameter of <code>ResponseItem</code>.
   *                 The underlying data in the response item is null.
   * @return True if the request is successfully sent.
   * 
   * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.requestSendMessage 
   *      opensocial.requestSendMessage
   */
  public function requestSendMessage(
      recipients:/* String */Array,
      message:Message,
      callback:Function = null):Boolean {
    if (!isReady_) return false;
    var reqID:String = callbacks_.push(callback);
    return ExternalInterface.call(jsNamespace_ + ".requestSendMessage",
                                  reqID, recipients, message.toRawObject());
  }

  /**
   * Callback of send message request.
   * @param reqID Request UID.
   */
  protected function handleRequestSendMessage(reqID:String):void {
    callbacks_.pop(reqID, ResponseItem.SIMPLE_SUCCESS);
  }


  /**
   * Sends request to share this app.
   * @param recipients An array of ids, such as OWNER, VIEWER, or person ids in reachable groups.
   * @param reason An <code>Message</code> object.
   * @param callback A fucntion with a parameter of <code>ResponseItem</code>.
   *                 The underlying data in the response item is null.
   * @return True if the request is successfully sent.
   * 
   * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.requestShareApp
   *      opensocial.requestShareApp
   */
  public function requestShareApp(
      recipients:/* String */Array,
      reason:Message,
      callback:Function = null):Boolean {
    if (!isReady_) return false;
    var reqID:String = callbacks_.push(callback);
    return ExternalInterface.call(jsNamespace_ + ".requestShareApp",
                                  reqID, recipients, reason.toRawObject());
  }

  /**
   * Callback of share app request.
   * @param reqID Request UID.
   */
  protected function handleRequestShareApp(reqID:String):void {
    callbacks_.pop(reqID, ResponseItem.SIMPLE_SUCCESS);
  }


  /**
   * Sends request to share this app.
   * @param permissions An array of <code>Globals.Permission</code> values.
   * @param reasonText A string of reason text.
   * @param callback A fucntion with a parameter of <code>ResponseItem</code>.
   *                 The underlying data in the response item is null.
   * @return True if the request is successfully sent.
   * 
   * @see http://code.google.com/apis/opensocial/docs/0.8/reference/#opensocial.requestPermission
   *      opensocial.requestPermission
   */
  public function requestPermission(
      permissions:/* String */Array,
      reasonText:String,
      callback:Function = null):Boolean {
    if (!isReady_) return false;
    var reqID:String = callbacks_.push(callback);
    return ExternalInterface.call(jsNamespace_ + ".requestPermission",
                                  reqID, permissions, reasonText);
  }

  /**
   * Callback of permission request.
   * @param reqID Request UID.
   */
  protected function handleRequestPermission(reqID:String):void {
    callbacks_.pop(reqID, ResponseItem.SIMPLE_SUCCESS);
  }
  
  
  
  /**
   * Sends request to a remote site to get or post data.
   * @param url The remote site url.
   * @param callback A fucntion with a parameter of <code>ResponseItem</code>.
   *                 The underlying data in the response item can be 
   *                 <code>String | Object</code> for different content types respectfully.
   * @param opt_params A <code>Map.&lt;GadgetsIo.RequestParameters, Object&gt;</code> object.
   * @return True if the request is successfully sent.
   * 
   * @see http://code.google.com/apis/opensocial/docs/0.8/reference/gadgets/#gadgets.io.makeRequest
   *      gadgets.io.makeRequest
   * @see http://code.google.com/apis/opensocial/articles/makerequest-0.8.html 
   *      Introduction to makeRequest
   */
  public function makeRequest(
      url:String,
      callback:Function = null,
      opt_params:Object = null):Boolean {
    if (!isReady_) return false;
    var reqID:String = callbacks_.push(callback);
    return ExternalInterface.call(jsNamespace_ + ".makeRequest",
                                  reqID, url, opt_params);
  }
  
  /**
   * Callback of make request.
   * @param reqID Request UID.
   * @param data Response data object from the remote site. The object format is determined by the
   *             content type parameter from the request.
   */
  protected function handleMakeRequest(reqID:String, data:Object):void {
    callbacks_.pop(reqID, new ResponseItem(data));
  }
  

  /**
   * Handles the javascript error and pop the callback.
   * @param reqID Request UID.
   * @param error The error object from javascript.
   */
  protected function handleError(reqID:String, error:Object):void {
    if (error != null) {
      var code:String = "";
      if (error["name"] == "OpensocialError") {
        code = error["code"];
      }
      callbacks_.pop(reqID, new ResponseItem(null, code, error["message"]));
    } else {
      callbacks_.drop(reqID);
      throw new OpensocialError("Unexpected error callback from javascript.");  
    }
  }
}

}
