"""
translate.py

Translates strings using Google Translate

All input and output is in unicode.
"""

__all__ = ('source_languages', 'target_languages', 'translate')

import sys
import urllib,logging
from google.appengine.api import urlfetch
from BeautifulSoup import BeautifulSoup
from django.utils import simplejson as json
base_uri = "http://ajax.googleapis.com/ajax/services/language/translate"

default_params = {'v': '1.0'}

def translate_ajax(sl, tl, phrase):
    assert type(phrase) == type(u''), "Expects input to be unicode."
    args = default_params.copy()
    args.update({
        'langpair': '%s%%7C%s' % (sl, tl),
        'q': urllib.quote_plus(phrase.encode('utf-8')),
    })
    argstring = '%s' % ('&'.join(['%s=%s' % (k,v) for (k,v)in args.iteritems()]))
    url = base_uri + '?'+ argstring
    try:
        response=urlfetch.fetch(url)
        if response.status_code==200:
            resp = json.loads(response.content)
#            logging.info(resp)
            if resp['responseStatus']==200 and resp['responseData']['translatedText'] is not u'':
                return resp['responseData']['translatedText']
            else:
                return None
        else:
            return None
    except:
        return None



def translate(sl, tl, text):

    assert type(text) == type(u''), "Expects input to be unicode."

    # Do a POST to google

    # I suspect "ie" to be Input Encoding.
    # I have no idea what "hl" is.

    translated_page = urlfetch.fetch(
        url="http://translate.google.com/translate_t?" + urllib.urlencode({'sl': sl, 'tl': tl}),
        payload=urllib.urlencode({'hl': 'en',
                               'ie': 'UTF8',
                               'text': text.encode('utf-8'),
                               'sl': sl,
                               'tl': tl}),
        method=urlfetch.POST,
        headers={'Content-Type': 'application/x-www-form-urlencoded'}
    )

    if translated_page.status_code == 200:
        translated_soup = BeautifulSoup(translated_page.content)
        return translated_soup('div', id='result_box')[0].string
    else:
        return ""
