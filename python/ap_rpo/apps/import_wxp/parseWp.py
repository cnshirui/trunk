#encoding=UTF-8
#http://www.eriksmartt.com/blog/archives/306
#In Part 1 of this series, I described some of the motivation, and the components being used to build a new blog for myself. In this (lengthy) post, I’ll address the solution I used to move my content archives from WordPress to the new app.
#encoding=UTF-8
import xml.etree.ElementTree as ET
from google.appengine.ext import db
from google.appengine.api import users
from blog.models import *
import datetime,logging,re,urllib,os,time
from django.utils.encoding import force_unicode,smart_str
from django.db.models import signals
from optparse import OptionParser

class Import(object):
    def __init__(self,wordpress_xml_file):
        self.tree = ET.parse(wordpress_xml_file)
        self.wpns='{http://wordpress.org/export/1.0/}'
        self.contentns="{http://purl.org/rss/1.0/modules/content/}"
        ET._namespace_map[self.wpns]='wp'
        ET._namespace_map[self.contentns]='content'
        self.results={}
        self.items=[]
        
    def make_comments(self,comments,post):
        self.results['comments']=[]
        for com in comments:
            try:
                comment_approved=int(com.findtext(self.wpns+'comment_approved'))
            except:
                comment_approved=0
            if comment_approved:
                comment=dict(
                                    author=com.findtext(self.wpns+'comment_author'),
                                    content=com.findtext(self.wpns+'comment_content'),
                                    email=com.findtext(self.wpns+'comment_author_email'),
                                    weburl=com.findtext(self.wpns+'comment_author_url'),
                                    author_IP=com.findtext(self.wpns+'comment_author_IP'),
                                    date=com.findtext(self.wpns+'comment_date'),
                                    date_gmt=com.findtext(self.wpns+'comment_date_gmt'),
                            )
    
                self.results['comments'].append(comment)
        return self.results
    
    def make_categories_and_tags(self,categories):
        self.results['categories'] = []
        self.results['tags']=[]        
        for cat in categories:
            if cat.attrib.has_key('domain'):
                cat_type=cat.attrib['domain']
                if cat_type=='tag':
                    if cat.text:
                        self.results['tags'].append(cat.text.strip())
                else:
                    nicename=cat.findtext(self.wpns+'category_nicename').strip()
                    name=cat.findtext(self.wpns+'cat_name').strip()
                    self.results['categories'].append({'nicename':nicename,'name':name})
            else:
                self.results['category']=force_unicode(urllib.unquote(smart_str(cat.text.strip())))
        
        return self.results
    
    def get_slug(self,linkstr):
        #        regex=ur'^.*/(.*)$'
        #        match = re.search(regex, subject)
        #        if match:
        #            result = match.group()
        #        else:
        #            result = ""
        linkstr=force_unicode(urllib.unquote(smart_str(linkstr.strip())))
        if linkstr.rfind('.html')==-1:
            linkstr=linkstr[:linkstr.rfind('/')] 
        else:
            linkstr=linkstr[:linkstr.rfind('.html')]
            
        slug=os.path.basename(linkstr)        
        
        return slug
    
    def make_post(self,item):
        self.results['link'] = item.find("link").text
        # get slug from link       
        self.results['slug'] = self.get_slug(item.find("link").text)
        self.results['title'] = item.find("title").text
        self.results['pubDate'] = item.find("pubDate").text
        self.results['summary'] = item.find("description").text
        self.results['body'] = item.find(self.contentns+"encoded").text
        self.results['post_date'] = item.find(self.wpns+"post_date").text
        self.results['post_date_gmt'] = item.find(self.wpns+"post_date_gmt").text
        return self.results
    
    def make_category_list(self):
        category_list=[]
        for category in self.tree.findall("channel/%scategory"%self.wpns):
            nicename=category.findtext(self.wpns+'category_nicename')
            name=category.findtext(self.wpns+'cat_name')
            category_list.append({'nicename':nicename,'name':name})
        return category_list
    
    def make_tag_list(self):
        tag_list=[]
        for tag in self.tree.findall("channel/%stag"%self.wpns):
            slug=tag.findtext(self.wpns+'tag_slug')
            name=tag.findtext(self.wpns+'tag_name')
            if slug and name:
                tag_list.append({'slug':slug,'name':name})
        return tag_list

    def generate(self):
        category_list=self.make_category_list()
        tag_list=self.make_tag_list()
        for item in self.tree.findall("channel/item"):
            post=self.make_post(item)
            categories = item.findall("category")
            self.make_categories_and_tags(categories)
            comments=item.findall(self.wpns+'comment')
            self.make_comments(comments, post)
            self.items.append(self.results)
            self.results={}
        return (self.items,category_list,tag_list)


