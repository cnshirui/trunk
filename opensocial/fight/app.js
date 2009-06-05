// Copyright 2008 Google Inc.
// All Rights Reserved.

/**
 * @fileoverview Contains an OpenSocial application of interactive game
 * with their friends' profile pic as e.g. targets for shooting or stomping.
 *
 * There are three classes.
 *    1. class Player: user who is playing the game.
 *    2. class Game: the game to be played that contains Sprite objects.
 *    3. class Sprite: profile images that move around for player to zap.
 *
 * Requires prototype.js library for event handler.
 * @author shawnshen@google.com (Shawn Shen)
 */

/**
 * Creating a namespace for this game file.
 * @type {Object}
 */
var ss = ss || {};

/**
 * Create ss.player.
 * @type {Object}
 */
ss.player = {};

/**
 * Create ss.game.
 * @type {Object}
 */
ss.game = {};

/**
 * Player 
 * Initialize with uid, name, img.
 * @param {string} uid The user id for a user.
 * @param {string} name The user name for a user.
 * @param {string} img The profile image URL for a user.
 * @constructor
 */
ss.Player = function(uid, name, img) {
  // public data for user.
  this.uid = uid;
  this.name = name;
  this.img = img;

  // public data for stats.
  this.hits = 0;
  this.hitFlag = 0;
};

/**
 * Sets hitFlag to zero when a click event misses target sprite. 
 */
ss.Player.prototype.missTarget = function() {
  this.hitFlag = 0;
};

/**
 * Sets hitFlag to one when a click event hits target sprite. 
 * Increments hits counter for stats. 
 */
ss.Player.prototype.hitTarget = function() {
  this.hitFlag = 1;
  this.hits++;
};

/**
 * Game 
 * Initializes private data, public counters and
 * an array of Sprite objects.
 * Calls showLevel to display current game level.
 * @constructor
 */
ss.Game = function() {
  // private data
  this.spriteSize_ = 50;
  this.maxX_ = 600;
  this.maxY_ = 300;
  this.maxLevel_ = 5;
  this.minSize_ = 10;

  // mainDivId_ The main canvas div.
  this.mainDivId_ = 'sprite';
  // levelDivId_ The div for game level.
  this.levelDivId_ = 'level';
  // spriteDivPrefix_ The prefix for sprite div.
  this.spriteDivPrefix_ = 'sprite';
  //MSG_CONGRATS_ Congrats message for completion.
  this.MSG_CONGRATS_ = 'Congratulations!';

  // public data
  this.level = 1;
  this.spriteCounter = 0;
  this.spriteKilled = 0;
  this.spriteDead = 0;
  this.sprites = [];
};



/**
 * Increment game level when level is less than maxLevel_.
 */
ss.Game.prototype.incrementLevel = function() {
  if (this.level < this.maxLevel_) {
    this.level++;
  }
};

/**
 * Show current game level in div levelDivId_.
 */
ss.Game.prototype.showLevel = function() {
  var elm = document.getElementById(this.levelDivId_);
  if (this.level < this.maxLevel_) {
    elm.innerHTML = this.level;
  } else {
    elm.innerHTML = this.MSG_CONGRATS_;
  }
};

/**
 * Starts a game when a user clicks on 'Start' button.
 * Resets all counters.
 * Makes all sprites alive.
 * Resets images of all sprites to original size.
 * Resets with random speeds for sprites.
 * Calls private move_ function.
 */
ss.Game.prototype.start = function() {
  this.spriteKilled = 0;
  this.spriteDead = 0;

  for (var i = 0; i < this.spriteCounter; i++) {
    this.sprites[i].alive = true;
    this.sprites[i].sizeX = this.spriteSize_;
    this.sprites[i].sizeY = this.spriteSize_;

    var angle = Math.random() * 6.28;
    var speedX = this.level * Math.sin(angle) / 2;
    var speedY = this.level * Math.sin(angle) / 2;

    this.sprites[i].speedX = speedX;
    this.sprites[i].speedY = speedY;
  }
  this.move_();
};

