from functools import wraps
#from django.core.cache import cache
from google.appengine.api import memcache as cache
from google.appengine.ext import db
import logging
#http://adam.gomaa.us/blog/2008/aug/11/the-python-property-builtin/
def cached_property(func):
    @wraps(func)
    def _closure(self):
        cache_key = "%s.%s.%s(%s)" % (  self.__class__.__module__,
                                        self.__class__.__name__,
                                        func.__name__,
                                        self.key)
        logging.info('cache_key=%s'%cache_key)
        val = cache.get(cache_key)

        if val is None:
            val = func(self)
            cache.set(cache_key, val)

        return val

    return property(func) 

#Base Model for signals:pre_save, post_save          
class SignalsBaseModel(db.Model):
    def put(self,created=False):  
        created=not self.is_saved()      
        signals.pre_save.send(sender=self.__class__, instance=self) 
        key=super(SignalsBaseModel,self).put()
        signals.post_save.send(sender=self.__class__, instance=self,created=created)        
        return key 
    #override put method in case signals not sent 
    #when using google.appengine.ext.db import djangoforms.save(commit=True)

    save=put

    def delete(self):
        signals.pre_delete.send(sender=self.__class__, instance=self)
        super(db.Model,self).delete()
        signals.post_delete.send(sender=self.__class__, instance=self)