class Importer(object):
    
    def __init__(self,wordpress_xml_file):
        self.generater=Import(wordpress_xml_file)
        self.tree = self.generater.tree
        self.wpns='{http://wordpress.org/export/1.0/}'
        self.contentns="{http://purl.org/rss/1.0/modules/content/}"
        ET._namespace_map[self.wpns]='wp'
        ET._namespace_map[self.contentns]='content'
        (self.items,self.category_list,self.tag_list)=self.generater.generate()

    def import2Gae(self):        
        signals.post_save.disconnect(log_event_on_post_save, sender=Comment)
        signals.post_save.disconnect(log_event_on_post_save, sender=Post)
        logging.info('importing categories...')
        category_list=self.__import_category_list()
        logging.info('%s categories imported...'%len(category_list))
        logging.info('importing tags...')
        tag_list=self.__import_tag_list()
        logging.info('%s tags imported...'%len(tag_list))
        logging.info('importing posts...')
        for item in self.items:
            post,created=self.__import_post(item)
            logging.info('post:%s imported...'%item['title'])
#            if created:
#                self.make_tag_counts(post)
#                self.make_category_counts(post)
        logging.info('%s posts imported...'%len(self.items))
#            post=db.run_in_transaction(self.__import_post, item)
       
        signals.post_save.connect(log_event_on_post_save, sender=Comment)
        signals.post_save.connect(log_event_on_post_save, sender=Post)
        
    def make_tag_counts(self,post):
        tags=post.tags
        for tagkey in tags:
            db.run_in_transaction(self.increment_counter,tagkey,post.key(), 1, True)
            
    def make_category_counts(self,post):
        category=post.category        
        db.run_in_transaction(self.increment_counter,category.key(),post.key(), 1, True)
    
    def __import_tag_list(self):
        tag_list=[]
        for tag_dict in self.tag_list:
            key_name='tag_'+force_unicode(urllib.unquote(smart_str(tag_dict['name'])))
            tag=Tag.get_or_insert(key_name=key_name,parent=None,name=tag_dict['name'],slug=tag_dict['slug'],entrycount=0)
            tag_list.append(tag)
#            logging.info('__import_tag_list :%s'%tag_dict['name'])
        return tag_list
            
    def __import_category_list(self):
        category_list=[]
        for category_dict in self.category_list:
            key_name='category_'+force_unicode(urllib.unquote(smart_str(category_dict['name'])))
            category=Category.get_or_insert(key_name=key_name,parent=None,name=category_dict['name'],slug=category_dict['nicename'],entrycount=0)
            category_list.append(category)
        return category_list

                    
    def __import_site_info(self,results):
        pass    
    
    def __import_post(self,item):
        if 'category' not in item or not item['category']:
            item['category']='Uncategoried'
        category_key_name='category_'+force_unicode(urllib.unquote(smart_str(item['category'])))
        category=Category.get_or_insert(key_name=category_key_name,parent=None,name=item['category'],slug=item['category'],entrycount=0)
        timestamp = self.get_key_from_time(item['post_date'])
        key_name='post_'+timestamp
        created= not db.get(db.Key.from_path(Post.kind(),key_name))
        tags=self.__import_tags_of_post(item)
        categories=self.__import_categories_of_post(item)
        tags+=categories
        post=Post.get_or_insert(
                                  key_name=key_name,
#                                  parent=category,
#                                  category=category,
                                  title =item['title'],
                                  content =item['body'],
                                  date =self.get_date_from_string(item['post_date'],'%Y-%m-%d %H:%M:%S',),
#                                  date=datetime.datetime.strptime(item['post_date'],'%Y-%m-%d %H:%M:%S',),
                                  author =users.get_current_user(),
                                  authorEmail =users.get_current_user().email(),
                                  slug =item['slug'],
                                  tags=tags,
#                                  categories=categories,
                                  isPublished=True,
                                )
        if not post.category:
            post.category=category
            post.put()
        self.__import_comments(item, post)
        return post,created
    
    def __import_categories_of_post(self,item):
        category_list=[]
        for cat in item['categories']:
            tag_name=force_unicode(urllib.unquote(smart_str(cat['name'])))
            key_name='tag_'+tag_name
            if cat['name'] is not None:
                tag = Tag.get_or_insert(key_name=key_name,parent=None,name=cat['name'],slug=cat['nicename'],entrycount=0)
                category_list.append(tag.key())
            
