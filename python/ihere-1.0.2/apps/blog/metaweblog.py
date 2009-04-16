# coding=UTF-8
from django.contrib.auth.models import User
from models import *
from xmlrpc import public
import xmlrpclib
from google.appengine.ext import db
#from utility import flickrapi
from google.appengine.api import users
import logging
from google.appengine.api import memcache
from django.contrib.sites.models import Site
from django.conf import settings

def authenticated(pos=1):
    """
    A decorator for functions that require authentication.
    Assumes that the username & password are the second & third parameters.
    Doesn't perform real authorization (yet), it just checks that the
    user is_superuser.
    """
    
    def _decorate(func):
        def _wrapper(*args, **kwargs):
            username = args[pos+0]
            password = args[pos+1]
            args = args[0:pos]+args[pos+2:]
            
            if not username==settings.WLW_ADMIN:
                logging.info("Authorization Failure:not a super user") 
                raise ValueError("Authorization Failure:user name is wrong!")
            if not password==settings.WLW_PASSWD:
                logging.info("Authentication Failure:password is wrong!")   
                raise ValueError("Authentication Failure:password is wrong!")
            user=username
            return func(user, *args, **kwargs)
        
        return _wrapper
    return _decorate

def full_url(url):
    logging.info('http://%s%s' % (Site.objects.get_current().domain, url))
    return 'http://%s%s' % (Site.objects.get_current().domain, url)
    
     

@public
@authenticated()
def blogger_getUsersBlogs(user, appkey):
    logging.info('blogger_getUsersBlogs')
    """
    an array of <struct>'s containing the ID (blogid), name
    (blogName), and URL (url) of each blog.
    """
    return [{
            'blogid': settings.SITE_ID or 1,
            'blogName': 'iHere - Python, Google App Engine, Django, Java & My Life',
            'url': 'http://%s' % (Site.objects.get_current().domain)
            }]

@public
@authenticated()
def metaWeblog_getCategories(user, blogid):
    logging.info('metaWeblog_getCategories')
    tags = Tag.all()
    return [{'description': unicode(tag.name),'title': unicode(tag.name)} for tag in tags]

# example... this is what wordpress returns:
# {'permaLink': 'http://gabbas.wordpress.com/2006/05/09/hello-world/',
#  'description': 'Welcome to <a href="http://wordpress.com/">Wordpress.com</a>. This is your first post. Edit or delete it and start blogging!',
#  'title': 'Hello world!',
#  'mt_excerpt': '',
#  'userid': '217209',
#  'dateCreated': <DateTime u'20060509T16:24:39' at 2c7580>,
#  'link': 'http://gabbas.wordpress.com/2006/05/09/hello-world/',
#  'mt_text_more': '',
#  'mt_allow_comments': 1,
#  'postid': '1',
#  'categories': ['Uncategorized'],
#  'mt_allow_pings': 1}

def format_date(d):
    logging.info('format_date')
    if not d: return None
    logging.info(xmlrpclib.DateTime(d.isoformat()))
    return xmlrpclib.DateTime(d.isoformat())

def post_struct(post):
    logging.info('post_struct')
    link = full_url(post.get_absolute_url())    
    try:
        categories = db.get(post.tags) 
    except: 
        categories =[]
    try:
        category=db.get(post.category).name
    except:
        category='Uncategorilized'
    struct = {
        'postid': str(post.key()),
        'title': unicode(post.title),
        'link': link,
        'permaLink': link,
#        'category':category.name,
        'description': unicode(post.content),
        'categories': [c.name for c in categories],
        'userid': unicode(post.author.nickname()),
        # 'mt_excerpt': '',
        # 'mt_text_more': '',
        # 'mt_allow_comments': 1,
        # 'mt_allow_pings': 1}
        }
    if post.date:
        struct['dateCreated'] = format_date(post.date)
    logging.info('end struct')
    return struct

def setTags(post, struct):
    logging.info('setTags')
    tags = struct.get('categories', None)
    if tags is None:
        post.tags = []
    else:
        post.tags = [Tag.get_or_insert(key_name='tag_'+name.strip(),parent=None,name=name.strip(),slug=name.strip()).key() for name in tags]
    
@public
@authenticated()
def metaWeblog_getPost(user, postid):
    logging.info('metaWeblog_getPost')
    post = Post.get(str(postid))
    return post_struct(post)

@public
@authenticated()
def metaWeblog_getRecentPosts(user, blogid, num_posts):
    logging.info('metaWeblog_getRecentPosts')
    posts = Post.all().order('-date').fetch(int(num_posts))
    logging.info('end metaWeblog_getRecentPosts')
    return [post_struct(post) for post in posts]

@public
@authenticated()
def metaWeblog_newPost(user, blogid, struct, publish):
    logging.info('metaWeblog_newPost')
    body = struct['description']
    # todo - parse out technorati tags
#    logging.info('isPublished=%s'%publish)
    post = Post(title = struct['title'],
                content = body,
                author = users.User(user),
                authorEmail = user,
#                date = struct['dateCreated'],
                isPublished = publish )
#    post.prepopulate()
    post.save()
    setTags(post, struct)
    memcache.flush_all()
    return str(post.key())

@public
@authenticated()
def metaWeblog_editPost(user, postid, struct, publish):
    logging.info('metaWeblog_editPost ')
    post = Post.get(db.Key(postid))
    if post is None:
        post = Post(title = struct['title'],
                content = struct['description'],
                author = users.User(user),
                authorEmail = user,
#                date = struct['dateCreated'],
                isPublished = publish )
    title = struct.get('title', None)
    if title is not None:
        post.title = title
    body = struct.get('description', None)
    if body is not None:
        post.content = body
        # todo - parse out technorati tags
    if user:
        post.author = users.User(user)
    post.isPublished = publish 
    setTags(post, struct)
#    post.prepopulate()
    post.save()
    memcache.flush_all()
    return True

@public
@authenticated(pos=2)
def blogger_deletePost(user, appkey, postid, publish):
    logging.info('blogger_deletePost')
    post = Post.get(postid)
    post.delete()
    memcache.flush_all()
    return True

def storemedia(filename, bits, type):
    logging.info('storemedia')
#    pass
#    api_key='####key####'
#    secret = '####your secret####'
#    flickr = flickrapi.FlickrAPI(api_key = api_key, secret = secret, token = '### token ####', cache=False)
#    f = flickr.uploadbits(filename, bits, type)
#    res = flickr.photos_getSizes(photo_id = f.photoid[0].text)
#    return res.sizes[0].size[3].attrib['source']

    if bits:
        file = bits
        userfile = UserFile(        
                                mimetype=type,
                                name=filename,
                            )
        bin=file.read()
        try:
            userfile.icon=images.resize(bin, width=60)
        except:
            pass
                
        userfile.save() 
        filebin = FileBin(userfile=userfile, bin=db.Blob(bin))
        filebin.save()
        file.close()
        if UserFile.all().count() > FILE_MAX:
            oldest_file = UserFile.all().order('-creationDate').get()
            oldest_file.delete()
        
        return userfile.get_absolute_url()


@public
@authenticated()
def metaWeblog_newMediaObject(blogid, username, password, file):
    logging.info('metaWeblog_newMediaObject')
    # The input struct must contain at least three elements, name,
    # type and bits. returns struct, which must contain at least one
    # element, url
    
    # This method isn't implemented yet, obviously.
    mediaurl = storemedia(file['name'], file['bits'], file['type'])
    return { 'url': mediaurl}
#    return {}
