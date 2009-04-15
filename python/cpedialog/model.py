__author__ = 'Ping Chen'


import pickle

from google.appengine.ext import db
from google.appengine.ext import search
import logging
import datetime
import urllib
import config
import cgi
import simplejson


class Archive(db.Model):
    monthyear = db.StringProperty(multiline=False)
    """July 2008"""
    weblogcount = db.IntegerProperty(default=0)
    date = db.DateTimeProperty(auto_now_add=True)

class Tag(db.Model):
    tag = db.StringProperty(multiline=False)
    entrycount = db.IntegerProperty(default=0)
    valid = db.BooleanProperty(default = True)


class Weblog(db.Model):
    permalink = db.StringProperty()
    title = db.StringProperty()
    content = db.TextProperty()
    date = db.DateTimeProperty(auto_now_add=True)
    author = db.UserProperty()
    authorEmail = db.EmailProperty()
    catalog = db.StringProperty()
    lastCommentedDate = db.DateTimeProperty()
    commentcount = db.IntegerProperty(default=0)
    lastModifiedDate = db.DateTimeProperty()
    lastModifiedBy = db.UserProperty()
    tags = db.ListProperty(db.Category)
    monthyear = db.StringProperty(multiline=False)
    entrytype = db.StringProperty(multiline=False,default='post',choices=[
        'post','page'])
    _weblogId = db.IntegerProperty()   ##for data migration from the mysql system
    assoc_dict = db.BlobProperty()     # Pickled dict for sidelinks, associated Amazon items, etc.

    def relative_permalink(self):
        if self.entrytype == 'post':
            return self.date.strftime('%Y/%m/')+ self.permalink
        else:
            return self.permalink

    def full_permalink(self):
        if self.entrytype == 'post':
            return config.blog['root_url'] + '/' + self.date.strftime('%Y/%m/')+ self.permalink
        else:
            return config.blog['root_url'] + '/'+ self.permalink

    def get_tags(self):
        '''comma delimted list of tags'''
        return ','.join([urllib.unquote(tag) for tag in self.tags])
  
    def set_tags(self, tags):
        if tags:
            tagstemp = [db.Category(urllib.quote(tag.strip().encode('utf8'))) for tag in tags.split(',')]
            self.tagsnew = [tag for tag in tagstemp if not tag in self.tags]
            self.tags = tagstemp
  
    tags_commas = property(get_tags,set_tags)

    #for data migration
    def update_archive(self,update):
        """Checks to see if there is a month-year entry for the
        month of current blog, if not creates it and increments count"""
        my = self.date.strftime('%B %Y') # July 2008
        archive = Archive.all().filter('monthyear',my).fetch(10)
        if archive == []:
            archive = Archive(monthyear=my,date=self.date,weblogcount=1)
            archive.put()
        else:
            if not update:
                # ratchet up the count
                archive[0].weblogcount += 1
                archive[0].put()

    def update_tags(self,update):
        """Update Tag cloud info"""
        if self.tags: 
            for tag in self.tags:
                tag_ = urllib.unquote(tag)
                tags = Tag.all().filter('tag',tag_).fetch(10)
                if tags == []:
                    tagnew = Tag(tag=tag_,entrycount=1)
                    tagnew.put()
                else:
                    if not update:
                        tags[0].entrycount+=1
                        tags[0].put()

    def save(self):
        self.update_tags(False)
        if self.entrytype == "post":
            self.update_archive(False)
        my = self.date.strftime('%B %Y') # July 2008
        self.monthyear = my
        self.put()

    def update(self):
        self.update_tags(True)
        if self.entrytype == "post":
            self.update_archive(True)
        self.put()


class WeblogReactions(db.Model):
    weblog = db.ReferenceProperty(Weblog)
    user = db.StringProperty()
    date = db.DateTimeProperty(auto_now_add=True)
    author = db.UserProperty()
    authorEmail = db.EmailProperty()
    authorWebsite = db.StringProperty()
    userIp = db.StringProperty()
    content = db.TextProperty()
    lastModifiedDate = db.DateTimeProperty()
    lastModifiedBy = db.UserProperty()
    _weblogReactionId = db.IntegerProperty()   ##for data migration from the mysql system

    def save(self):
        self.put()
        if self.weblog is not None:
            self.weblog.lastCommentedDate = self.date
            self.weblog.commentcount += 1
            self.weblog.put()


class AuthSubStoredToken(db.Model):
  user_email = db.StringProperty(required=True)
  target_service = db.StringProperty(multiline=False,default='base',choices=[
        'apps','base','blogger','calendar','codesearch','contacts','docs',
        'albums','spreadsheet','youtube'])
  session_token = db.StringProperty(required=True)


class CPediaLog(db.Model):
   title = db.StringProperty()
   author = db.StringProperty()
   email = db.StringProperty()
   root_url = db.StringProperty()
   host_ip = db.StringProperty()    


class Album(db.Model):
    album_username = db.StringProperty()
    owner = db.UserProperty()
    date = db.DateTimeProperty(auto_now_add=True)
    access = db.StringProperty(multiline=False,default='public',choices=[
        'public','private','login'])     #public: all can access the album; private:only the owner can access;
                                         #login:login user can access.

    album_type = db.StringProperty(multiline=False,default='public',choices=[
        'public','private'])    #private album need to authorize by user and store the session token.

    valid = db.BooleanProperty(default = True)

class Menu(db.Model):
    title = db.StringProperty()
    permalink = db.StringProperty()          
    target = db.StringProperty(multiline=False,default='_self',choices=[
        '_self','_blank','_parent','_top'])
    order = db.IntegerProperty()
    valid = db.BooleanProperty(default = True)

    def full_permalink(self):
        return config.blog['root_url'] + '/' + self.permalink


class Images(db.Model):
    uploader = db.UserProperty()
    image = db.BlobProperty()
    date = db.DateTimeProperty(auto_now_add=True)


class Greeting(db.Model):
    date = db.DateTimeProperty(auto_now_add=True)
    user = db.StringProperty()
    author = db.UserProperty()
    content = db.StringProperty()
    valid = db.BooleanProperty(default = True)


class FavouriteSite(db.Model):
    title = db.StringProperty()
    permalink = db.StringProperty()
    target = db.StringProperty(multiline=False,default='_self',choices=[
        '_self','_blank','_parent','_top'])
    order = db.IntegerProperty()
    valid = db.BooleanProperty(default = True)


class DeliciousPost(object):
    def __init__(self, item):
        self.link = item["u"]
        self.title = item["d"]
        self.description = item.get("n", "")
        self.tags = item["t"]
