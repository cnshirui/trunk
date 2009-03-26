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

package {
  
import flash.display.Loader;
import flash.display.MovieClip;
import flash.display.Sprite;
import flash.events.Event;
import flash.events.MouseEvent;
import flash.filters.BitmapFilterQuality;
import flash.filters.DropShadowFilter;
import flash.net.URLRequest;
import flash.text.TextField;
import flash.text.TextFormat;

import org.opensocial.client.base.*;
import org.opensocial.client.jswrapper.*;
import org.opensocial.client.ui.*;
import org.opensocial.client.util.*;

/**
 * Sample Application using the opensocial flash API in Adobe Flash IDE.
 * <p>
 * Usage:<br>
 * 1. Extend your document class from SocialApp Base class. 
 * <br> 
 * 2. Implement the setup() and put some initial drawing without data access. 
 * <br>
 * 3. Implement the display() with the basic data handling logics.
 *    SocialApp will fetch the basic data set before display() is called.
 * <br>
 * 4. Interact with your users in the after display(). Use this.api to access
 *    the opensocial data. Create wild data connection to your own server 
 *    freely.   
 * </p>
 * 
 * @author yiziwu@google.com (Yizi Wu)
 */
public class SampleApp extends MovieClip {

  private var client:OpensocialClient;

  public function SampleApp() {
    
    
    prepareBtns();
    
    
    // Create the output box for information displaying
    DebugInfo.create(this, 2, 100, 500, 350);
    
    
    // Initialize Client and start
    client = new OpensocialClient();
    client.logger = new Logger(DebugInfo.print);
    
    client.logger.log(new Date());
    
    client.addEventListener(OpensocialEvent.READY, onReady);
    client.start();
  }

  // -------------------------------------------------------------
  //  Overriden functions in super class
  // -------------------------------------------------------------

  
  public function onReady(event:OpensocialEvent):void {
    
    client.logger.info(Environment.getDomain());
    client.logger.info(GadgetsViews.getCurrentView());

    //fetchMe();
    //fetchFriends();
    //sendMessage();
    //createActivity();
    //makeRequest();
    
  }
  
  private function fetchMe():void {
    
    var hasLocation:Boolean = Environment.supportsField(Environment.ObjectType.PERSON, Person.Field.CURRENT_LOCATION);
    
    var params:Object = DataRequest.newPeopleRequestParams(0, 1, [Person.Field.CURRENT_LOCATION]);

    client.fetchPerson(IdSpec.PersonId.VIEWER, function(r:ResponseItem):void {
      try {
        var p:Person = r.getData();
        client.logger.info(p.getDisplayName() + (hasLocation ? p.getFieldData(Person.Field.CURRENT_LOCATION, Address).toString() : ""));
        drawPerson(p, 0);
      } catch (e:Error) {
        client.logger.info(e);
      }
    }, params);
  }
  
  private function fetchFriends(start:int = 0):void {
    
    
    var idSpec:IdSpec = IdSpec.newInstance(IdSpec.PersonId.VIEWER, IdSpec.GroupId.FRIENDS);
    
    var hasLocation:Boolean = Environment.supportsField(Environment.ObjectType.PERSON, Person.Field.CURRENT_LOCATION);
    var params:Object = DataRequest.newPeopleRequestParams(start, 5);
    
    client.fetchPeople(idSpec, function(r:ResponseItem):void {
      try {
        var c:Collection = r.getData();
        client.logger.info(c.toDebugString());
        
        var arr:Array = c.getArray();
        for (var i:int = 0; i < arr.length; i++) {
          var p:Person = arr[i];
          client.logger.info(p.getDisplayName());
          drawPerson(p, i + 1);
        }
        if (c.getRemainingSize() > 0) {
          fetchFriends(c.getNextOffset());
        }
      } catch (e:Error) {
        client.logger.info(e);
      }
    }, params);
  }

  private function sendMessage():void {
    var m:Message = Message.newInstance("Hello World...", "I am sending message.");
    client.logger.log(m.toRawObject());
    client.requestSendMessage(['VIEWER'], m, function(r:ResponseItem):void {
      if (r.hadError()) {
        client.logger.info("msg send failed!.");
      } else {
        client.logger.info("msg sent.");
      }
    });
  }
  
  private function createActivity():void {
    var a:Activity = Activity.newInstance("My new activity!", "Hello?");
    client.logger.log(a.toRawObject());
    client.requestCreateActivity(a, null, function(r:ResponseItem):void {
      if (r.hadError()) {
        client.logger.info(r.getErrorCode());
        client.logger.info(r.getErrorMessage());
      } else {
        client.logger.info("activity created");
      }
      
    });
  }
  
  private function makeRequest():void {
    var postData:Object = {'data1':123};
    var params:Object = GadgetsIo.newPostRequestParams(postData, 
        GadgetsIo.ContentType.TEXT, GadgetsIo.AuthorizationType.SIGNED);
    client.makeRequest("http://222.35.143.62/vcdemo/oauth-callback.php", function(r:ResponseItem):void {
      var data:String = r.getData();
      client.logger.info(data);
    }, params);
  }

  // -------------------------------------------------------------
  //  Helper functions for displaying
  // -------------------------------------------------------------

  private function getFilter():DropShadowFilter {
    var filter:DropShadowFilter = new DropShadowFilter();
    filter.inner = false;
    filter.angle = 120;
    filter.strength = 0.8;
    filter.distance = 2;
    filter.quality = BitmapFilterQuality.MEDIUM;
    return filter;
  }

  private function prepareBtns():void {
	
		if (this['fetchMeBtn']) {
      var btn1:TextField = this['fetchMeBtn'] as TextField;
      btn1.addEventListener(MouseEvent.CLICK, function(event:MouseEvent):void {
        fetchMe();
      });
    }
    
    if (this['fetchFriendsBtn']) {
      var btn2:TextField = this['fetchFriendsBtn'] as TextField;
      btn2.addEventListener(MouseEvent.CLICK, function(event:MouseEvent):void {
        fetchFriends();
      });
    }
    
    if (this['sendMessageBtn']) {
      var btn3:TextField = this['sendMessageBtn'] as TextField;
      btn3.addEventListener(MouseEvent.CLICK, function(event:MouseEvent):void {
        sendMessage();
      });
    }
    
    if (this['createActivityBtn']) {
      var btn4:TextField = this['createActivityBtn'] as TextField;
      btn4.addEventListener(MouseEvent.CLICK, function(event:MouseEvent):void {
        createActivity();
      });
    }
		
		
		if (this['makeRequestBtn']) {
			var btn5:TextField = this['makeRequestBtn'] as TextField;
			btn5.addEventListener(MouseEvent.CLICK, function(event:MouseEvent):void {
			  makeRequest();
			});
		}

  }

  private function drawPerson(person:Person, index:int):void {
    var box:Sprite = new Sprite();
    box.x = 2 + (index % 3) * 180;
    box.y = (Math.floor(index / 3)) * 70 + 2;
    box.filters = [getFilter()];
    addChild(box);

    var name:TextField = new TextField();
    name.x = 2;
    name.y = 2;
    var txtfmt:TextFormat = new TextFormat();
    txtfmt.size = 12;
    txtfmt.font = "arial";
    txtfmt.color = 0xFFFFFF;
    name.defaultTextFormat = txtfmt;
    name.text = person.getDisplayName();
    box.addChild(name);
    
    var url:String = person.getFieldString(Person.Field.THUMBNAIL_URL);
    
    //client.logger.info(url);
    
    if (url != null) {
      var request:URLRequest = new URLRequest(url);
      var thumb:Loader = new Loader();
      box.addChild(thumb);
      thumb.x = 2;
      thumb.y = 20 + 2;
      thumb.load(request);
      
      thumb.contentLoaderInfo.addEventListener(Event.COMPLETE, 
          function(event:Event):void {
            thumb.width = 32;
            thumb.height = 32;
          }, false, 0, true);
    }
  }
  
}
}
