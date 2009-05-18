# coding=UTF-8
from django.contrib.syndication.feeds import Feed, FeedDoesNotExist
from blog.models import Post,Category
from django.utils.encoding import force_unicode,smart_str
import urllib
from django.utils import feedgenerator
from google.appengine.api import users
from ragendja.dbutils import get_object_or_404
from django.http import Http404
from django.conf import settings
from django.contrib.sites.models import Site
try:
    POSTS_PER_FEED=settings.POSTS_PER_FEED
except:
    POSTS_PER_FEED=10


class LatestEntries(Feed):
    BASE_URL='http://%s' % (Site.objects.get_current().domain)

    title="iHere's LatestEntries"
    link="/feeds/latest/"
    description="Updates on changes and additions to iHere"
#    user=users.User(settings.FEEDS_EMAIL)
    item_author_name=settings.FEEDS_AUTHOR
    item_author_email=settings.FEEDS_EMAIL
    item_author_link=BASE_URL+'/about/'
    
    def item_pubdate(self,post):
        return post.date
    
    def item_link(self, post):
        if not post:
            raise FeedDoesNotExist
        return post.get_permalink()
    
    def items(self,post):
        return Post.all().order('-date').fetch(POSTS_PER_FEED)
    
    def item_categories(self, post):
        """
        Takes the object returned by get_object() and returns the feed's
        categories as iterable over strings.
        """
        return (post.category,)
    
class LatestEntriesByCategory(Feed):
    BASE_URL='http://%s' % (Site.objects.get_current().domain)

    title="iHere's LatestEntriesByCategory"
    link="/feeds/categories/"
    description="Updates on changes and additions to iHere by category"
    item_author_name=settings.FEEDS_AUTHOR
    item_author_email=settings.FEEDS_EMAIL
    item_author_link=BASE_URL+'/about/'

    def item_pubdate(self,post):
        return post.date
    
    def get_object(self, bits):
        if len(bits)!=1:
            return Post.all().order('-date').fetch(POSTS_PER_FEED)
        category=Category.all().filter('name =',force_unicode(urllib.unquote(smart_str(bits[0])))).get()
        if not category:
            raise Http404()
        return category.post_set.order('-date').fetch(POSTS_PER_FEED)
        
    
    def item_link(self, post):
        if not post:
            raise FeedDoesNotExist
        return post.get_permalink()
    
    def items(self,post):
        return post
    
    def item_categories(self, post):
        """
        Takes the object returned by get_object() and returns the feed's
        categories as iterable over strings.
        """
        return (post.category,)
    
class PostEntry(Feed):
    BASE_URL='http://%s' % (Site.objects.get_current().domain)

    title="iHere's PostEntry"
    link="/feeds/post/"
    description="iHere's Blog"
    item_author_name=settings.FEEDS_AUTHOR
    item_author_email=settings.FEEDS_EMAIL
    item_author_link=BASE_URL+'/about/'

    def item_pubdate(self,post):
        return post.date
    
    def get_object(self, bits):
        if len(bits)!=1:
            raise FeedDoesNotExist
        return [get_object_or_404(Post,force_unicode(urllib.unquote(smart_str(bits[0])))),]
        
    
    def item_link(self, obj):
        post=get_object_or_404(Post,obj.key())
        if not post:
            raise FeedDoesNotExist
        return smart_str(post.get_permalink())
    
    def items(self,post):
        return post
    
    def item_categories(self, post):
        """
        Takes the object returned by get_object() and returns the feed's
        categories as iterable over strings.
        """
        return (post.category,)