#        for category_dict in item['categories']:
#            key_name='category_'+force_unicode(urllib.unquote(smart_str(category_dict['name'])))
#            category= Category.get_or_insert(key_name=key_name,parent=None,name=category_dict['name'],slug=category_dict['nicename'])
#            category_list.append(category.key())        
        return category_list
    
    def __import_tags_of_post(self,item):
        tag_list=[]
        for tag_name in item['tags']:
            tag_name=force_unicode(urllib.unquote(smart_str(tag_name)))
            key_name='tag_'+tag_name
#            tag= db.get(db.Key.from_path(Tag.kind(),key_name))  
            if tag_name is not None:
                tag = Tag.get_or_insert(key_name=key_name,parent=None,name=tag_name,slug=tag_name,entrycount=0)
                tag_list.append(tag.key())
#            logging.info('__import_tags_of_post :%s'%tag_name)
            
        return tag_list

    def get_key_from_time(self,strtime):
        strtime=force_unicode(urllib.unquote(smart_str(strtime)))
        date = self.get_date_from_string(strtime,'%Y-%m-%d %H:%M:%S',).strftime('%Y-%m-%d %H:%M:%S')
#        logging.info('date:%s'%date)
        timestamp = time.strptime(date,'%Y-%m-%d %H:%M:%S',)
#        logging.info('timestamp:%s'%timestamp)
        return str(time.mktime(timestamp))[:10]
    
    def __import_comments(self,item,post):
        comment_list=[]
        for comment_dict in item['comments']:
            timestamp = self.get_key_from_time(comment_dict['date'])
            key_name='comment_'+timestamp
            comment= Comment.get_or_insert(
                                key_name=key_name,
                                post = post,
                                user = comment_dict['author'],
                                date = self.get_date_from_string(comment_dict['date'],'%Y-%m-%d %H:%M:%S',),
#                                date = datetime.datetime.strptime(comment_dict['date'],'%Y-%m-%d %H:%M:%S',),
#                                author = users.User(comment_dict['email']),
                                authorEmail = comment_dict['email'] or 'none@dumy.com',
                                authorWebsite = comment_dict['weburl'],
                                userIp = comment_dict['author_IP'],
                                content = comment_dict['content'],
                                parent=post,
                                )
            
            comment_list.append(comment)
        return comment_list          

    def increment_counter(self,key,post_key, amount,created):
        obj = db.get(key)
        if created:
            obj.entrycount += amount
            return db.put(obj)
        else:
            return obj
        
    def get_date_from_string(self,t, format):
        try:
            return datetime.datetime(*time.strptime(t, format)[0:6])
        except ValueError, msg:
            if "%S" in format:
                msg = str(msg)
                mat = re.match(r"unconverted data remains:"
                               " \.([0-9]{1,6})$", msg)
                if mat is not None:
                    # fractional seconds are present - this is the style
                    # used by datetime's isoformat() method
                    frac = "." + mat.group(1)
                    t = t[:-len(frac)]
                    t = datetime.datetime(*time.strptime(t, format)[0:6])
                    microsecond = int(float(frac)*1e6)
                    return t.replace(microsecond=microsecond)
                else:
                    mat = re.match(r"unconverted data remains:"
                                   " \,([0-9]{3,3})$", msg)
                    if mat is not None:
                        # fractional seconds are present - this is the style
                        # used by the logging module
                        frac = "." + mat.group(1)
                        t = t[:-len(frac)]
                        t = datetime.datetime(*time.strptime(t, format)[0:6])
                        microsecond = int(float(frac)*1e6)
                        return t.replace(microsecond=microsecond)

def parseoptions(args):
    """Parses command line options."""
    parser = OptionParser()
    parser.add_option("-d", "--database", default="", type="string",
                      help="The name of the database you want to connect to.")
    parser.add_option("-s", "--server", default="localhost", type="string",
                      help="The name of the server you want to connect to.")
    parser.add_option("-u", "--username", default="", type="string",
                      help="The username to connect to the database with.")
    parser.add_option("-p", "--password", type="string",
                      help="The password to connect to the database with.")
    parser.add_option("-o", "--out", type="string",
                      help="The filename where you want the output stored.")
    return parser.parse_args(args)[0]

if __name__ == '__main__':
    options = parseoptions(sys.argv)
#    exporter = Exporter(options)
#    exporter.export()   

