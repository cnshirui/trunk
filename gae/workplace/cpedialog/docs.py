__author__ = 'Ping Chen'

import os
import re
import datetime
import calendar
import logging
import string
from google.appengine.ext.webapp import template
from google.appengine.api import users
from google.appengine.ext import webapp
from google.appengine.ext import db
from google.appengine.api import memcache

import config
import simplejson
import cgi

class BaseRequestHandler(webapp.RequestHandler):
  def generate(self, template_name, template_values={}):
    values = {
      'request': self.request,
    }
    values.update(template_values)
    directory = os.path.dirname(__file__)
    path = os.path.join(directory, os.path.join('templates', template_name))
    view.ViewPage(cache_time=0).render(self, template_name,values)

