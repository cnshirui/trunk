
/**
 * @fileoverview Provides the core JavaScript functionality for the Geochat
 *   application.
 */
 
// TODO Scroll the chat log when new messages are received.

(function($) {

  var map = null;
  var geocoder = null;
  var user = null;
  var lastUpdate = 0;

  window.people = {}

  /**
   * Represents each person active within Geochat.
   * @param {string} name The name of the person in question.
   * @param {string} email The person's email address.
   * @param {number} latitude The person's starting latitude.
   * @param {number} longitude The person's starting longitude.
   * @constructor
   */
  var Person =  function(name, email, lat, lng) {

    var me = this;
    window.people[email] = this;

    this.name = name;
    this.email = email;
    this.point = new GLatLng(lat, lng);
    this.marker = new GMarker(this.point, {draggable: true});
    this.marker.disableDragging();
    this.bubble = new ChatBubble(this);
    this.nametag = new NameTag(this);

    map.addOverlay(this.marker);
    map.addOverlay(this.bubble);
    map.addOverlay(this.nametag);

    // Handle drag events for this Person's marker.
    GEvent.addListener(this.marker, 'drag', function() {
      me.bubble.redraw();
      me.nametag.redraw();
      updateAddress();
    })
    
    // Handle drop events for this Person's marker. Note that this fires off
    // an Ajax call updating the user's location.
    GEvent.addListener(this.marker, 'dragend', function() {
      me.bubble.redraw();
      me.nametag.redraw();
      updateAddress();
      updateUserPosition();
    });
    
  };
  
  /**
   * Enable this Person as an actual user, assigning them to the
   * global "user" variable and allowing interaction.
   */
  Person.prototype.enableUser = function() {
    if (user) {
      user.disableUser();
    }
    user = this;
    this.marker.setImage(GEOCHAT_IMAGES['marker-user']);
    this.marker.enableDragging();
    updateAddress();
    map.setCenter(this.point, 13);
  };
  
  /**
   * Move this Person to the specified latitude and longitude.
   * @param {number} lat The latitude to move to.
   * @param {number} lng The longitude to move to.
   */  
  Person.prototype.move = function(lat, lng) {
    if (this.point.lat() != lat || this.point.lng() != lng) {
      this.point = new GLatLng(lat, lng);
      this.marker.setLatLng(this.point);
      this.bubble.redraw();
      this.bubble.fade();
      this.nametag.redraw();
      if (this == user) {
        updateAddress();
      }
    }
  };
  
  /**
   * Make this Person say a specified message.
   * @param {string} message The message to set within the user's chat bubble.
   * @param {boolean} raw Whether or not to sanitize the message.
   */
  Person.prototype.say = function(message, raw) {
    if (raw) {
      this.bubble.setMessage(message);
    } else {
      this.bubble.setMessage(sanitizeInput(message));
    }
  };
  
  /**
   * Represents the chat bubble associated with a given user, typically
   * displayed above their map marker.
   * @param {Person} The Person object to associate this chat bubble with.
   * @constructor
   * @extends GOverlay
   */
  function ChatBubble(person) {
    this.person = person;
  };
  ChatBubble.prototype = new GOverlay();
  
  ChatBubble.prototype.initialize = function(map) {
    this.bubbleDiv = $('<div />').addClass('bubble');
    this.bubbleDiv.append($('<div />').addClass('bubble-top'));
    this.messageDiv = $('<div />').addClass('bubble-middle');
    this.bubbleDiv.append(this.messageDiv);
    this.bubbleDiv.append($('<div />').addClass('bubble-bottom'));
    $(map.getPane(G_MAP_FLOAT_PANE)).append(this.bubbleDiv);
    this.xoffset = -10;
    this.yoffset = -30;
    var me = this;
    this.bubbleDiv.mouseover(function() {
      me.fade();
    });
  };
  
  /**
   * Redraws the ChatBubble, typically used during map interaction.
   * @param {boolean} force Whether to force a redraw.
   */
  ChatBubble.prototype.redraw = function(force) {
    var point = map.fromLatLngToDivPixel(this.person.marker.getPoint());
    this.bubbleDiv.css('left', point.x + this.xoffset);
    this.bubbleDiv.css('bottom', -1 * point.y - this.yoffset);
    this.bubbleDiv.css('z-index', 150 + point.y);
  };
  
  /**
   * Updates the ChatBubble's visible chat bubble to include the specified message.
   * @param {string} message The message to insert into the chat bubble.
   */
  ChatBubble.prototype.setMessage = function(message) {
    this.messageDiv.empty().append(message)
    this.show();
    this.fade();
  };
  
  /**
   * Show the chat bubble.
   */
  ChatBubble.prototype.show = function() {
    this.bubbleDiv.show();
  };
  
  /**
   * Hide the chat bubble.
   */
  ChatBubble.prototype.hide = function() {
    this.bubbleDiv.hide();
  };
  
  /**
   * Fade the chat bubble. Calling this method forces the bubble back to
   * full opacity, after which it fades out over time.
   */
  ChatBubble.prototype.fade = function() {
    var bubbleDiv = this.bubbleDiv;
    bubbleDiv.stop().fadeTo(100, 1, function() {
      bubbleDiv.fadeTo(100000, 0.50);
    });
  };
  
  /**
   * The name tag associated with a given person, typically displayed
   * underneath their map marker.
   * @param {Person} The Person object to associate this chat bubble with.
   * @constructor
   * @extends GOverlay
   */
  function NameTag(person) {
    this.person = person;
  };
  NameTag.prototype = new GOverlay();
  
  /**
   * Initializes the NameTag, injecting its DOM elements on demand.
   * @param {GMap2} The map to initialize the NameTag on.
   */
  NameTag.prototype.initialize = function(map) {
    this.nameTagDiv = $('<div />').addClass('nametag');
    this.nameSpan = $('<span>' + this.person.name + '</span>');
    this.nameTagDiv.append(this.nameSpan);
    $(map.getPane(G_MAP_FLOAT_PANE)).append(this.nameTagDiv);
    var me = this;
  };
  
  /**
   * Redraws the ChatBubble, typically used during map interaction.
   * @param {boolean} force Whether to force a redraw.
   */  
  NameTag.prototype.redraw = function(force) {
    var point = map.fromLatLngToDivPixel(this.person.marker.getPoint());
    this.nameTagDiv.css('left', point.x);
    this.nameTagDiv.css('top', point.y);
    this.nameTagDiv.css('z-index', 150 + point.y);
  };
  
  /**
   * Show this NameTag.
   */
  NameTag.prototype.show = function() {
    this.nameTagDiv.show();
  };
  
  /**
   * Hide this NameTag.
   */
  NameTag.prototype.hide = function() {
    this.nameTagDiv.hide();
  };
   
  /**
   * Performs basic scrubbing of input to prevent HTML injection.
   * @param {string} input The input text to sanitize.
   * @return {string} The sanitized input.
   */
  var sanitizeInput = function(input) {
    return input.replace('&', '&amp;').replace('<', '&lt;').
        replace('>', '&gt;');
  };
  
  /**
   * Updates the address form to indicate the user's current location.
   */
  var updateAddress = function() {
    var point = user.marker.getLatLng();
    var lat = Math.round(point.lat() * 10000) / 10000;
    var lng = Math.round(point.lng() * 10000) / 10000;
    $('#address').attr('value', lat + ', ' + lng);
  };
  
  /**
   * Makes an Ajax call to update the user's position in the Geochat DB and
   * cache.
   */
  var updateUserPosition = function() {
    $.post('/event/move', {
      'latitude': user.marker.getLatLng().lat(),
      'longitude': user.marker.getLatLng().lng(),
      'zoom': map.getZoom()
    });
  }
  
  /**
   * Causes a chat event for the currently active user, including an Ajax
   * call against the Geochat datastore.
   * @param {DOM} chatInput The input field to pull chat contents from.
   */
  window.say = function(chatInput) {
    var chat = chatInput.value;
    if (chat) {
      //map.setCenter(user.marker.getPoint(), 13);
      user.say(chat);
      chatInput.value = '';
      $.post('/event/chat', {
        'contents': chat,
        'latitude': user.marker.getLatLng().lat(),
        'longitude': user.marker.getLatLng().lng(),
        'zoom': map.getZoom()
      });
    }
  };
  
  /**
   * Move the user and map to the specified address, if found.
   */
  window.move = function(address) {
    geocoder.getLatLng(address, function(latlng) {
      user.move(latlng.lat(), latlng.lng());
      map.setCenter(user.marker.getLatLng(), 13);
      updateUserPosition();
    });
  };

  /**
   * Callback for updates containing chat events.
   * @param {Object} data A JSON object containing event data.
   */
  window.chatCallback = function(data) {
    var events = data.chats;
    var logEntriesDiv = $('#log-entries');
    for (var i = 0; i < events.length; ++i) {
            
      // Update the chat log.
      var logEntryDiv = $('<div class="log-entry" />');
      var logEntryPointA = $('<a href="javascript:move(\'' +
          events[i].latitude + ',' + events[i].longitude +
          '\')" />');
      if (user.name == events[i].user.nickname) {
        logEntryPointA.append('<img src="' + GEOCHAT_IMAGES['marker-user'] +
            '" />');
      } else {
        logEntryPointA.append('<img src="' + GEOCHAT_IMAGES['marker'] +
            '" />');
      }
      logEntryDiv.append(logEntryPointA);
      logEntryDiv.append('<strong>' + events[i].user.nickname + '</strong>');
      logEntryDiv.append('<div>' + events[i].contents + '</div>');
      logEntriesDiv.append(logEntryDiv);
     
      var speaker = null;
      
      // Verify whether the speaker exists. If not, create them.
      if (!window.people[events[i].user.email]) {
        speaker = new Person(
          events[i].user.nickname,
          events[i].user.email,
          events[i].latitude,
          events[i].longitude);
      } else {
        speaker = window.people[events[i].user.email];
      }
      
      // Update the speaker's chat bubble.
      speaker.move(events[i].latitude, events[i].longitude);
      speaker.say(events[i].contents);
      
    }    
  };
  
  /**
   * Callback for updates containing move events.
   * @param {Object} data A JSON object containing event data.
   */
  window.moveCallback = function(data) {
    var moves = data.moves;
    for (var i = 0; i < moves.length; ++i) {
      var move = moves[i];
      if (!window.people[move.user.email]) {
        new Person(
           move.user.nickname,
           move.user.email,
           move.latitude,
           move.longitude);
      } else {
        var mover = window.people[move.user.email];
        if (mover != user) {
          mover.move(move.latitude, move.longitude);        
        }
      }
    }
  }

  /**
   * A callback for when an update request succeeds.
   * @param {string} json JSON data to be evaluated and passed on to event
   *   callbacks.
   */
  window.updateSuccess = function(json) {
    var data = eval('(' + json + ')');
    lastUpdate = data.timestamp;
    chatCallback(data);
    moveCallback(data);
    window.setTimeout(update, GEOCHAT_VARS['update_interval'])
  }
  
  /**
   * A callback for when updates fail. Presents an error to the user and
   * forces a lengthier delay between updates.
   */
  window.updateError = function() {
    alert('An update error occured! Trying again in a bit.');
    window.setTimeout(update, GEOCHAT_VARS['error_interval'])
  }

  /**
   * The main update handler, used to query the Geochat DB and cache for
   * updated events. Successful queries pass the results on to the
   * chatCallback and moveCallback functions.
   */
  window.update = function() {
    var bounds = map.getBounds();
    var min = bounds.getSouthWest();
    var max = bounds.getNorthEast();
    $.ajax({
      type: 'GET',
      url: '/event/update',
      data: [
        'min_latitude=', min.lat(),
        '&min_longitude=', min.lng(),
        '&max_latitude=', max.lat(),
        '&max_longitude=', max.lng(),
        '&zoom=', map.getZoom(),
        '&since=', lastUpdate
      ].join(''),
      success: updateSuccess,
      error: updateError
    });
  }
  
  
  window.onload = function() {
    if (GBrowserIsCompatible()) {
      
      // Initialize the map
      var mapDiv = document.getElementById('map');
      map = new GMap2(mapDiv);
      map.addControl(new GMapTypeControl());
      map.disableDoubleClickZoom();
      GEvent.addListener(map, 'dblclick', function(overlay, point) {
        user.move(point.lat(), point.lng());
        updateUserPosition();
      });
      
      // Initialize the geocoder.
      geocoder = new GClientGeocoder();
      
      if (GEOCHAT_VARS['user_nickname'] ) {
        
        // If the user is logged in, initialize them and move them to their
        // default location.
        geocoder.getLatLng(GEOCHAT_VARS['default_location'], function(latlng) {
          var latitude = GEOCHAT_VARS['initial_latitude'];
          var longitude = GEOCHAT_VARS['initial_longitude'];
          if (latlng) {
            latitude = latlng.lat();
            longitude = latlng.lng();
          }
          map.setCenter(new GLatLng(latitude, longitude), 13);
          var user = new Person(
              GEOCHAT_VARS['user_nickname'],
              GEOCHAT_VARS['user_email'],
              latitude,
              longitude);
          user.enableUser();
          updateUserPosition();
          update();
        });
      } else {
        
        // If the user isn't logged in, present them with a welcome screen.
        map.setCenter(new GLatLng(GEOCHAT_VARS['initial_latitude'],
                                  GEOCHAT_VARS['initial_longitude']),
                                  13);
        var narrator = new Person(
            'Googleguy',
            GEOCHAT_VARS['user_email'], 
            GEOCHAT_VARS['initial_latitude'],
            GEOCHAT_VARS['initial_longitude']);
        map.disableDragging();
        narrator.say('<a href="' + GEOCHAT_VARS['auth_url'] +
            '">Sign in</a> to your Google Account to start chatting!', true);
            
      }
    }
  };
  
  window.onunload = function() {
    GUnload();
  };

})(jQuery);
