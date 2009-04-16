# coding=UTF-8
from google.appengine.ext.db import djangoforms as forms
from google.appengine.ext import db
from models import *
from django.shortcuts import render_to_response
from django.template import RequestContext
from google.appengine.api import users
from django.http import HttpResponseRedirect,Http404, HttpResponseForbidden,HttpResponse,HttpResponseNotFound
from django.core.paginator import Paginator, InvalidPage
from django.views.decorators.vary import vary_on_headers
from django.views.decorators.cache import cache_control,cache_page
from django.utils.encoding import force_unicode,smart_str
from google.appengine.api import memcache
from django.core.urlresolvers import reverse
from django.views.generic import list_detail
from ragendja.dbutils import get_object_or_404
import datetime,logging,time
from utility.authorized import admin_required,login_required
import urllib
from utility import permalink
from utility.permalink import slug_service,call_slug_service_demo
from django import forms as djangoforms
import django.utils.simplejson
from utility.xmlrpc import view
from counter import Counter
from django.utils.cache import get_cache_key

class PostForm(forms.ModelForm):
    tag = djangoforms.CharField(required=False)
    key_name=djangoforms.CharField(widget=djangoforms.HiddenInput(),required=False)
    class Meta:
        model = Post          
        exclude = ['date','tags','author','lastCommentedDate', 'commentcount','visitcount', 'lastModifiedDate', 'lastModifiedBy', ]

class CommentForm(forms.ModelForm):
    class Meta:
        model = Comment
        exclude = ['post', 'date', 'author', 'userIp', 'lastModifiedDate', 'lastModifiedBy', ]


@admin_required
def add_post(request):
    user=users.get_current_user()
    if request.method == 'GET':
        timestamp=str(time.time())[:10]
        key_name='post_'+timestamp
        form = PostForm({'authorEmail':str(user.email()),'key_name':key_name,})
        draft=Post.all().filter('isPublished =',False).order('-date')
        template_values={
                         'form': form,
                         'draft':draft,
                         }
        return render_to_response('blog/post_form.html', template_values, context_instance=RequestContext(request),)
        
    else:
        form = PostForm(request.POST)
        if form.is_valid():
            post = form.save(commit=False)
            if not post.slug:
                try:
                    post.slug=permalink.get_slug(post.title)
                except:
                    pass
            key_name=post.key().id_or_name()      
            tagstrings=form.cleaned_data['tag'].strip()
            post.tags=set_tags(post, tagstrings)
            post.author =user
            post.authorEmail =post.authorEmail
            post.put()
            #flush cache, for the new post appears in the main page            
            memcache.flush_all()
            return HttpResponseRedirect(post.get_permalink())
    
    

@cache_control(must_revalidate=True)#cache_control 告诉缓存对每次访问都重新验证缓存并在最长 3600 秒内保存所缓存版本
@vary_on_headers('User-Agent','Cookie')
def view_post(request, key):
    key=force_unicode(urllib.unquote(smart_str(key))) 
    post= get_object_or_404(Post,key)
    comments = post.comment_set
    user = users.get_current_user()
    if request.method == 'GET':
        if user:
            form = CommentForm({'user':str(user.nickname()), 'authorEmail':str(user.email()),'authorWebsite':'http://'})
        else:
            form = CommentForm()
        if not post.slug:
            #adapt old style url pages
            try:
                post.slug=permalink.get_slug(post.title)
                post.put()
            except :
                memcache.flush_all()
                return HttpResponseRedirect(reverse(list_post))
          
        hits = Counter(str(key))
        hits.increment()
#        post.visitcount+=1
#        post.put()
        template_values = {
                  'post' : post,
                  'comments': comments,                  
                  'form' : form,
        }
        return render_to_response('blog/post_detail.html', template_values,context_instance=RequestContext(request),)
       
    else :
        return add_comment(request,key)
        
    

@cache_control(must_revalidate=True)#cache_control 告诉缓存对每次访问都重新验证缓存并在最长 3600 秒内保存所缓存版本
@vary_on_headers('User-Agent','Cookie')
def view_post_perma(request,*args,**kargs):    
    slug=kargs['slug']  
    slug=force_unicode(urllib.unquote(smart_str(slug))) 
    post = Post.all().filter("slug =", slug).get()
    if post:
        return view_post(request,str(post.key()))
    else:
        memcache.flush_all()        
#        return HttpResponseRedirect(reverse(list_post))
        raise Http404('Object does not exist!')

def set_tags(post,tagstrings):    
    tags=tagstrings.split(',')
    if tags is None:
        post.tags = []
    else:
        post.tags = [Tag.get_or_insert(key_name='tag_'+name.strip(),parent=None,name=name.strip(),slug=name.strip()).key() for name in tags]
    return post.tags

@admin_required
def edit_post(request, key):
    key=force_unicode(urllib.unquote(smart_str(key))) 
    user=users.get_current_user()
    post=get_object_or_404(Post,key)
    
    if post.author==users.get_current_user() or users.is_current_user_admin():
        if request.method == 'POST':
            form = PostForm(request.POST)
            if form.is_valid():
                formPost=form.save(commit=False)
                post.title=formPost.title
                post.content=formPost.content
                post.authorEmail=formPost.authorEmail
                post.entryType=formPost.entryType
                post.category=formPost.category                    
                post.isPublished=formPost.isPublished
                post.slug=formPost.slug
                post.lastModifiedBy=user
                post.lastModifiedDate=datetime.datetime.now()
                tagstrings=form.cleaned_data['tag'].strip()  
                post.tags=set_tags(post, tagstrings)
                post.put()
                    #make the edited post show up
                memcache.flush_all()
                return HttpResponseRedirect(post.get_permalink())
        else:
            if post.category:
                category_key=post.category.key()
            else:
                category_key=None
                
            data={
                      'tag':post.get_tags_string,
#                      'tag':','.join([tag.name for tag in db.get(post.tags)]),
                      'title':post.title,
                      'content':post.content,
                      'authorEmail':post.authorEmail,
                      'entryType':post.entryType,
                      'category':category_key,
                      'slug':post.slug,
                      'isPublished':post.isPublished,
                 }
            form=PostForm(data)
            draft=Post.all().filter('isPublished =',False).order('-date')
            template_values={
                                 'form': form, 
                                 'draft':draft, 
                            }
            return render_to_response('blog/post_form.html', template_values,context_instance=RequestContext(request),)
    
