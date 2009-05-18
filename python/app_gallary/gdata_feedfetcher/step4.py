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
import cgi
from google.appengine.ext import webapp
from google.appengine.api import users
from google.appengine.ext import db
import gdata.service
import gdata.urlfetch


gdata.service.http_request_handler = gdata.urlfetch


# Change the value of HOST_NAME to the name given to point to your app.
HOST_NAME = 'gdata-feedfetcher.appspot.com'


class StoredToken(db.Model):
  user_email = db.StringProperty(required=True)
  session_token = db.StringProperty(required=True)
  target_url = db.StringProperty(required=True)


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
    # Feed URL which should be fetched by the gdata client.
    self.feed_url = None

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
      # Find out what the target URL is that we should attempt to fetch.
      elif param.startswith('feed_url'):
        self.feed_url = urllib.unquote_plus(param.split('=')[1])

    # If we received a token for a specific feed_url and not a more general 
    # scope, then use the exact feed_url in this request as the scope for the
    # token.
    if self.token and self.feed_url and not self.token_scope:
      self.token_scope = self.feed_url

    # Manage our Authentication for the user
    self.ManageAuth()
    self.LookupToken()

    self.response.out.write('<div id="main">')
    self.FetchFeed()
    self.response.out.write('</div>')

    self.response.out.write(
        '<div id="sidebar"><div id="scopes"><h4>Request a token</h4><ul>')
    self.response.out.write('<li><a href="%s">Google Documents</a></li>' % (
        self.client.GenerateAuthSubURL(
            'http://%s/step4?token_scope=http://docs.google.com/feeds/' % (
                HOST_NAME), 
            'http://docs.google.com/feeds/', secure=False, session=True)))
    self.response.out.write('</ul></div><br/><div id="tokens">')

  def ManageAuth(self):
    self.client = gdata.service.GDataService()
    if self.token:
      # Upgrade to a session token and store the session token.
      self.UpgradeAndStoreToken()

  def UpgradeAndStoreToken(self):
    self.client.SetAuthSubToken(self.token)
    self.client.UpgradeToSessionToken()
    if self.current_user:
      # Create a new token object for the data store which associates the
      # session token with the requested URL and the current user.
      new_token = StoredToken(user_email=self.current_user.email(), 
          session_token=self.client.GetAuthSubToken(), 
          target_url=self.token_scope)
      new_token.put()

  def LookupToken(self):
    if self.feed_url and self.current_user:
      stored_tokens = StoredToken.gql('WHERE user_email = :1',
          self.current_user.email())
      for token in stored_tokens:
        if self.feed_url.startswith(token.target_url):
          self.client.SetAuthSubToken(token.session_token)
          return

  def FetchFeed(self):
    # Attempt to fetch the feed.
    if not self.feed_url:
      self.response.out.write(
          'No feed_url was specified for the app to fetch.<br/>')
      self.response.out.write('Here\'s an example query which will show the'
          ' XML for the feed listing your Google Documents <a '
          'href="http://%s/step4' 
          '?feed_url=http://docs.google.com/feeds/documents/private/full">'
          'http://%s/step4' 
          '?feed_url=http://docs.google.com/feeds/documents/private/full'
          '</a>' % (HOST_NAME, HOST_NAME))
      return
    if not self.client:
      self.client = gdata.service.GDataService()
    try:
      response = self.client.Get(self.feed_url, converter=str)
      self.response.out.write(cgi.escape(response))
    except gdata.service.RequestError, request_error:
      # If fetching fails, then tell the user that they need to login to
      # authorize this app by logging in at the following URL.
      if request_error[0]['status'] == 401:
        # Get the URL of the current page so that our AuthSub request will
        # send the user back to here.
        next = self.request.uri
        auth_sub_url = self.client.GenerateAuthSubURL(next, self.feed_url,
            secure=False, session=True)
        self.response.out.write('<a href="%s">' % (auth_sub_url))
        self.response.out.write(
            'Click here to authorize this application to view the feed</a>')
      else:
        self.response.out.write(
            'Something else went wrong, here is the error object: %s ' % (
                str(request_error[0])))


def main():
  application = webapp.WSGIApplication([('/.*', Fetcher),], debug=True)
  wsgiref.handlers.CGIHandler().run(application)

if __name__ == '__main__':
  main()
