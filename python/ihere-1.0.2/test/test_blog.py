# coding=UTF-8
from blog.tests import DjangoBlogTest
#import unittest
#from django.test.client import Client
#from google.appengine.api import users
##from django.test import TestCase
#from blog.models import Post,Comment,Event
#import logging
#
## import the Django Forms replacement provided by the App Engine SDK, 
## so that Django Forms get Data Store model support.
# 
#from google.appengine.ext.db.djangoforms import ModelForm 

#class BlogTest(unittest.TestCase):
#    
#    def setUp(self):
#        
#        # Every test needs a client.
#        self.client = Client()        
#        self.user=users.get_current_user()
#        self.assertTrue(self.user is not None)        
#        self.assertTrue(users.is_current_user_admin())
#        
#        self.data1={
#                             'authorEmail':'myself@appengineguy.com',
#                             'category':'category1',
#                             'title':'title1',
#                             'content':'content1',
#                             'users':users,
#              }
#        
#        self.data2={
#                             'authorEmail':'myself@appengineguy.com',
#                             'category':'category2',
#                             'title':'title2',
#                             'content':'content2',
#                             'users':users,
#              }
#        
#        self.data_add={
#                             'authorEmail':'myself@appengineguy.com',
#                             'category':'category_add',
#                             'title':'title_add',
#                             'content':'content_add',
#                             'users':users,
#                 }
#        
#        self.commentData={
#                         'user':self.user.nickname(),
#                         'authorEmail':self.user.email(),
#                         'authorWebsite':'http://test.com',
#                         'content':'testComment',
#                         'users':users,
#                         
#                         }
#                
#        response = self.client.post('/post/add/',self.data1)
#        response = self.client.post('/post/add/',self.data2)
#        
#
#    def test_add_post(self):
#        notAddedPost = Post.all().filter("title =", self.data_add['title']).get()
#        self.assertTrue(notAddedPost is None)          
#        response = self.client.post('/post/add/',self.data_add)
#        retrPost = Post.all().filter("title =", self.data_add['title']).get()
#        self.assertTrue(retrPost is not None)
#        self.assertTrue(self.data_add['content'], retrPost.content) 
#        self.assertTrue(self.data_add['title'], retrPost.title)
#        self.assertTrue(self.data_add['category'], retrPost.category)
#        self.assertTrue(self.user, retrPost.author)
#        self.assertTrue(self.data_add['authorEmail'], retrPost.authorEmail)
#
#    def test_list_post(self):       
#        response = self.client.get('/')
#
#        # Check that the response is 200 OK.
#        self.failUnlessEqual(response.status_code, 200)
#        # Check that the multi-value data has been rolled out ok
#        self.assertTrue('[%s] %s'%(self.data1['category'],self.data1['title']) in response.content)
#        self.assertTrue('[%s] %s'%(self.data2['category'],self.data2['title']) in response.content)
#        
#    def test_category(self):
#        
#        #response = self.client.post('/accounts/',{})
#        # Issue a GET request.
#        response = self.client.get('/category/%s/1'%self.data1['category'])
#
#        # Check that the response is 200 OK.
#        self.failUnlessEqual(response.status_code, 200)
#        # Check that the multi-value data has been rolled out ok
#        self.assertTrue('[%s] %s'%(self.data1['category'],self.data1['title']) in response.content)
#        self.assertTrue('[%s] %s'%(self.data2['category'],self.data2['title']) not in response.content)
#        
#    def test_view_list_and_comment(self):
#        
#        #response = self.client.post('/accounts/',{})
#        # Issue a GET request.3
#        post = Post.all().filter("title =", self.data1['title']).get()
#        postId=post.key().id()
#        
#        response = self.client.get('/post/%s'%postId)
#
#        # Check that the response is 200 OK.
#        self.failUnlessEqual(response.status_code, 200)
#        # Check that the multi-value data has been rolled out ok
#        self.assertTrue(self.data1['title'] in response.content)      
#       
#        comment = Comment.all().filter("post =", post).get()
#        self.assertTrue(comment is None)
#        
#        self.client.post('/post/%s'%postId, self.commentData)
#        self.failUnlessEqual(response.status_code, 200)        
#        
#        commentResponse = self.client.get('/post/%s'%postId)
#        
#        self.assertTrue(self.commentData['user'] in commentResponse.content) 
#        self.assertTrue(self.commentData['authorEmail'] in commentResponse.content) 
#        self.assertTrue(self.commentData['authorWebsite'] in commentResponse.content) 
#        self.assertTrue(self.commentData['content'] in commentResponse.content) 
#        
#        addedcomment = Comment.all().filter("post =", post).get()
#        self.assertTrue(addedcomment.content==self.commentData['content'])
#        self.assertTrue(addedcomment is not None)
#        
#                        
#    def test_edit_post(self):
#        post = Post.all().filter("title =", self.data1['title']).get()
#        postId=post.key().id()
#        response = self.client.get('/post/edit/%s'%postId)
#        self.failUnlessEqual(response.status_code, 200)
#        self.assertTrue(self.data1['title'] in response.content)  
#        post = Post.all().filter("title =", self.data1['title']).get()
#        self.assertTrue(post is not None)
#        newdata1={
#                         'authorEmail':'newmyself@appengineguy.com',
#                         'category':'newcategory1',
#                         'title':'newtitle1',
#                         'content':'newcontent1',
#                         'users':users,
#             }
#        response = self.client.post('/post/edit/%s'%postId,newdata1)      
#        
#        retrPost = Post.all().filter("title =", newdata1['title']).get()
#        self.assertTrue(newdata1['content'], retrPost.content) 
#        self.assertTrue(newdata1['title'], retrPost.title)
#        self.assertTrue(newdata1['category'], retrPost.category)
#        self.assertTrue(self.user, retrPost.author)
#        self.assertTrue(newdata1['authorEmail'], retrPost.authorEmail)
#        
#    def test_delete_post(self):
#        post = Post.all().filter("title =", self.data1['title']).get()
#        postId=post.key().id()
#        response = self.client.get('/post/%s'%postId)
#        self.failUnlessEqual(response.status_code, 200)
#        self.assertTrue(self.data1['title'] in response.content)  
#        post = Post.all().filter("title =", self.data1['title']).get()
#        self.assertTrue(post is not None)
#        
#        response = self.client.get('/post/delete/%s'%postId)
#        self.failUnlessEqual(response.status_code, 302)
#        self.assertTrue(self.data1['title'] not in response.content)  
#        deletedpost = Post.all().filter("title =", self.data1['title']).get()
#        self.assertTrue(deletedpost is None)
#    
#    def test_view_post_visitcount(self):
#        
#        #response = self.client.post('/accounts/',{})
#        # Issue a GET request.3
#        post = Post.all().filter("title =", self.data1['title']).get()
#        postId=post.key().id()
#        postBeforeCount=post.visitcount
#        
#        response = self.client.get('/post/%s'%postId)
#
#        # Check that the response is 200 OK.
#        self.failUnlessEqual(response.status_code, 200)
#        # Check that the multi-value data has been rolled out ok
#        returnPost = Post.all().filter("title =", self.data1['title']).get()       
#        self.assertTrue(postBeforeCount+1==returnPost.visitcount)
#
#    def test_post_save_signals(self):
#        notAddedPost = Post.all().filter("title =", self.data_add['title']).get()
#        self.assertTrue(notAddedPost is None)          
#        response = self.client.post('/post/add/',self.data_add)
#        retrPost = Post.all().filter("title =", self.data_add['title']).get()
#        self.assertTrue(retrPost is not None)
#        event=Event.all().filter("event =", u"""新文章: %s"""%self.data_add['title']).get()
#        self.assertTrue(event is not None)
#        self.assertTrue(event.event==u"""新文章: %s"""%self.data_add['title'])
#        
#    def test_pre_save_signals(self):
#        notAddedPost = Post.all().filter("title =", self.data_add['title']).get()
#        self.assertTrue(notAddedPost is None)          
#        response = self.client.post('/post/add/',self.data_add)
#        retrPost = Post.all().filter("title =", self.data_add['title']).get()
#        self.assertTrue(retrPost is not None)
#        event=Event.all().filter("event =", "pre"+u"""新文章: %s"""%self.data_add['title']).get()
#        self.assertTrue(event is not None)
#        self.assertTrue(event.event=="pre"+u"""新文章: %s"""%self.data_add['title'])
#        
#    def test_commentDeletedWithPost(self):    
#
#        self.test_view_list_and_comment()
#        post = Post.all().filter("title =", self.data1['title']).get()
#        commentId=post.comment_set.get().key().id()
#        post.delete()
#        
#        self.assertTrue(Comment.get_by_id(commentId) is None)
#        
#    
#    def tearDown(self):
#        for post in Post.all():
#            post.delete()
#        for comment in Comment.all():
#            comment.delete()
        