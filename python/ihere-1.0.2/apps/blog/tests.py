# coding=UTF-8
import unittest,os,urllib,logging
from django.test.client import Client
#import the stubs, i.e. the fake datastore, user and mail service and urlfetch
from google.appengine.api import apiproxy_stub_map
from google.appengine.api import datastore_file_stub
#from google.appengine.api import mail_stub
from google.appengine.api import urlfetch_stub
from google.appengine.api.memcache import memcache_service_pb
from google.appengine.api.memcache import memcache_stub
from google.appengine.api import user_service_stub
from django.utils.encoding import force_unicode,smart_str
from google.appengine.api import users
#from django.test import TestCase
from views import PostForm
from models import *
from appenginepatcher import on_production_server
if not on_production_server:
    from google.appengine.api.images import images_stub
#from test import test_blog

# import the Django Forms replacement provided by the App Engine SDK, 
# so that Django Forms get Data Store model support.
 
from google.appengine.ext.db.djangoforms import ModelForm 
class DjangoBlogTest(unittest.TestCase):
    
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
        os.environ['USER_IS_ADMIN'] = '1' 
        os.environ['APPLICATION_ID'] = 'ihere' 
        #os.environ.get('USER_IS_ADMIN', '0') == '1'
        # Use a fresh urlfetch stub.
        apiproxy_stub_map.apiproxy.RegisterStub('urlfetch', urlfetch_stub.URLFetchServiceStub())

        # Use a fresh mail stub.
#        apiproxy_stub_map.apiproxy.RegisterStub('mail', mail_stub.MailServiceStub()) 
        # Use a fresh images stub.
        if not on_production_server:
            apiproxy_stub_map.apiproxy.RegisterStub('images', images_stub.ImagesServiceStub())


        # Every test needs a client.
        self.client = Client()        
        self.user=users.get_current_user()
        self.category1=Category(key_name='category_category1',name='category1',slug='category1').put()
        self.category2=Category(key_name='category_category2',name='category2',slug='category2').put()
        self.category_add=Category(key_name='category_category_add',name='category_add',slug='category_add').put()
        key_name='post_0000000001'
        self.data1={    
                             'key_name':'post_0000000001',
                             'authorEmail':'myself@appengineguy.com',
                             'category':self.category1,
                             'title':'title1',
                             'content':'content1',
                             'users':users,
                             'slug':'title1-test.html',
                             'entryType':'post',
                             'tag':'tag1,tag2',
                             'isPublished':True,
              }
        
        self.data2={
                             'key_name':'post_0000000002',
                             'authorEmail':'myself@appengineguy.com',
                             'category':self.category2,
                             'title':'title2',
                             'content':'content2',
                             'users':users,
                             'slug':'title2-test.html',
                             'entryType':'post',
                             'tag':'tag1,tag2',
                             'isPublished':True,
              }
        
        self.data_add={
                             'key_name':'post_0000000003',
                             'authorEmail':'myself@appengineguy.com',
                             'category':self.category_add,
                             'title':'title_add',
                             'content':'content_add',
                             'users':users,
                             'slug':'title-add-test.html',
                             'entryType':'post',
                             'tag':'tag1,tag2',
                             'isPublished':True,
                 }
        
        self.commentData={
                         'user':self.user.nickname(),
                         'authorEmail':self.user.email(),
                         'authorWebsite':'http://test.com',
                         'content':'testComment',
                         'users':users,
                         
                         }
        
        self.assertTrue(self.user is not None)        
        self.assertTrue(users.is_current_user_admin())
                
        response = self.client.post('/post/add/',self.data1)
        response = self.client.post('/post/add/',self.data2)
        
    def test_add_post(self):
        notAddedPost = Post.all().filter("title =", self.data_add['title']).get()
        self.assertTrue(notAddedPost is None)          
        response = self.client.post('/post/add/',self.data_add)
        retrPost = Post.all().filter("title =", self.data_add['title']).get()
        self.assertTrue(retrPost is not None)
        self.assertTrue(self.data_add['content'], retrPost.content) 
        self.assertTrue(self.data_add['title'], retrPost.title)
        self.assertTrue(self.data_add['category'], retrPost.category.key())
        self.assertTrue(self.user, retrPost.author)
        self.assertTrue(self.data_add['authorEmail'], retrPost.authorEmail)

    def test_list_post(self):       
        response = self.client.get('/')

        # Check that the response is 200 OK.
        self.failUnlessEqual(response.status_code, 200)
        # Check that the multi-value data has been rolled out ok
        self.assertTrue('%s'%(self.data1['title']) in response.content)
        self.assertTrue('%s'%(self.data2['title']) in response.content)
        
    def test_category(self):
        self.test_add_post()
        #response = self.client.post('/accounts/',{})
        # Issue a GET request.
        response = self.client.get('/category/category1/1')

        # Check that the response is 200 OK.    
        self.failUnlessEqual(response.status_code, 200)
        # Check that the multi-value data has been rolled out ok
        self.assertTrue('%s'%('category1') in response.content)
