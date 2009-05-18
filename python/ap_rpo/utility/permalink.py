# coding=UTF-8
import os
import re
import datetime
import logging
import urllib
from django.http import HttpResponseServerError
from django.http import HttpResponseRedirect,Http404, HttpResponseForbidden,HttpResponse,HttpResponseNotFound
import translate
from google.appengine.ext import db
from django.shortcuts import render_to_response
from google.appengine.api import urlfetch
from django.utils.encoding import force_unicode,smart_str
from django.contrib.sites.models import Site

def call_slug_service_demo(request):
    if 'slug_text' not in request.REQUEST:
        return HttpResponseNotFound()
    slug_text=request.REQUEST['slug_text']
    slug_text=force_unicode(urllib.unquote(smart_str(slug_text)))
    result= get_slug_from_service(slug_text)
    return render_to_response('slug_service_demo.html',{'result':force_unicode(urllib.unquote(smart_str(result)))})

def get_slug_from_service(slug_text):
    BASE_URL='http://%s' % (Site.objects.get_current().domain)

    page = urlfetch.fetch(
        url=BASE_URL+"/slug/service/?"+urllib.quote(smart_str(slug_text)),
        payload=urllib.urlencode({'slug_text': urllib.quote(smart_str(slug_text)) ,}),
        method=urlfetch.POST,
        headers={'Content-Type': 'application/x-www-form-urlencoded'}
    )
    if page.status_code == 200:
        result= page.content
    else:
        result= ""
    return result

def slug_service(request):
    if 'slug_text' not in request.REQUEST:
        return HttpResponseNotFound()
    slug_text=request.REQUEST['slug_text']
    slug_text=force_unicode(urllib.unquote(smart_str(slug_text)))
#    logging.info('slug_text=%s'%slug_text)
    result=get_slug(slug_text)
    if result is None:
        return HttpResponseNotFound()
    else:
        return HttpResponse(result)

def get_slug(title):
#    logging.info('title=%s'%title)
    translated_text=translate.translate_ajax('zh-CN','en', title)
    if translated_text is None:
        return None 
    logging.info('translated_text=%s'%translated_text)
    try:
        slug = get_friendly_url(translated_text)
#        logging.info('slug=%s'%slug)
    except:
        slug = translated_text
    from blog.models import Post
    search_blog = db.GqlQuery("select * from %s where slug >= :1 and slug < :2 order by slug desc"%Post.kind(),slug, slug+u"\xEF\xBF\xBD").get()
#    logging.info('search_blog=%s'%search_blog)
    if search_blog is not None:
        slug = search_blog.slug+"i"
#    logging.info('slug=%s'%slug)
    return slug

def get_friendly_url(title):
    return re.sub('-+', '-', re.sub('[^\w-]', '', re.sub('\s+', '-', removepunctuation(title).strip()))).lower()

def removepunctuation(str):
    punctuation = re.compile(r'[.?!,":;]')
    str = punctuation.sub("", str)
    return str

def u(s, encoding):
    if isinstance(s, unicode):
        return s
    else:
        return unicode(s, encoding)