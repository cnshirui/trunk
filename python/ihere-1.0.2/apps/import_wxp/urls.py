from django.conf.urls.defaults import *

urlpatterns = patterns('import_wxp.views',    
    (r'^import/$', 'import_wxp'),
    (r'^export/$', 'export_wxp'),
)
