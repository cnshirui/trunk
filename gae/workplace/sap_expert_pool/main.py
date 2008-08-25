import os,sys
os.environ['DJANGO_SETTINGS_MODULE'] = 'apps.settings'

# Google App Engine imports.
from google.appengine.ext.webapp import util

# Force Django to reload its settings.
from django.conf import settings
settings._target = None

import django.core.handlers.wsgi
import django.core.signals
import django.db
import django.dispatch.dispatcher


from google.appengine.api import users
from google.appengine.ext import webapp
from google.appengine.ext.webapp.util import run_wsgi_app

#class MainPage(webapp.RequestHandler):
#  def get(self):
#     user = users.get_current_user()
#  if user:
#     self.response.headers['Content-Type'] = 'text/plain'
#     self.response.out.write('Hello, ' + user.nickname())
#  else:
#    self.redirect(users.create_login_url(self.request.uri))
#    application = webapp.WSGIApplication(
#                                [('/', MainPage)],
#                                debug=True)
#
#  def main():
#    run_wsgi_app(application)
class ForceLoginPage(webapp.RequestHandler):
  """ Base class of all our normal request handlers which makes sure the user
      is logged in before rendering the page.
  """

  def get(self):
    """ Handler for get - redirects to the login page if necessary, otherwise
        calls our subclass to generate the response.
    """
    user = users.GetCurrentUser()
    if user:
      response = self.Get(user)
      if response:
        self.response.out.write(response)
    else:
      self.redirect(users.CreateLoginURL(self.request.uri))      


class MyHandler(webapp.RequestHandler):
  def get(self):
    user = users.get_current_user()
    if user:
      greeting = ("Welcome, %s! (<a href=\"%s\">sign out</a>)" %
                  (user.nickname(), users.create_logout_url("/")))
    else:
      greeting = ("<a href=\"%s\">Sign in or register</a>." %
                  users.create_login_url("/"))
    self.response.out.write("<html><body>%s</body></html>" % greeting) 


class IndexPage(ForceLoginPage):
  """ Renderer for a page that shows the game lobby """
  template_path = os.path.join(os.path.dirname(__file__), 'apps/templates/index.html')
  def Get(self, user):
    template_values = {
      'user' : user,
      'logout_url' : users.CreateLogoutURL('/'),
      'mainContentClass' : 'indexContent'
      }
    return template.render(IndexPage.template_path, template_values)

# Log errors.
#django.dispatch.dispatcher.connect(
#   log_exception, django.core.signals.got_request_exception)

# Unregister the rollback event handler.
  django.dispatch.dispatcher.disconnect(
    django.db._rollback_on_exception,
    django.core.signals.got_request_exception)

def main():

  # Create a Django application for WSGI.
#  application = django.core.handlers.wsgi.WSGIHandler(
#    [('/', MyHandler)], debug=True)
  application = webapp.WSGIApplication(
                                [('/', MyHandler), ('/apps/templates/index', IndexPage)],
                                debug=True)

  # Run the WSGI CGI handler with that application.
  util.run_wsgi_app(application)

if __name__ == '__main__':
  main()


