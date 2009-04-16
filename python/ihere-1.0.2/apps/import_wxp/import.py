#encoding=UTF-8
#http://www.eriksmartt.com/blog/archives/306
#In Part 1 of this series, I described some of the motivation, and the components being used to build a new blog for myself. In this (lengthy) post, I’ll address the solution I used to move my content archives from WordPress to the new app.
#useage:
#import.py -f c:/wordpress.xml -m evertobe@gmail.com -a inforsphere -s 6.latest.inforsphere.appspot.com

def init_env():
    import aecmd
    aecmd.setup_project()
    from appenginepatch.appenginepatcher.patch import patch_all, setup_logging
    patch_all()
    from django.conf import settings

import sys
import getpass
import datetime,logging,re,urllib,os,time
from os.path import abspath, dirname, join
# Hardwire in appengine modules to PYTHONPATH
# or use wrapper to do it more elegantly
appengine_dirs = ['C:/Program Files/Google/google_appengine','C:/Program Files/Google/google_appengine/lib','C:/Program Files/Google/google_appengine/lib/yaml','C:/Program Files/Google/google_appengine/lib/antlr3',]
sys.path.extend(appengine_dirs)
# Add current folder to sys.path, so we can import aecmd
PROJECT_ROOT = abspath(os.path.dirname(os.path.dirname(os.path.dirname(__file__))))
sys.path.insert(0, join(PROJECT_ROOT, "common/appenginepatch"))

init_env()
# Add your models to path
#my_root_dir = os.path.abspath(os.path.dirname(__file__))
my_root_dir =os.path.dirname(os.path.dirname(__file__))
sys.path.insert(0, my_root_dir)
import xml.etree.ElementTree as ET
from google.appengine.ext import db
from google.appengine.api import users
from google.appengine.ext.remote_api import remote_api_stub
from blog.models import *
from django.utils.encoding import force_unicode,smart_str
from django.db.models import signals
from optparse import OptionParser
from parseWp import Import,Importer

def auth_func():
    return (raw_input('Username:'), getpass.getpass('Password:'))

def parseoptions(args):
    """Parses command line options."""
    parser = OptionParser()
    parser.add_option("-a", "--appname", default="", type="string",
                      help="The name of the app you want to connect to.")
    parser.add_option("-s", "--server", default="", type="string",
                      help="The name of the server you want to connect to.")
    parser.add_option("-f", "--file", type="string",
                      help="The file to import.")
    parser.add_option("-d", "--authdomain", default="gmail.com", type="string",
                      help="The AUTH_DOMAIN.")
    parser.add_option("-m", "--useremail", type="string",
                      help="The USER_EMAIL.")
    return parser.parse_args(args)[0]

def do_remote_import(options):
    file_path=options.file or raw_input('file:')
    wxpfile=open(file_path)
    logging.info('file: %s'%file_path) 
    logging.info('start importing...') 
    importer=Importer(wxpfile)
    importer.import2Gae()
    logging.info('done!')   


if __name__ == '__main__':
    options = parseoptions(sys.argv)
    os.environ['AUTH_DOMAIN'] = options.authdomain 
    os.environ['USER_EMAIL'] = options.useremail or raw_input('useremail:')
    APP_NAME=options.appname or raw_input('APPID:')    

    remote_api_stub.ConfigureRemoteDatastore(
                                         APP_NAME,
                                         '/remote_api',
                                          auth_func,
                                          servername=options.server,
#                                          servername='localhost:8080',
                                        )
    
    do_remote_import(options)



