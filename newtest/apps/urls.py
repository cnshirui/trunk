from django.conf.urls.defaults import *

urlpatterns = patterns('',
    # Example:
    # (r'^testit/', include('newtest.apps.foo.urls.foo')),
    (r'^$', 'apps.helloworld.index'),
    (r'^add/$', 'apps.add.index'),
    (r'^list/$', 'apps.list.index'),

    # Uncomment this for admin:
#     (r'^admin/', include('django.contrib.admin.urls')),
)