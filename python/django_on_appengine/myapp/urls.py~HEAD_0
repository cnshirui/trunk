from django.conf.urls.defaults import *

urlpatterns = patterns('',
    (r'^$', 'myapp.poll.views.index'),
    (r'^create/$', 'myapp.poll.views.create'),
    (r'^poll/(?P<poll_key>[^\.^/]+)/$', 'myapp.poll.views.poll_detail'),
    (r'^poll/(?P<poll_key>[^\.^/]+)/results/$', 'myapp.poll.views.poll_results'),
    )
