from google.appengine.api import users
from django.http import HttpResponseRedirect, HttpResponseForbidden,HttpResponse
from google.appengine.api import memcache
import logging
from django.conf import settings
#import datetime

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
        def check_login(*args, **kwargs):
            user = users.get_current_user()
            request = args[0]
            if not user:
                if request.method != 'GET':
                    return HttpResponseForbidden()
                else:
                    return HttpResponseRedirect(users.create_login_url(request.get_full_path()))
            elif role == "admin" and users.is_current_user_admin():
                return handler_method(*args, **kwargs)
            else:
                if request.method == 'GET':
                    return HttpResponseForbidden()
                else:
                    return HttpResponseForbidden()   # User didn't meet role.
        return check_login
    return wrapper


#def cache(key="", time=180):
#    def decorate(method):
#        def _wrapper(*args, **kwargs):
##                if not g_blog.enable_memcache:
##                    method(*args, **kwargs)
##                    return
#            request = args[0]
#            if request.method == "GET":
#                skey = key + request.get_full_path()
#                logging.info('skey:' + skey)
#                cachedResponse = memcache.get(skey)
#                if cachedResponse:
#                    logging.info('cache:' + skey)
#                    return cachedResponse
#                else:
#                    response = method(*args, **kwargs)
#                    memcache.add(skey, response, time)
#                    return response
#            else:
#                return method(*args, **kwargs)
#        return _wrapper
#    return decorate

from django.utils.cache import get_cache_key
def update_cache(function):
  """
      update cache after post changed
  """
  def update_cache_wrapper(request, *args, **kw):
    cache_key=get_cache_key(request)
    response = function(request,*args, **kw)
    memcache.set(cache_key, response, settings.CACHE_MIDDLEWARE_SECONDS)
    return response
  return update_cache_wrapper
  

def login_required(function):
  """Implementation of Django's login_required decorator.
  
  The login redirect URL is always set to request.path
  """
  def login_required_wrapper(request, *args, **kw):
    if request.user.is_authenticated():
      return function(request, *args, **kw)
    return HttpResponseRedirect(users.create_login_url(request.path))
  return login_required_wrapper
  
def admin_required(function):
  """Implementation of Django's login_required decorator.
  
  The login redirect URL is always set to request.path
  """
  def admin_required_wrapper(request, *args, **kw):
    if request.user.is_authenticated() and users.is_current_user_admin():
      return function(request, *args, **kw)
    return HttpResponseRedirect(users.create_login_url(request.path))
  return admin_required_wrapper


#def format_date(dt):
#    return dt.strftime('%a, %d %b %Y %H:%M:%S GMT')
#http://code.google.com/p/n23/source/browse/trunk/cache.py
def cache(key="",time=60*5):
    """A decorator to cache the search results by url"""
    def _decorate(method):
        def _wrapper(*args, **kwargs):
#            if not g_blog.enable_memcache:
#                method(*args, **kwargs)
#                return
            request=args[0]
            if request.method == "GET":
                skey=""
                user = users.get_current_user()
                if user:                
                    skey+=user.nickname()+'_'
                else:
                    skey+='anonymous_'
                
                skey+=key
                skey+=request.get_full_path()
                logging.info('skey:'+skey)
                cachedResponse= memcache.get(skey)
                if cachedResponse:
                    logging.info('cache:'+skey)                
                    return cachedResponse
                else:
                    response=method(*args, **kwargs)
                    memcache.set(skey,response,time)
                    return response
            else:
                return method(*args, **kwargs)
        return _wrapper
    return _decorate

