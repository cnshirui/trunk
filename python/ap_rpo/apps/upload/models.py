# coding=UTF-8
#from appengine_django.models import BaseModel
from google.appengine.ext import db
from django.db import models

class UserFile(db.Model):
    name = db.StringProperty()
    mimetype = db.StringProperty()
    comment = db.StringProperty()
    size = db.IntegerProperty()
    author = db.UserProperty()
    creationDate = db.DateTimeProperty(auto_now_add=True)
    icon=db.BlobProperty()

    def delete(self):
        bin = self.filebin_set.get()
        if bin:
            bin.delete()
        super(UserFile, self).delete()
        
    def __unicode__(self):
        return self.name
    
    @models.permalink
    def get_absolute_url(self):
        return ('upload.views.show_albumentry', [str(self.key()),])

class FileBin(db.Model):
    userfile = db.ReferenceProperty(UserFile)
    bin = db.BlobProperty()
    
    def __unicode__(self):
        return self.userfile.name+'_bin'
    
