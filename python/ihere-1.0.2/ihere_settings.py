# -*- coding: utf-8 -*-
#start ihere blog settings##########################################
#LINK_FORMAT=r'^(?P<slug>.*).html$'
#LINK_FORMAT=r'^(?P<slug>.*)/$'
#LINK_FORMAT=r'^(?P<year>\d+)/(?P<month>\d\d+)/(?P<day>\d\d+)/(?P<slug>.*).html$'
LINK_FORMAT=r'^(?P<year>\d+)/(?P<month>\d\d+)/(?P<slug>.*).html$'
WLW_ADMIN='ihere@domain.com'
WLW_PASSWD='ihere'
#end ihere blog settings############################################


# Email server settings
EMAIL_HOST = 'gmail.com'
EMAIL_PORT = 25
EMAIL_HOST_USER = 'user'
EMAIL_HOST_PASSWORD = 'pass'
EMAIL_USE_TLS = True
DEFAULT_FROM_EMAIL = 'user@gmail.com'
SERVER_EMAIL = 'user@gmail.com'

#feeds settings
FEEDS_AUTHOR=WLW_ADMIN
FEEDS_EMAIL=WLW_ADMIN
POSTS_PER_FEED=10

#twitter settings
TWITTER_ID=21535364
TWITTER_USERNAME='iHereBlog'