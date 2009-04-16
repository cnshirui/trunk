# coding=UTF-8
from google.appengine.ext import db
from django.db.models.signals import post_save,pre_delete,post_delete
import logging
from counter import Counter,CounterShard
from django.db.models import signals,permalink
from google.appengine.api import users
from django.db import models
from django.utils.encoding import force_unicode,smart_str
from django.core.urlresolvers import reverse
from tools import cached_property
 
#class SysInfo(db.Model):
#    post_count=db.IntegerProperty(default=0)
#    comment_count=db.IntegerProperty(default=0)
#    
#
#class Archive(db.Model):
#    monthyear = db.StringProperty(multiline=False)    
#    weblogcount = db.IntegerProperty(default=0)
#    date = db.DateTimeProperty(auto_now_add=True)

class Menu(db.Model):
    name=db.StringProperty(multiline=False,default='')
    link=db.StringProperty(multiline=False,default='')
    parentMenu=db.SelfReferenceProperty()
    
    def __unicode__(self):
        return self.name    

    

class Tag(db.Model):
    name = db.StringProperty(multiline=False,default='')
    slug=db.StringProperty(multiline=False,default='')
#    entrycount = db.IntegerProperty(default=0)
#    posts=db.ListProperty(db.Key)
    valid = db.BooleanProperty(default = True)
    
    @cached_property
    def entrycount(self):
        return Post.gql("WHERE tags = :1 and isPublished =:2 ORDER BY date DESC", self.key(), True).count() 
        
#        hits = Counter(str(self.key()))  
#        my_hits=hits.count        
#        return my_hits
    
    def __unicode__(self):
        return self.name
    
    @models.permalink
    def get_absolute_url(self):
        return ('blog.views.tag', [smart_str(self.key()),1])
    
    @cached_property
    def members(self):
        return Post.gql("WHERE tags = :1 and isPublished =:2 ORDER BY date DESC", self.key(), True)

class Category(db.Model):
    name=db.StringProperty(multiline=False,default='')
    slug=db.StringProperty(multiline=False,default='')


#    Usage:
#        hits = Counter('hits')
#        hits.increment()
#        my_hits = hits.count
#        hits.get_count(nocache=True)  # Forces non-cached count of all shards
#        hits.count = 6                # Set the counter to arbitrary value
#        hits.increment(incr=-1)       # Decrement
#        hits.increment(10)
    
    @cached_property
    def entrycount(self):
        return self.post_set.count() 
    
    def __unicode__(self):
        return self.name
    
    @models.permalink
    def get_absolute_url(self):
        return ('blog.views.category', [smart_str(self.name),1,])

class Post(db.Model):
    title = db.StringProperty(required=True)
    content = db.TextProperty(required=True)
    date = db.DateTimeProperty(auto_now_add=True)
    author = db.UserProperty()
    authorEmail = db.EmailProperty(required=True)
    category=db.ReferenceProperty(Category)
    tags = db.ListProperty(db.Key)
    slug = db.StringProperty()
    lastCommentedDate = db.DateTimeProperty()
    visitcount = db.IntegerProperty(default=0)
    lastModifiedDate = db.DateTimeProperty()
    lastModifiedBy = db.UserProperty()    
    entryType = db.StringProperty(required=True,multiline=False,default='post',choices=['post','page'])
    isPublished = db.BooleanProperty(default=False)
    
    @cached_property
    def commentcount(self):
        return Comment.gql("WHERE post = :1 ",self).count()

    def get_cached_visitcount(self):
        hits = Counter(str(self.key()))  
        my_hits=hits.count
        if self.visitcount+10 < my_hits:
            self.visitcount=my_hits
            self.put()
            logging.info('sync_visitcount=%s'%my_hits)
        return my_hits
    

    def delete(self):
        comments = self.comment_set.fetch(1000)
        [comment.delete() for comment in comments]
#        if comments:
#            db.delete(comments)
        super(Post, self).delete()
        
    def __unicode__(self):
        return '%s adds a post: [%s] at %s'%(self.author,self.title,self.date.strftime('%A, %d. %B %Y %I:%M%p'))
        
