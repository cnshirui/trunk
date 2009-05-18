# Create your views here.
from google.appengine.ext.db import djangoforms as forms
from django.contrib.sites.models import Site
from google.appengine.ext import db
from blog.models import Post, Comment, Event,Category,Tag
from django.shortcuts import render_to_response
from google.appengine.api import users
from django.http import HttpResponseRedirect, HttpResponseForbidden,HttpResponse,HttpResponseNotFound
from django.views.decorators.vary import vary_on_headers
from django.utils.encoding import force_unicode,smart_str
from google.appengine.api import memcache
from django.core.urlresolvers import reverse
from django.views.generic import list_detail
from utility.authorized import admin_required,login_required
from blog.views import list_post    
from django.template import RequestContext

@admin_required
def import_wxp(request):
    if request.method == 'POST':
        wxpfile=request.FILES['file']
        try:
            from import_wxp.parseWp import Import,Importer
        except:
            return HttpResponseForbidden()
        importer=Importer(wxpfile)
        importer.import2Gae()
        memcache.flush_all()
        return HttpResponseRedirect(reverse(list_post))
        
    else:
        template_values={'users':users,}
        return render_to_response('import_wxp/import.html',template_values,context_instance=RequestContext(request))
    

@admin_required
def export_wxp(request):
    base_url='http://%s' % (Site.objects.get_current().domain)    
    categories=Category.all()
    tags=Tag.all()    
    posts = Post.all().order('-date')
    items=[]
    for post in posts:
        item={}
        item['post']=post
        comments=post.comment_set.order('-date')
        item['comments']=comments
        item['tags']=db.get(post.tags)
        item['category']=post.category.name
        items.append(item)
        
    response=render_to_response('import_wxp/wordpress.xml',locals(),context_instance=RequestContext(request))
    filename='wordpress.xml'
    response['Content-Disposition'] = 'attachment; filename="%s"'%filename
    response['Content-Type'] = 'application/octet-stream'
    return response