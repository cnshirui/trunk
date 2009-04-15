# -*- coding: utf-8 -*-
from ragendja.settings_pre import *
try:
  from ihere_settings import *
except ImportError:
  pass


#change following code of ragendja.settings_pre,which add gae_admin_template dir to the TEMPLATE_DIR
#PROJECT_DIR = os.path.dirname(os.path.dirname(os.path.dirname(
#    os.path.dirname(__file__))))
#COMMON_DIR = os.path.dirname(os.path.dirname(os.path.dirname(__file__)))
#
##ROOT_PATH = os.path.dirname(__file__)
#GAE_HACK_TEMPLATES=os.path.join(PROJECT_DIR, 'GAE_admin_templates')
#MAIN_DIRS = (GAE_HACK_TEMPLATES,PROJECT_DIR, COMMON_DIR,)
#
#TEMPLATE_DIRS = tuple([os.path.join(dir, 'templates') for dir in MAIN_DIRS])


#Very important: If you upgrade an existing project please set DJANGO_STYLE_MODEL_KIND = False in your settings.py. 
#From now on, the model kind() gets prefixed with the app label, by default
#, which prevents naming conflicts when multiple apps define models with the same name. 
#Note that Django uses this naming scheme, too, when creating SQL tables. 
#This feature breaks existing models which are stored without the app label, 
#so you need to disable it when upgrading your project (or you can override the kind() method for existing models).
DJANGO_STYLE_MODEL_KIND = False


## Combine media files
#COMBINE_MEDIA = {
#    # Create a combined JS file which is called "combined-en.js" for English,
#    # "combined-de.js" for German, and so on
#    'combined-%(LANGUAGE_CODE)s.js': (
#        # This adds a settings variable which gives you access to the MEDIA_URL
##        '.site_data.js',
#        # Integrate bla.js from "myapp/media" folder
#        # You don't write "media" because that folder is used automatically
##        'myapp/bla.js',
#        # Integrate morecode.js from "media" under project root folder
#        'global/js/jquery-1.3.1.min.js',
#        'global/js/bookmarklet.js',
#        'global/js/load-google.js',
#        'global/js/load-twitter-head.js',
#    ),
#    # Create a combined CSS file which is called "combined-ltr.css" for
#    # left-to-right text direction
#    'combined-%(LANGUAGE_DIR)s.css': (
##        'myapp/style.css',
#        # Load layout for the correct text direction
#        'global/stylesheets/style.css',
#    ),
#}

# Increase this when you update your media on the production site, so users
# don't have to refresh their cache. By setting this your MEDIA_URL
# automatically becomes /media/MEDIA_VERSION/
MEDIA_VERSION = 1

# Make this unique, and don't share it with anybody.
SECRET_KEY = '1234567890'

#ENABLE_PROFILER = True
#ONLY_FORCED_PROFILE = True
#PROFILE_PERCENTAGE = 25
#SORT_PROFILE_RESULTS_BY = 'cumulative' # default is 'time'
#PROFILE_PATTERN = 'ext.db..+\((?:get|get_by_key_name|fetch|count|put)\)'

# Enable I18N and set default language to 'en'
USE_I18N = True
LANGUAGE_CODE = 'en'

#Restrict supported languages (and JS media generation)
#LANGUAGES = (
#    ('de', 'German'),
#    ('en', 'English'),
#)

TEMPLATE_CONTEXT_PROCESSORS = (
    'django.core.context_processors.auth',
    'django.core.context_processors.media',
    'django.core.context_processors.request',
    'django.core.context_processors.i18n',
    'blog.context_processors.twitter',
)

MIDDLEWARE_CLASSES = (
    'ragendja.sites.dynamicsite.DynamicSiteIDMiddleware',
    'django.middleware.cache.UpdateCacheMiddleware',                      
    'django.contrib.sessions.middleware.SessionMiddleware',
    # Django authentication
#    'django.contrib.auth.middleware.AuthenticationMiddleware',
    # Google authentication
    'ragendja.auth.middleware.GoogleAuthenticationMiddleware',
    # Hybrid Django/Google authentication
    #'ragendja.auth.middleware.HybridAuthenticationMiddleware',
    'django.middleware.common.CommonMiddleware',
    'django.middleware.locale.LocaleMiddleware',    
    'django.contrib.flatpages.middleware.FlatpageFallbackMiddleware',
    'django.contrib.redirects.middleware.RedirectFallbackMiddleware',    
    'django.middleware.cache.FetchFromCacheMiddleware',
)

# Google authentication
AUTH_USER_MODULE = 'ragendja.auth.google_models'
AUTH_ADMIN_MODULE = 'ragendja.auth.google_admin'
# Hybrid Django/Google authentication
#AUTH_USER_MODULE = 'ragendja.auth.hybrid_models'

GLOBALTAGS = (
    'ragendja.templatetags.ragendjatags',
    'django.templatetags.i18n',
    'ragendja.templatetags.googletags',
    'blog.templatetags.blog_tags',
)

LOGIN_URL = '/account/login/'
LOGOUT_URL = '/account/logout/'
LOGIN_REDIRECT_URL = '/'

import sys
ROOT_PATH = os.path.dirname(__file__)

sys.path.insert(0, os.path.join(ROOT_PATH, "utility"))
sys.path.insert(0, os.path.join(ROOT_PATH, "apps"))

INSTALLED_APPS = (
    'django.contrib.auth',
    'django.contrib.sessions',
    'django.contrib.admin',
    'django.contrib.webdesign',
    'django.contrib.flatpages',
    'django.contrib.redirects',
    'django.contrib.sites',
    'appenginepatcher',
    'mediautils',
    'ragendja',
    'django.contrib.markup',
     'blog',
     'utility',
     'import_wxp',
     'upload',     
)

# List apps which should be left out from app settings and urlsauto loading
IGNORE_APP_SETTINGS = IGNORE_APP_URLSAUTO = (
)

from ragendja.settings_post import *