#    @models.permalink
#    def get_absolute_url(self):
#        return ('blog.views.view_post', [smart_str(self.key())])
    
    def get_permalink(self):
        if not self.slug:
            return reverse('blog.views.view_post', args=[smart_str(self.key())])

        try:
            return reverse('view_post_perma',urlconf=None, args=None ,kwargs={
            'year': self.date.year,
            'month': str(self.date.month).zfill(2),
            'day': str(self.date.day).zfill(2),
            'slug': self.slug})  
        except:
            try:
                return reverse('view_post_perma',urlconf=None, args=None ,kwargs={
                'year': self.date.year,
                'month': str(self.date.month).zfill(2),
                'slug': self.slug})  
            except:
                try:
                    return reverse('view_post_perma',urlconf=None, args=None ,kwargs={
                    'slug': self.slug})  
                except:
                    return reverse('blog.views.view_post', args=[smart_str(self.key())])
    
    get_absolute_url=get_permalink
    
    @cached_property
    def get_tags(self):
        return db.get(self.tags)   
    
    @cached_property
    def get_tags_string(self):
        return ','.join([tag.name for tag in db.get(self.tags)])
        
    @cached_property
    def next(self):     
        return Post.gql('WHERE date > :1 AND isPublished = :2 ORDER BY date',self.date,self.isPublished).fetch(1)

    @cached_property
    def prev(self):
        return Post.gql('WHERE date < :1 AND isPublished = :2 ORDER BY date DESC',self.date,self.isPublished).fetch(1)

        
class Comment(db.Model):
    post = db.ReferenceProperty(Post)
    user = db.StringProperty(required=True)
    date = db.DateTimeProperty(auto_now_add=True)
    author = db.UserProperty()
    authorEmail = db.EmailProperty(required=True)
    authorWebsite = db.StringProperty()
    userIp = db.StringProperty()
    content = db.TextProperty(required=True)
    lastModifiedDate = db.DateTimeProperty()
    lastModifiedBy = db.UserProperty()

    def __unicode__(self):
        return '%s adds a comment on Post: [%s] at %s'%(self.user,self.post.title,self.date.strftime('%A, %d. %B %Y %I:%M%p'))

class Link(db.Model):
    description=db.StringProperty()
    href=db.StringProperty()
    target=db.StringProperty(default='_blank', multiline=False,choices=['_blank','_self'])
    date=db.DateTimeProperty(auto_now_add=True)
    status=db.BooleanProperty(default=True)
    
    def __unicode__(self):
        return """<a target="%s" href="%s">%s</a>"""%(self.target,self.href,self.description)
        

class Event(db.Model):  
    event = db.ReferenceProperty()
    created = db.DateTimeProperty(auto_now_add=True)
    
    def __unicode__(self):
        return self.event



def log_event_on_post_save(sender,**kwargs):  
    instance = kwargs['instance']
    if kwargs['created']:  
#        logging.info('[event]post_save: %s'%instance)        
        event = Event(user=users.get_current_user(),event = instance)  
        event.put()  
    
def delete_event_on_pre_delete(sender,**kwargs):  
    instance = kwargs['instance']
    event = Event.all().filter("event =", instance).get()
    if event:
#        logging.info('delete an event:%s'%event.event)
        event.delete()        

def sync_post_visitcount(sender,**kwargs):
    counterShard = kwargs['instance']
    post=Post.get(counterShard.name)
    counter=Counter(counterShard.name)
    visitcount=post.visitcount
    hits=counter.count
    if visitcount<hits:
        post.visitcount=hits
        post.put()    
#    else:
#        counter.set_count(visitcount)

#signals.post_save.connect(sync_post_visitcount, sender=CounterShard)

signals.post_save.connect(log_event_on_post_save, sender=Comment)
signals.post_save.connect(log_event_on_post_save, sender=Post)
signals.pre_delete.connect(delete_event_on_pre_delete, sender=None)
