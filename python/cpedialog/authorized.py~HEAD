__author__ = 'Ping Chen'


from google.appengine.api import users
from google.appengine.ext import db
import gdata.service

import urllib

from model import AuthSubStoredToken

def role(role):
    """A decorator to enforce user roles, currently 'user' (logged in) and 'admin'.

    To use it, decorate your handler methods like this:

    import authorized
    @authorized.role("admin")
    def get(self):
      user = users.GetCurrentUser(self)
      self.response.out.write('Hello, ' + user.nickname())

    If this decorator is applied to a GET handler, we check if the user is logged in and
    redirect her to the create_login_url() if not.

    For HTTP verbs other than GET, we cannot do redirects to the login url because the
    return redirects are done as GETs (not the original HTTP verb for the handler).  
    So if the user is not logged in, we return an error.
    """
    def wrapper(handler_method):
        def check_login(self, *args, **kwargs):
            user = users.get_current_user()
            if not user:
                if self.request.method != 'GET':
                    self.error(403)
                else:
                    self.redirect(users.create_login_url(self.request.uri))
            elif role == "user" or (role == "admin" and users.is_current_user_admin()):
                return handler_method(self, *args, **kwargs)
            else:
                if self.request.method == 'GET':
                    self.redirect("/403.html")
                else:
                    self.error(403)   # User didn't meet role.
        return check_login
    return wrapper


def authSub(service):
    """A decorator to make sure the user have granted access to the specified service by Google.'.

    To use it, decorate your handler methods like this:

    import authorized
    @authorized.authSub("albums")
    def get(self):
    """
    def wrapper(handler_method):
        def check_authSub(self, *args, **kwargs):
            user = users.get_current_user()
            if not user:
                if self.request.method != 'GET':
                    self.error(403)
                else:
                    self.redirect(users.create_login_url(self.request.uri))
            else:
                client = gdata.service.GDataService()
                token = None
                #store or update the session token if the url come from the "Grant access" page.
                service_ = None
                token_ = None
                for param in self.request.query.split('&'):
                    if param:
                        # Get the service variable we specified when generating the URL
                        if param.startswith('service'):
                            service_ = urllib.unquote_plus(param.split('=')[1])
                        elif param.startswith('token'):
                            token_ = param.split('=')[1]
                if service_ is not None and token_ is not None :
                    token = UpgradeAndStoreToken(client, user.email(), service_, token_)

                if token is None:
                    token =  LookupToken(user.email(),service)
                if token is None:
                    next = self.request.uri
                    query = {"service":service}
                    next += (next.count('?') and '&amp;' or '?')+urllib.urlencode(query)
                    auth_sub_url = gdata.service.GDataService().GenerateAuthSubURL(next, scope[service],
                        secure=False, session=True)
                    self.redirect(auth_sub_url)
                else:
                    try:
                        return handler_method(self, *args, **kwargs)
                    except gdata.service.RequestError, request_error:
                        #remove the invalid session token.
                        DeleteUnvalidToken(user.email(),service)
                        
                        # If fetching fails, then tell the user that they need to login to
                        # authorize this app by logging in at the following URL.
                        if request_error[0]['status'] == 401:
                          # Get the URL of the current page so that our AuthSub request will
                          # send the user back to here.
                            next = self.request.uri
                            query = {"service":service}
                            next += (next.count('?') and '&amp;' or '?')+urllib.urlencode(query)
                            auth_sub_url = gdata.service.GDataService().GenerateAuthSubURL(next, scope[service],
                                secure=False, session=True)
                            self.redirect(auth_sub_url)
                        else:
                            self.error(403)
        return check_authSub
    return wrapper


def UpgradeAndStoreToken(client,email,service,token):
    client.SetAuthSubToken(token)
    client.UpgradeToSessionToken()
  
    stored_token = AuthSubStoredToken.gql('WHERE user_email = :1 and target_service = :2',
        email, service).get()
    if stored_token:
        stored_token.session_token = token
        stored_token.put()
    else:
        new_token = AuthSubStoredToken(user_email=email,
            session_token=client.GetAuthSubToken(),
            target_service=service)
        new_token.put()
    return client.GetAuthSubToken()

def LookupToken(email, service):
    stored_token = AuthSubStoredToken.gql('WHERE user_email = :1 and target_service = :2',
        email, service).get()
    if stored_token:
        return stored_token.session_token
    else:
        return None

def DeleteUnvalidToken(email, service):
    stored_token = AuthSubStoredToken.gql('WHERE user_email = :1 and target_service = :2',
        email, service).get()
    if stored_token:
        return stored_token.delete()

scope = {
    "calendar":"http://www.google.com/calendar/feeds/",
    "docs":"http://docs.google.com/feeds/",
    "albums":"http://picasaweb.google.com/data/feed/",
    "blogger":"http://www.blogger.com/feeds/",
    "base":"http://www.google.com/base/feeds/",
    "site":"https://www.google.com/webmasters/tools/feeds/",
    "spreadsheets":"http://spreadsheets.google.com/feeds/",
    "codesearch":"http://www.google.com/codesearch/feeds/",
    "finance":"http://finance.google.com/finance/feeds/",
    "contacts":"http://www.google.com/m8/feeds/",
    "youtube":"http://gdata.youtube.com/feeds/",
}