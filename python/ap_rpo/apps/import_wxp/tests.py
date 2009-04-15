# coding=UTF-8
import unittest,os,urllib
from django.test.client import Client
#import the stubs, i.e. the fake datastore, user and mail service and urlfetch
from google.appengine.api import apiproxy_stub_map
from google.appengine.api import datastore_file_stub
#from google.appengine.api import mail_stub
from google.appengine.api import urlfetch_stub
from google.appengine.api import user_service_stub
from google.appengine.api.memcache import memcache_stub
from django.utils.encoding import force_unicode,smart_str
from google.appengine.api import users
from django.test import TestCase

from google.appengine.ext.db.djangoforms import ModelForm
import unittest
from django.test.client import Client
from google.appengine.api import users
import logging 
from blog.models import *
from parseWp import Importer,Import
from google.appengine.ext import db
from blog.models import Post, Comment, Event,Tag,Category

#How I do gae test:-)
#1. 
#using gaeunit: http://code.google.com/p/gaeunit/
#and need to fix a bug of Django : Ticket #5176 
#http://code.djangoproject.com/ticket/5176

#2.
#using appengine_django_helper:
#http://code.google.com/intl/zh-CN/appengine/articles/appengine_helper_for_django.html
#http://appengineguy.com/2008/06/proper-unit-testing-of-app-enginedjango.html [GFW blocked]
#run from cmd: manage.py test upload
#Here i use the 2nd way:P
#enjoy 
class FileTest(unittest.TestCase):
    
    def setUp(self):        
        # Start with a fresh api proxy.
        apiproxy_stub_map.apiproxy = apiproxy_stub_map.APIProxyStubMap()

        # Use a fresh stub datastore.
        # From this point on in the tests, all calls to the Data Store, such as get and put,
        # will be to the temporary, in-memory datastore stub.         
        stub = datastore_file_stub.DatastoreFileStub(u'myTemporaryDataStorage', '/dev/null', '/dev/null')
        apiproxy_stub_map.apiproxy.RegisterStub('datastore_v3', stub)

        #User a memcache stub
        apiproxy_stub_map.apiproxy.RegisterStub('memcache', memcache_stub.MemcacheServiceStub())

        # Use a fresh stub UserService.
        apiproxy_stub_map.apiproxy.RegisterStub('user', user_service_stub.UserServiceStub())
        os.environ['AUTH_DOMAIN'] = 'gmail.com'
        os.environ['USER_EMAIL'] = 'myself@appengineguy.com' # set to '' for no logged in user 
        os.environ['SERVER_NAME'] = 'testserver' 
        os.environ['SERVER_PORT'] = '80' 
        os.environ['USER_IS_ADMIN'] = '1' #admin user 0 | 1
        os.environ['APPLICATION_ID'] = 'appId' 
        
        # Use a fresh urlfetch stub.
        apiproxy_stub_map.apiproxy.RegisterStub('urlfetch', urlfetch_stub.URLFetchServiceStub())

        # Use a fresh mail stub.
#        apiproxy_stub_map.apiproxy.RegisterStub('mail', mail_stub.MailServiceStub()) 

        # Every test needs a client.
        self.client = Client()        

    def test_import_wxpfile(self):
        wxpfile=open('import_wxp/wordpress_test.xml')     
        importer=Importer(wxpfile)
        importer.import2Gae()
        items=importer.items
        wxpfile.close()
#        self.assertTrue(self.check_categories(items))
        self.assertTrue(self.check_tags(items))
        self.assertTrue(self.check_entries(items))
        
    def check_categories(self,items):
               
        for item in items:
            for category_dict in item['categories']:
                category= db.GqlQuery("select * from Category where name = :1 and slug = :2 ",name=category_dict['name'],slug=category_dict['nicename'])
                if not category:
                    return False
#                logging.info('name:%s'%category_dict['name'])
#                logging.info('nicename:%s'%category_dict['nicename'])

        return True
    
    def check_tags(self,items):
        for item in items:
            for tag in item['tags']:
                tag= db.GqlQuery("select * from Tag where name = :1 and slug = :2 ",name=tag,slug=tag)
                if not tag:
                    return False
        
        return True
    
    def check_entries(self,items):
        
        for item in items:
            post= db.GqlQuery(
                                 "select * from Post where title = :1 and content = :2 and slug = :3",
                                 title=item['title'],
                                 content=item['body'],
                                 slug=item['slug'],
                             )

#            logging.info('slug:%s'%item['slug'])
#            logging.info('link:%s'%item['link'])
            if not post:
                return False
        
        return True
   
#    def test_entity_group(self):
#        
#        category=Category(
#                                  name='testcate',
#                                  slug='testcate',
#                                  key_name='testcate',
#                                  )
#        db.put(category)
#        print category
#        post=Post(
#                              title ='title',
#                              content ='body',
#        #                      date=datetime.datetime.strptime('post_date_gmt','%Y-%m-%d %H:%M:%S',),
#                              author =users.get_current_user(),
#                              authorEmail =users.get_current_user().email(),
#                              slug ='slug',
#                              category=category,
#                              parent=category,
#                              key_name='title',
#                          )
#        db.put(post)
#        print post
#        key=db.Key.from_path('Category','testcate','Post','title')
#        print db.get(key) 
#        
#        comment=Comment(
#                                        post = post,
#                                        user = 'author',
#        #                                date = datetime.datetime.strptime('date','%Y-%m-%d %H:%M:%S',),
#        #                                author = users.User('email'),
#                                        authorEmail = 'email',
#                                        authorWebsite = 'http://weburl.com',
#                                        userIp = '9.123.123.123',
#                                        content = 'content',
#                                        parent=post,
#                                        key_name='content',
#                                )
#        db.put(comment)
#        
#        print comment
#        print comment.parent().kind()
#        print comment.parent().parent().kind()
#        keyname='content'
#        s = Comment.get_or_insert(keyname,parent=post, content="The Three Little Pigs",authorEmail = 'email',user = 'author',)
#        print s
#        print db.get(s.key())
#        print Comment.get_by_key_name('content',parent=post)
#        print db.get(db.Key.from_path(
#            'Category',
#            'testcate',
#            'Post',
#            'title',
#            'Comment',
#            'content',
#            ))
#        query=Comment.all()
#        #query.filter('content =', 'content').order('-date').ancestor(post).get()
#        test =Comment.all().ancestor(post).get()
#        print test.content
#        print category.post_set.get()
#        print post.comment_set.get()

    def tearDown(self):
        #For that we are using a temporary datastore stub located in the memory,we don't have to clean up.
        pass
        