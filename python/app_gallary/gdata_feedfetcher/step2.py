# Copyright (C) 2008 Google Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.


__author__ = 'api.jscudder (Jeffrey Scudder)'


import wsgiref.handlers
import urllib
from google.appengine.ext import webapp
from google.appengine.api import users
import gdata.service
import gdata.urlfetch

gdata.service.http_request_handler = gdata.urlfetch

# Change the value of HOST_NAME to the name given to point to your app.
HOST_NAME = 'gdata-feedfetcher.appspot.com'

class Fetcher(webapp.RequestHandler):

  # Initialize some global variables we will use
  def __init__(self):
    # Stores the page's current user
    self.current_user = None
    # Stores the token_scope information
    self.token_scope = None
    # Stores the Google Data Client
    self.client = None
    # The one time use token value from the URL after the AuthSub redirect.
    self.token = None

  def get(self):
    # Write our pages title
    self.response.out.write("""<html><head><title>
        Google Data Feed Fetcher: read Google Data API Atom feeds</title>""")
    # Get the current user
    self.current_user = users.GetCurrentUser()

    self.response.out.write('<body>')
    # Allow the user to sign in or sign out
    if self.current_user:
      self.response.out.write('<a href="%s">Sign Out</a><br>' % (
          users.CreateLogoutURL(self.request.uri)))
    else:
      self.response.out.write('<a href="%s">Sign In</a><br>' % (
          users.CreateLoginURL(self.request.uri)))

    for param in self.request.query.split('&'):
      # Get the token scope variable we specified when generating the URL
      if param.startswith('token_scope'):
        self.token_scope = urllib.unquote_plus(param.split('=')[1])
      # Google Data will return a token, get that
      elif param.startswith('token'):
        self.token = param.split('=')[1]

    # Manage our Authentication for the user
    self.ManageAuth()

    self.response.out.write('<div id="main"></div>')

    self.response.out.write(
        '<div id="sidebar"><div id="scopes"><h4>Request a token</h4><ul>')
    self.response.out.write('<li><a href="%s">Google Documents</a></li>' % (
        self.client.GenerateAuthSubURL(
            'http://%s/step2?token_scope=http://docs.google.com/feeds/' % (
                HOST_NAME), 
            'http://docs.google.com/feeds/', secure=False, session=True)))
    self.response.out.write('</ul></div><br/><div id="tokens">')

  def ManageAuth(self):
    self.client = gdata.service.GDataService()
    if self.token and self.current_user:
      # Upgrade to a session token and store the session token.
      self.UpgradeToken()

  def UpgradeToken(self):
    self.client.SetAuthSubToken(self.token)
    # Sets the session token in self.client.auth_token
    self.client.UpgradeToSessionToken()

def main():
  application = webapp.WSGIApplication([('/.*', Fetcher),], debug=True)
  wsgiref.handlers.CGIHandler().run(application)

if __name__ == '__main__':
  main()
