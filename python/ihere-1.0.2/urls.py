# -*- coding: utf-8 -*-
from django.conf.urls.defaults import *
from ragendja.urlsauto import urlpatterns
from ragendja.auth.urls import urlpatterns as auth_patterns
from django.contrib import admin
from django.conf.urls.defaults import *
from blog.feeds import LatestEntries, LatestEntriesByCategory, PostEntry
from blog.views import list_post, view_post,view_post_perma
from django.views.generic.simple import redirect_to, direct_to_template
from django.views.generic import list_detail, date_based, create_update  
from django.views.generic.simple import direct_to_template
from google.appengine.api import users
from django.utils.encoding import force_unicode,smart_str
from blog.models import Post, Comment, Event,Tag,Category
import urllib
from django.conf import settings

admin.autodiscover()

handler500 = 'ragendja.views.server_error'

urlpatterns = auth_patterns + patterns('',
    ('^admin/(.*)', admin.site.root),
) + urlpatterns



urlpatterns += patterns('blog.views',
    url(r'^$', 'list_post',name='list_post'),
    (r'^(?P<page>\d+)$', 'list_post',),    
    url(r'^hotlist/(?P<page>\d+)$', 'hotlist',name='hotlist'),    
    (r'^xmlrpc/$', 'xmlrpc'),
    url(r'^category/(?P<category>.*)/(?P<page>\d+)$', 'category',name='category'),
    (r'^tag/(?P<key>.*)/(?P<page>\d+)$', 'tag'),    
)


urlpatterns += patterns('',
    (r'^post/', include('blog.urls')),
    (r'^album/', include('upload.urls')),
    (r'^', include('import_wxp.urls')),
)



feeds={
       'latest':LatestEntries,
       'categories':LatestEntriesByCategory,
       'post':PostEntry,
}

urlpatterns += patterns('',
    (r'^feeds/(?P<url>.*)/$', 'django.contrib.syndication.views.feed', {'feed_dict':feeds, }),
)


try:
    urlpatterns += patterns('',
        url(settings.LINK_FORMAT, view_post_perma,name='view_post_perma'),
    )
except:
    urlpatterns += patterns('',
        url(r'^(?P<year>\d+)/(?P<month>\d\d+)/(?P<slug>.*).html$', view_post_perma,name='view_post_perma'),
    )
