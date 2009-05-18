# coding=UTF-8
from django.conf.urls.defaults import *
from django.views.generic.simple import redirect_to, direct_to_template
from models import *
from views import FileForm
FILE_MAX=100

urlpatterns = patterns('upload.views',
    (r'^$','albumentry'),
    url(r'^(?P<key>\w+)$','show_albumentry',name="show_albumentry"),
    (r'^download/(?P<key>\w+)$','download_albumentry'),
    (r'^delete/(?P<key>\w+)$','delete_albumentry'),
    (r'^share/(?P<key>\w+)$','share'),
    (r'^OuterHeaven/$','outer_heaven'),
    url(r'^icon/(?P<key>\w+)/$','show_icon',name="show_icon"),
    (r'^PikaChoose/$',direct_to_template, {'template': 'upload/PikaChoose.html','extra_context':{'filelist':UserFile.all().order('-creationDate'),'form':FileForm(),},}),
    (r'^mbGallery/$',direct_to_template, {'template': 'upload/mbGallery.html','extra_context':{'filelist':UserFile.all().order('-creationDate'),'form':FileForm(),},}),

)