/**
 * Moves sprites randomly on mainDivId_ div.
 * Note that this game can be extended by adding different move methods.
 * @private
 */
ss.Game.prototype.move_ = function() {
  for (var i = 0; i < this.spriteCounter; i++) {
    if (this.sprites[i].alive) {
      // move sprite images
      this.sprites[i].x += this.sprites[i].speedX;
      this.sprites[i].y += this.sprites[i].speedY;

      // shrink sprite images
      this.sprites[i].sizeX -= 0.1;
      this.sprites[i].sizeY -= 0.1;

      // handles vertical bounce for sprite images
      if (this.sprites[i].y > this.maxY_ || this.sprites[i].y <= 0) {
        this.sprites[i].speedY = 0 - this.sprites[i].speedY;
      }
      // handles horizontal bounce for sprite images
      if (this.sprites[i].x > this.maxX_ || this.sprites[i].x <= 0) {
        this.sprites[i].speedX = 0 - this.sprites[i].speedX;
      }

      // kill sprites when images are smaller than minSize_
      if (this.sprites[i].sizeX <= this.minSize_ ||
          this.sprites[i].sizeY <= this.minSize_) {
        this.sprites[i].alive = false;
        this.spriteDead++;
      }
    }

    // Draws sprites image div with updated sizes.
    this.sprites[i].drawSprite();
  }

  // Increments level when all sprites killed and show level.
  if (this.spriteCounter == this.spriteKilled) {
    this.incrementLevel();
    this.showLevel();
  }

  if (this.spriteDead < this.spriteCounter) {
    // Continues the timer loop with move_.
    setTimeout('ss.game.move_()', 10);
  }
};

/**
 * Adds a Sprite object to sprites array of Game object.
 * Randomizes position (x,y).
 * Randomizes speed (speedX, speedY).
 * Sets sprite image size.
 */
ss.Game.prototype.addSprite = function() {
  var x = this.rand(10, this.maxX_ - 10);
  var y = this.rand(10, this.maxY_ - 10);

  var angle = Math.random() * 2 * Math.PI;
  var speedX = this.level * Math.sin(angle) / 2;
  var speedY = this.level * Math.sin(angle) / 2;
  var sizeX = this.spriteSize_;
  var sizeY = this.spriteSize_;

  this.sprites[this.spriteCounter] = new ss.Sprite(x, y, speedX, speedY,
      sizeX, sizeY, this.spriteDivPrefix_ + this.spriteCounter);
  this.spriteCounter++;
};

/**
 * Utility function for generating random numbers.
 * @param {number} min Lower limit of random number.
 * @param {number} max Upper limit of random number.
 * @return {number} Return random number.
 */
ss.Game.prototype.rand = function(min, max) {
  return Math.round(Math.random() * (max - min)) + min;
};


/**
 * Sprite 
 * Initializes position (x,y), speed (speedX, speedY).
 * @param {integer} x The x-position of sprite image.
 * @param {integer} y The y-position of sprite image.
 * @param {integer} speedX The x-speed of sprite image.
 * @param {integer} speedY The y-speed of sprite image.
 * @param {integer} sizeX The x-size of sprite image.
 * @param {integer} sizeY The y-size of sprite image.
 * @param {string} sId The div id of sprite image.
 * @constructor
 */
ss.Sprite = function(x, y, speedX, speedY, sizeX, sizeY, sId) {
  this.x = x;
  this.y = y;
  this.speedX = speedX;
  this.speedY = speedY;
  this.sizeX = sizeX;
  this.sizeY = sizeY;
  this.alive = true;

  this.ref = document.getElementById(sId);
}

/**
 * Updates size of sprite image.
 * Renders only if sprite alive.
 * Adds event listener for click on sprite image div.
 */
