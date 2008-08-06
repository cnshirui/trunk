import cgi
import wsgiref.handlers

from google.appengine.ext import webapp
from google.appengine.api import users
from google.appengine.ext import db

class Greeting(db.Model):
  author = db.UserProperty()
  content = db.StringProperty(multiline=True)
  date = db.DateTimeProperty(auto_now_add=True)
  
class Guestbook(webapp.RequestHandler):
  def post(self):
    greeting = Greeting()

    if users.get_current_user():
      greeting.author = users.get_current_user()

    greeting.content = self.request.get('content')
    greeting.put()
    self.redirect('/')

class MainPage(webapp.RequestHandler):
  def get(self):
    user = users.get_current_user()

    if user:  
      self.response.out.write('<html><body>')
  
      greetings = Greeting.all()
      greetings.filter("author =", users.get_current_user())
      greetings.order("-date")      
  
      for greeting in greetings:
        if greeting.author:
          self.response.out.write('<b>%s</b> at %s wrote:' %(greeting.author.nickname(), greeting.date))
        else:
          self.response.out.write('An anonymous person wrote:')
        self.response.out.write('<blockquote>%s</blockquote>' %
                                cgi.escape(greeting.content))
  
      # Write the submission form and the footer of the page
      self.response.out.write("""
            <form action="/sign" method="post">
              <div><textarea name="content" rows="3" cols="60"></textarea></div>
              <div><input type="submit" value="Sign Guestbook"></div>
            </form>
          </body>
        </html>""")
    else:
      self.redirect(users.create_login_url(self.request.uri))
      
    
def main():
  #application = webapp.WSGIApplication([('/', MainPage),('/add', Add)],debug=True)  
  application = webapp.WSGIApplication([('/', MainPage),('/sign', Guestbook)],debug=True)
  wsgiref.handlers.CGIHandler().run(application)

if __name__ == "__main__":
  main()    