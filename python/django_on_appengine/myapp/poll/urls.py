from django.conf.urls.defaults import *

urlpatterns = patterns('poll.views',
    (r'^$', 'index'),
)

from google.appengine.ext import db

# Create your models here.
    
