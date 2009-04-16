
# Copyright 2008 Google Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
# 
#     http://www.apache.org/licenses/LICENSE-2.0
# 
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

"""Handlers for Geochat user events.

Contains several RequestHandler subclasses used to handle put and get
operations, along with any helper functions. This script is designed to be
run directly as a WSGI application, and within Geochat handles all URLs
under /event.

  UpdateHandler: Handles user requests for updated lists of events.
  ChatHandler: Handles user chat input events.
  MoveHandler: Handles user movement events.
  RefreshCache(): Checks the age of the cache, and updates if necessary.
"""

# TODO Cache sync problems.
# TODO Problem with duplicate messages.
# TODO Spam controls.

import datetime
import settings
import logging
import os
import time
import wsgiref.handlers

import datamodel
import json
import settings

from google.appengine.api import users
from google.appengine.ext import db
from google.appengine.ext import webapp

# The time interval between syncs as a timedelta.
sync_interval = datetime.timedelta(0, 10)

# A datetime indicating the last time the chat cache was synced from the DB.
last_sync = datetime.datetime.now() - sync_interval

# A list storing the chat cache.
chat_cache = []

# A list storing the move cache.
move_cache = []


class UpdateHandler(webapp.RequestHandler):
  
  """Handles user requests for updated lists of events.
  
  UpdateHandler only accepts "get" events, sent via web forms. It expects each
  request to include "min_latitude", "min_longitude", "max_latitude",
  "max_longitude", "zoom", and "since" fields.
  """
  
  def get(self):
    global sync_interval
    global last_sync
    global chat_cache
    global move_cache

    min_latitude = float(self.request.get('min_latitude'))
    min_longitude = float(self.request.get('min_longitude'))
    max_latitude = float(self.request.get('max_latitude'))
    max_longitude = float(self.request.get('max_longitude'))
    zoom = self.request.get('zoom')
    if self.request.get('since') == '':
      since = 0
    else:
      since = float(self.request.get('since'))
    since_datetime = datetime.datetime.fromtimestamp(since)
    
    # Restrict latitude/longitude to restrict bulk downloads.
    if (max_latitude - min_latitude) > 1:
      max_latitude = min_latitude + 1
    if (max_longitude - min_longitude) > 1:
      max_longitude = min_longitude + 1
    
    
    chat_events = []
    move_events = []
    
    if since > 0:
      RefreshCache()
      for entry in chat_cache:
        if (entry.timestamp > since_datetime and
            entry.latitude > min_latitude and
            entry.latitude < max_latitude and
            entry.longitude > min_longitude and
            entry.longitude < max_longitude):
          chat_events.append(entry)
      
      for entry in move_cache:
        if (entry['timestamp'] > since_datetime and
            entry['latitude'] > min_latitude and
            entry['latitude'] < max_latitude and
            entry['longitude'] > min_longitude and
            entry['longitude'] < max_longitude):
          move_events.append(entry)        
              
    output = {
        'timestamp': time.time(),
        'chats': chat_events,
        'moves': move_events,
      }

    self.response.headers['Content-Type'] = 'text/plain'
    self.response.out.write(json.encode(output));
    

class MoveHandler(webapp.RequestHandler):
  
  """Handles user movement events.
  
  MoveHandler only provides a post method for receiving new user co-ordinates,
  and doesn't store any data to the datastore as ChatHandler does with
  ChatEvents, instead just adding straight to the local cache.
  """
  
  def post(self):
    global move_cache

    # Get the current user and return if not logged in.
    user = users.get_current_user()
    if user == None:      
      return
    
    # Construct an ad hoc event dictionary.
    event_dict = {
      'user': user,
      'timestamp': datetime.datetime.now(),
      'latitude': float(self.request.get('latitude')),
      'longitude': float(self.request.get('longitude')),
      'zoom': int(self.request.get('zoom')),
    }
    
    # Append to the move cache, so we don't need to wait for a refresh.
    move_cache.append(event_dict)


class ChatHandler(webapp.RequestHandler):

  """Handles user chat events.
  
  Chathandler only provides a post method for receiving user chat contents
  and related positioning. This data is immediately dumped to the datastore,
  without batching, and appended to the local cache.
  """

  def post(self):
    global chat_cache
    
    # Get the current user and return if not logged in.
    user = users.get_current_User()
    if user == None:      
      return

    # Create and insert the a new chat event.
    event = datamodel.ChatEvent()
    event.user      = user
    event.contents  = self.request.get('contents')
    event.latitude  = float(self.request.get('latitude'))
    event.longitude = float(self.request.get('longitude'))
    event.zoom      = int(self.request.get('zoom'))
    event.put()

    # Append to the chat cache, so we don't need to wait on a refresh.
    chat_cache.append(event)
    
    
def RefreshCache():
  
  """Check the freshness of chat and move caches, and refresh if necessary.
  
  RefreshCache relies on the globals "sync_interval" and "last_sync" to
  determine the age of the existing cache and whether or not it should be
  updated. All output goes to "chat_cache" and "move_cache" globals.
  """
  
  global sync_interval
  global last_sync
  global chat_cache
  global move_cache
  
  now = datetime.datetime.now()
  sync_frame = sync_interval * 2
  
  if last_sync < now - sync_interval:
    
    # Sync the chat cache.
    query = db.Query(datamodel.ChatEvent)
    query.filter('timestamp > ', now - sync_frame)
    query.order('timestamp')
    chat_cache = list(query.fetch(100))
    last_sync = datetime.datetime.now()
    logging.info('Chat cache refreshed.')
    
    # Trim the move cache.
    move_cache = move_cache[-100:]
    

  
def main():
  
  """Main method called when the script is executed directly.
  
  This method is called each time the script is launched, and also has the
  effect of enabling caching for global variables.
  """
  
  application = webapp.WSGIApplication(
      [
        ('/event/update', UpdateHandler),
        ('/event/chat', ChatHandler),
        ('/event/move', MoveHandler),
      ],
      debug = True)
  wsgiref.handlers.CGIHandler().run(application)

if __name__ == '__main__':
  main()