@login_required
def add_comment(request,post_key):   
    user = users.get_current_user()
    post= get_object_or_404(Post,post_key) 
    form = CommentForm(request.POST)
    if form.is_valid():
        comment = form.save(commit=False)
        timestamp=str(time.time())[:10]
        key_name='comment_'+timestamp
                
        if 'REMOTE_ADDR' in request.META:
            comment.userIp = request.META['REMOTE_ADDR']
        if user:
            comment.author = user
            comment.authorEmail = str(user.email())
            comment.user = str(user.nickname())
        authorWebsite = comment.authorWebsite.strip()   
        if authorWebsite=='http://':
            authorWebsite=None
        new_comment=Comment(
                                    key_name=key_name,
                                    post = post,
                                    user = comment.user,
                                    author = comment.author,
                                    authorEmail = comment.authorEmail,
                                    authorWebsite = authorWebsite,
                                    userIp = comment.userIp,
                                    content = comment.content,
                                    parent=post,
                            )
        new_comment.put()
        #flush cache, for that the new comment appears in the post page            
        memcache.flush_all()
        return HttpResponseRedirect(post.get_permalink()+'#comments')
    
@admin_required
def delete_post(request, key):
    key=force_unicode(urllib.unquote(smart_str(key))) 
    post=get_object_or_404(Post,key)
    post.delete()
    #flush cache in order to update the post list
    memcache.flush_all()
    return HttpResponseRedirect(reverse(list_post))
    

@cache_control(must_revalidate=True,max_age=3600)#cache_control 告诉缓存对每次访问都重新验证缓存并在最长 3600 秒内保存所缓存版本
@vary_on_headers('User-Agent','Cookie')
def list_post(request, page=1,):
    page =int(page)
    object_list=Post.all().filter('isPublished =',True).order('-date')  
    if request.is_ajax():
        offset=int(request.GET['p'])+page
        logging.info(offset)
#        return render_to_response('blog/post_list_pageflow_ajax_response.html', template_values, context_instance=RequestContext(request),)
        return list_detail.object_list(
                                        request,
                                        object_list,  
                                        allow_empty= True,  
                                        paginate_by=5,
                                        page=offset,
                                        template_name='blog/post_list_pageflow_ajax_response.html',
                                        template_object_name='post',
                                        extra_context={'offset':(offset-1)*5,},
                                   )
    
    return list_detail.object_list(
                                        request,
                                        object_list,  
                                        allow_empty= True,  
                                        paginate_by=5,
                                        page=page,
                                        template_name='blog/post_list_pageflow.html',
                                        template_object_name='post',
                                        
                                   )

@cache_control(must_revalidate=True,max_age=3600)#cache_control 告诉缓存对每次访问都重新验证缓存并在最长 3600 秒内保存所缓存版本
@vary_on_headers('User-Agent','Cookie')
def hotlist(request, page=1,):
    page =int(page)
    object_list=Post.all().filter('isPublished =',True).order('-visitcount')
    return list_detail.object_list(
                                        request,
                                        object_list,  
                                        allow_empty= True,  
                                        paginate_by=20,
                                        page=page,
                                        template_name='blog/post_common_list.html',
                                        template_object_name='post',
                                        
                                   )


@cache_control(must_revalidate=True,max_age=3600)
@vary_on_headers('User-Agent','Cookie')
def category(request,category,page=1):
    page =int(page)
    category = force_unicode(urllib.unquote(smart_str(category))) 
#    searchCategory=Category.all().filter('name =',category).get()
    searchCategory=Category.get_by_key_name('category_'+category)
    if not searchCategory:
        searchCategory=Category.all().filter('name =',category).get()
    elif not searchCategory:
        return HttpResponseNotFound()
    object_list=searchCategory.post_set.filter('isPublished =',True).order('-date')
    return list_detail.object_list(
                                        request,
                                        object_list,  
                                        allow_empty= True,  
                                        paginate_by=20,
                                        page=page,
                                        template_name='blog/post_common_list.html',
                                        template_object_name='post',
                                   )

@login_required
def delete_comment(request,key):
    comment=get_object_or_404(Comment,key)
    if users.get_current_user()==comment.author or users.is_current_user_admin():
        post=comment.post
        comment.delete()
        #flush cache in order to update the comment list
        memcache.flush_all()                
        return HttpResponseRedirect(post.get_permalink()+'#comments')
    else:
        return HttpResponseForbidden()
    
@cache_control(must_revalidate=True,max_age=3600)#cache_control 告诉缓存对每次访问都重新验证缓存并在最长 3600 秒内保存所缓存版本
@vary_on_headers('User-Agent','Cookie')
def tag(request,key,page=1):
    page =int(page)
    tag=get_object_or_404(Tag,key)
    return list_detail.object_list(
                                        request,
                                        tag.members,  
                                        allow_empty= True,  
                                        paginate_by=20,
                                        page=page,
                                        template_name='blog/post_common_list.html',
                                        template_object_name='post',
                                        extra_context={'tags':Tag.all(),'categories':Category.all()},
                                   )
    
def xmlrpc(request):
    return view(request, 'blog.metaweblog')
