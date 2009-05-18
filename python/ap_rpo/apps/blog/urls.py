from django.conf.urls.defaults import *

urlpatterns = patterns('blog.views',    
    
    (r'^add/$', 'add_post'),
#    (r'^xmlrpc/$', 'xmlrpc'),
    (r'^edit/(?P<key>.*)', 'edit_post'),
    (r'^delete/(?P<key>.*)', 'delete_post'),    
    (r'^deleteComment/(?P<key>.*)$','delete_comment'),
    (r'^(?P<key>.*)', 'view_post',),
)
