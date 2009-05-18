from django.conf.urls.defaults import *

urlpatterns = patterns('',
    (r'^$', 'pollango.poll.views.index'),
    (r'^create/$', 'pollango.poll.views.create'),
    (r'^poll/(?P<poll_key>[^\.^/]+)/$', 'pollango.poll.views.poll_detail'),
    (r'^poll/(?P<poll_key>[^\.^/]+)/results/$', 'pollango.poll.views.poll_results'),
    )