ss.Sprite.prototype.drawSprite = function() {
  this.ref.style.left = this.x + 'px';
  this.ref.style.top = this.y + 'px';
  this.ref.style.width = this.sizeX + 'px';
  this.ref.style.height = this.sizeY + 'px';
  this.ref.style.display = this.alive ? 'block' : 'none';

  Event.observe(this.ref, 'click', ss.zap, true);
};

/**
 * Zaps a sprite and makes it disappear.
 * Sets clicked sprite.alive to false.
 * Updates spriteKilled and spriteDead counters. 
 * Calls player.hitTarget to tally scores.
 * @param {event} e The mouse event.
 */
ss.zap = function(e) {
  if (e) {
    var eventTargetName = e.target.id;
    var spriteNum = eventTargetName.match(/sprite(\d+)/);
    var eventTargetNum = spriteNum[1];
    ss.game.sprites[eventTargetNum].alive = false;
    this.alive = false;
    ss.game.spriteKilled++;
    ss.game.spriteDead++;
    ss.player.hitTarget();
  } else {
    this.alive = false;
  }
};


/**
 * Creates OpenSocial data request object to 
 * fetch user data e.g. profile and friends and
 * calls onLoadFriendsInfo to process returned data.  
 * Gets called by OpenSocial OnLoadHandler.
 */
ss.init = function() {
  // Creates a generic OpenSocial data request object.
  var req = opensocial.newDataRequest();
  // Sets parameters to be passed with the OS request.
  var params = {};
  params[opensocial.DataRequest.PeopleRequestFields.MAX] = 200;
  params[opensocial.DataRequest.PeopleRequestFields.PROFILE_DETAILS] = [
    opensocial.Person.Field.ABOUT_ME,
    opensocial.Person.Field.STATUS,
    opensocial.Person.Field.PROFILE_URL,
    opensocial.Person.Field.THUMBNAIL_URL
  ];

  // Creates FetchPersonRequest and FetchPeopleRequest objects.
  req.add(req.newFetchPersonRequest(opensocial.DataRequest.PersonId.OWNER,
      params), 'viewer');
  req.add(req.newFetchPeopleRequest('VIEWER_FRIENDS', params), 'viewerFriends');

  // Sends request and invoke callback function to parse response.
  req.send(ss.onLoadFriendsInfo);
};

/**
  * Processes returned viewer data to construct a ss.Player object.
  * Processes returned viewerFriends data to construct a Game object
  * with sprites image src set to profile images of user's friends.
  * @param {opensocial.DataResponse} data Stores viewer/friend that are returned.
  */
ss.onLoadFriendsInfo = function(data) {
  // Gets viewer data for instantiating player object.
  var viewer = data.get('viewer').getData();
  var uid = viewer.getField(opensocial.Person.Field.ID);
  var img = viewer.getField(opensocial.Person.Field.THUMBNAIL_URL);
  var name = viewer.getDisplayName();
  ss.player = new ss.Player(uid, name, img);

  // Instantiates Game object.
  ss.game = new ss.Game();

  // Adds event listener for click on mainDivId_ div. 
  Event.observe(ss.game.mainDivId_, 'click', ss.player.missTarget.bindAsEventListener(ss), false);

  var count = 0;
  var index = 0;
  // Loops through and processes viewer's friends data.
  var viewerFriends = data.get('viewerFriends').getData();
  viewerFriends.each(function(person) {
    if (count < 10) {
      var fimg = person.getField(opensocial.Person.Field.THUMBNAIL_URL);
      // Sets sprite image src.
      var sprite = document.getElementById(ss.game.spriteDivPrefix_ + index);
      sprite.src = fimg;
      // Instantiates sprite object.
      ss.game.addSprite();
      // Renders sprite to mainDivId_ div.
      ss.game.sprites[index].drawSprite();
      index++;
    }
    count++;
  });
};
