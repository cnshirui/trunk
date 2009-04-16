# !/usr/bin/env python
#
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

"""Implements the server-side RPCs for the My Hangouts application.

Provides a simple RPC framework for making mostly abstract Python
function calls from JavaScript. Provides a few simple functions for
the My Hangouts application.

This application uses the lower level Datastore API to make datastore
calls and perform queries.
"""

__author__ = 'Kevin Gibbs'

import os
import logging
import datetime
import simplejson
import wsgiref.handlers

from google.appengine.api import datastore
from google.appengine.api import users
from google.appengine.ext import webapp
from google.appengine.ext.webapp import template

# Renders the main template.
class MainPage(webapp.RequestHandler):
  def get(self):
    query = datastore.Query('User')
    current_user = users.GetCurrentUser()
    user_list = {}
    for user in query.Get(100):
      user_list[user['user'].email()] = user['user']
    if current_user:
      user_list[current_user.email()] = current_user
    display_user = self.request.get('user')
    if display_user:
      display_user = users.User(display_user + os.environ['AUTH_DOMAIN'])
    else:
      display_user = current_user
    if len(user_list) > 0 and (not display_user or
                               display_user.email() not in user_list):
      display_user = user_list.values()[0]
    template_values = {
      'users': user_list.values(),
      'current_user': current_user,
      'display_user': display_user,
      'login_url': users.CreateLoginURL(self.request.uri),
      'logout_url': users.CreateLogoutURL(self.request.uri),
    }
    path = os.path.join(os.path.dirname(__file__), "myhangouts.html")
    self.response.out.write(template.render(path, template_values))

# This handler allows the functions defined in the RPCHandler class to
# be called automatically by remote code.
class RPCHandler(webapp.RequestHandler):
  def get(self):
    action = self.request.get('action')
    arg_counter = 0;
    args = ()
    while True:
      arg = self.request.get('arg' + str(arg_counter))
      arg_counter += 1
      if arg:
        args += (simplejson.loads(arg),);
      else:
        break;
    result = getattr(self, action)(*args)
    self.response.out.write(simplejson.dumps((result)))

  # The RPCs exported to JavaScript follow here:

  def AddLocation(self, latd, longd, name):
    user = users.GetCurrentUser()
    query = datastore.Query('User')
    query['user ='] = user
    if (query.Count() == 0):
      user_entity = datastore.Entity('User')
      user_entity['user'] = user
      datastore.Put(user_entity)
    location = datastore.Entity("Location")
    location['user'] = user
    location['latd'] = latd
    location['longd'] = longd
    location['name'] = name
    datastore.Put(location)
    # Add a few additional attributes so that they are available to
    # the remote user.
    location['user'] = location['user'].email()
    location['key'] = str(location.key())
    return location

  def RemoveLocation(self, key):
    datastore.Delete(datastore.Key(key.encode('utf-8')))
    return True

  def GetLocations(self, user_email=None):
    if user_email:
      user = users.User(user_email)
    else:
      user = users.GetCurrentUser()
    query = datastore.Query('Location')
    if not user:
      return []
    query['user ='] = user
    locations = []
    for location in query.Get(100):
      # Add a few additional attributes so that they are available to
      # the remote user.
      location['user'] = location['user'].email()
      location['key'] = str(location.key())
      locations += [location]
    return locations

def main():
  application = webapp.WSGIApplication([
    ('/', MainPage),
    ('/rpc', RPCHandler),
    ], debug=True)
  wsgiref.handlers.CGIHandler().run(application)

if __name__ == '__main__':
  main()