#        self.assertTrue('%s'%('category2') not in response.content)
        
    def test_view_list_and_comment(self):
        
        #response = self.client.post('/accounts/',{})
        # Issue a GET request.3
        post = Post.all().filter("title =", self.data1['title']).get()
        postId=post.key()
        
        response = self.client.get('/post/%s'%postId)

        # Check that the response is 200 OK.
        self.failUnlessEqual(response.status_code, 200)
        # Check that the multi-value data has been rolled out ok
        self.assertTrue(self.data1['title'] in response.content)      
       
        comment = Comment.all().filter("post =", post).get()
        self.assertTrue(comment is None)
        
        self.client.post('/post/%s'%postId, self.commentData)
        self.failUnlessEqual(response.status_code, 200)        
        
        commentResponse = self.client.get('/post/%s'%postId)
        
        self.assertTrue(self.commentData['user'] in commentResponse.content) 
        self.assertTrue(self.commentData['authorEmail'] in commentResponse.content) 
        self.assertTrue(self.commentData['authorWebsite'] in commentResponse.content) 
        self.assertTrue(self.commentData['content'] in commentResponse.content) 
        
        addedcomment = Comment.all().filter("post =", post).get()
        self.assertTrue(addedcomment.content==self.commentData['content'])
        self.assertTrue(addedcomment is not None)
        return addedcomment.key()
                        
    def test_edit_post(self):
        post = Post.all().filter("title =", self.data1['title']).get()
        postId=post.key()
        response = self.client.get('/post/edit/%s'%postId)
        self.failUnlessEqual(response.status_code, 200)
        self.assertTrue(self.data1['title'] in response.content)  
        post = Post.all().filter("title =", self.data1['title']).get()
        self.assertTrue(post is not None)
        category_newdata1=Category(name='category_newdata1',slug='category_newdata1').put()
        newdata1={
                         'key_name':'post_0000000004',
                         'authorEmail':'newmyself@appengineguy.com',
                         'category':category_newdata1,
                         'title':'newtitle1',
                         'content':'newcontent1',
                         'users':users,
                         'entryType':'post',
                         'tag':'tag1,tag2',
                         'isPublished':True,
             }
        response = self.client.post('/post/edit/%s'%postId,newdata1)      
        
        retrPost = Post.all().filter("title =", newdata1['title']).get()
        self.assertTrue(newdata1['content'], retrPost.content) 
        self.assertTrue(newdata1['title'], retrPost.title)
        self.assertTrue(newdata1['category'], retrPost.category.key())
        self.assertTrue(self.user, retrPost.author)
        self.assertTrue(newdata1['authorEmail'], retrPost.authorEmail)
        
    def test_delete_post(self):
        post = Post.all().filter("title =", self.data1['title']).get()
        postId=post.key()
        response = self.client.get('/post/%s'%postId)
        self.failUnlessEqual(response.status_code, 200)
        self.assertTrue(self.data1['title'] in response.content)  
        post = Post.all().filter("title =", self.data1['title']).get()
        self.assertTrue(post is not None)
        
        response = self.client.get('/post/delete/%s'%postId)
        self.failUnlessEqual(response.status_code, 302)
        self.assertTrue(self.data1['title'] not in response.content)  
        deletedpost = Post.all().filter("title =", self.data1['title']).get()
        self.assertTrue(deletedpost is None)
    
#    def test_view_post_visitcount(self):
#        
#        #response = self.client.post('/accounts/',{})
#        # Issue a GET request.3
#        post = Post.all().filter("title =", self.data1['title']).get()
#        postId=post.key()
#        postBeforeCount=post.visitcount
#        
#        response = self.client.get('/post/%s'%postId)
#
#        # Check that the response is 200 OK.
#        self.failUnlessEqual(response.status_code, 200)
#        # Check that the multi-value data has been rolled out ok
#        returnPost = Post.all().filter("title =", self.data1['title']).get()       
#        self.assertTrue(postBeforeCount+1==returnPost.visitcount)

#    def test_post_save_signals(self):
#        notAddedPost = Post.all().filter("title =", self.data_add['title']).get()
#        self.assertTrue(notAddedPost is None)          
#        response = self.client.post('/post/add/',self.data_add)
#        retrPost = Post.all().filter("title =", self.data_add['title']).get()
#        self.assertTrue(retrPost is not None)
#        event=Event.all().filter("event =", retrPost).get()
#        self.assertTrue(event is not None)
#        self.assertTrue(event.event==retrPost)
#        eventResponse = self.client.get('/event/')
#        self.failUnlessEqual(eventResponse.status_code, 200)
##        logging.info('test_post_save_signals:'+unicode(retrPost))
##        str='%s adds a post: [%s] at %s'%(retrPost.author,retrPost.title,retrPost.date)
#        self.assertTrue(smart_str(retrPost.date.strftime('%A, %d. %B %Y %I:%M%p')) in eventResponse.content) 
#        self.assertTrue(smart_str(retrPost.title) in eventResponse.content)
#        self.assertTrue(smart_str(retrPost.author) in eventResponse.content) 
#        
#    def test_pre_delete_signals(self):
#        self.test_delete_post()
#        eventResponse = self.client.get('/event/')
#        self.assertTrue(smart_str(self.data1['title']) not in eventResponse.content)
        
    def test_comment_delete(self):
        commentId=self.test_view_list_and_comment()         
        response = self.client.get('/post/deleteComment/%s'%commentId)
        self.failUnlessEqual(response.status_code, 302)
        self.assertTrue(self.commentData['content'] not in response.content)
        
    def test_commentDeletedWithPost(self):    

        self.test_view_list_and_comment()
        post = Post.all().filter("title =", self.data1['title']).get()
        commentId=post.comment_set.get().key()
        post.delete()
        
        self.assertTrue(Comment.get(commentId) is None)
        
    
    def tearDown(self):
        pass
        
