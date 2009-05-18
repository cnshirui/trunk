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
#from django.test import TestCase
from appenginepatcher import on_production_server

from google.appengine.ext.db.djangoforms import ModelForm
import unittest
from django.test.client import Client
from google.appengine.api import users
import logging 
from models import *
if not on_production_server:
    from google.appengine.api.images import images_stub

ROOT_PATH = os.path.dirname(__file__)
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

        # Use a fresh images stub.
        
        if not on_production_server:
            apiproxy_stub_map.apiproxy.RegisterStub('images', images_stub.ImagesServiceStub())



        # Use a fresh mail stub.
#        apiproxy_stub_map.apiproxy.RegisterStub('mail', mail_stub.MailServiceStub()) 

        # Every test needs a client.
        self.client = Client()        

    def test_add_file(self):
        file=open(ROOT_PATH+'/testtxt.txt')
        
        response = self.client.post('/album/', {'file': file})
        file.close()
        self.failUnlessEqual(response.status_code, 302)
        
        returnResponse = self.client.get('/album/')
        self.assertTrue('testtxt.txt' in returnResponse.content)
        userFile=UserFile.all().filter("name =","testtxt.txt").get()
        self.assertTrue(userFile is not None)
    
    def test_download_file(self):    
        self.test_add_file()
        userFile=UserFile.all().filter("name =","testtxt.txt").get()
        bin=userFile.filebin_set.get()
        returnResponse = self.client.get('/album/download/%s'%userFile.key())
        self.assertTrue(returnResponse['Content-Type']=='application/octet-stream')
        self.assertTrue(returnResponse.content==bin.bin)
        
    def test_show_picture(self):    
        file=open(ROOT_PATH+'/daodao2.JPG')
        
        response = self.client.post('/album/', {'file': file})
        file.close()
        self.failUnlessEqual(response.status_code, 302)
        
        returnResponse = self.client.get('/album/')
        self.assertTrue('daodao2.JPG' in returnResponse.content)
        userFile=UserFile.all().filter("name =","daodao2.JPG").get()
        self.assertTrue(userFile is not None)
        
        self.test_add_file()
        userFile=UserFile.all().filter("name =","daodao2.JPG").get()
        bin=userFile.filebin_set.get()
        returnResponse = self.client.get('/album/%s'%userFile.key())
        self.assertTrue(returnResponse['Content-Type']=='image/JPEG')
        self.assertTrue(returnResponse.content==bin.bin)
        
    def test_delete_file(self):
        self.test_add_file()
        userFile=UserFile.all().filter("name =","testtxt.txt").get()
        fileBinKey=userFile.filebin_set.get().key()
        response = self.client.get('/album/delete/%s'%userFile.key())      
        self.assertTrue(UserFile.all().filter("name =",'testtxt.txt').get() is None)
        self.assertTrue(FileBin.get(fileBinKey) is None)
        returnResponse = self.client.get('/album/')
        self.assertTrue('testtxt.txt' not in returnResponse.content)

    def tearDown(self):
        #For that we are using a temporary datastore stub located in the memory,we don't have to clean up.
        pass
        