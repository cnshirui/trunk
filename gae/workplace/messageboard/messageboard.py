# -*- coding: utf-8 -*-

import os
import re
import cgi
import datetime
import wsgiref.handlers
from google.appengine.ext import db
from google.appengine.api import users
from google.appengine.ext import webapp
from google.appengine.ext.webapp import template


class Message(db.Model):
    title=db.StringProperty()
    nickname=db.StringProperty()
    email=db.EmailProperty()
    website=db.LinkProperty()
    content=db.StringProperty(multiline=True)
    reply=db.StringProperty(multiline=True)
    ipaddress=db.StringProperty()
    adddate=db.DateTimeProperty(auto_now_add=True)
    
class MainPage(webapp.RequestHandler):
    def get(self):
        messages = db.GqlQuery("SELECT * FROM Message ORDER BY adddate DESC LIMIT 10")
        for message in messages:          
            self.response.out.write('%s at %s said:'%(message.nickname,message.adddate))
	    self.response.out.write('<hr/>Email: %s<br/>WebSite: %s<br/>Message: <blockquote>%s</blockquote>' %(message.email,message.website,cgi.escape(message.content)))
        self.response.out.write("""
          <form action="/add" method="post">
	    <div>Title: <input type="text" name="title"/></div>
	    <div>Name: <input type="text" name="nickname"/></div>
	    <div>Email: <input type="text" name="email"/></div>
	    <div>WebSite: <input type="text" name="website"/></div>
	    <div>Message: <textarea name="content" rows="3" cols="60"></textarea></div>
            <div><input type="submit" value="Submit"></div>
          </form>
        </body>
      </html>""")
        
class Add(webapp.RequestHandler):
    def post(self):
        message = Message()
        message.title = self.request.get('title')
        message.nickname = self.request.get('nickname')
        message.email = self.request.get('email')
        message.website = self.request.get('website')
        message.content = self.request.get('content')
        message.put()
        self.redirect('/')
        
        
def main():
    application = webapp.WSGIApplication([('/', MainPage),('/add', Add)],debug=True)
    wsgiref.handlers.CGIHandler().run(application)
        
if __name__ == "__main__":
    main()
