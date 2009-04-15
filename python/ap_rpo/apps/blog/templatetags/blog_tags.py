from django.template import Library
from django.template.defaultfilters import stringfilter

#from blog.models import Post
from google.appengine.ext import db
register = Library()

@register.inclusion_tag("blog/post_item.html")
def show_blog_post(post):
    return {"post": post}

@register.inclusion_tag("blog/menu.html")
def show_menu():
    menus=db.GqlQuery("SELECT * FROM Menu WHERE parentMenu = :1",None) 
#    'tags':Tag.all().filter('entrycount >',0),
    return {"menus": menus}

@register.inclusion_tag("blog/hotlist.html")
def show_hotlist(num=10):
    hotlist=db.GqlQuery("SELECT * FROM Post " + 
                "WHERE isPublished = :1 " +
                "ORDER BY visitcount DESC",
                True, ).fetch(num) 
#    hotlist=Post.all().filter('isPublished =',True).order('-visitcount').fetch(num)   
    return {"hotlist": hotlist}

@register.inclusion_tag("blog/recent_comments.html")
def show_recent_comments(num=10):
    recent_comments=db.GqlQuery("SELECT * FROM Comment " + 
                "ORDER BY date DESC",).fetch(num) 
#    recent_comments=Comment.all().order('-date').fetch(10)
    return {"recent_comments": recent_comments}

@register.inclusion_tag("blog/links.html")
def show_links():
    link_list=db.GqlQuery("SELECT * FROM Link") 
    return {"link_list": link_list}

@register.inclusion_tag("blog/tags.html")
def show_tags():
    tags=db.GqlQuery("SELECT * FROM Tag") 
#    'tags':Tag.all().filter('entrycount >',0),
    return {"tags": tags}

@register.inclusion_tag("blog/categories.html")
def show_categories():
    categories=db.GqlQuery("SELECT * FROM Category") 
#    'categories':Category.all().filter('entrycount >',0),
    return {"categories": categories}

@register.filter(name='restrict_to_max')
@stringfilter
def restrict_to_max(value,max):
    if int(value) <= int(max):
        return value
    else:
        return max

