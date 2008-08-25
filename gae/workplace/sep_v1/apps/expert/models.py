from google.appengine.ext import db

class Expert(db.Model):
    name = db.StringProperty()
    birthday = db.StringProperty()
    gender = db.StringProperty(default = 'Male', choices = ['Male', 'Female'])
    nationality = db.StringProperty()
    city = db.StringProperty()
    sap_experience = db.StringProperty()
    manager = db.StringProperty()
    
    def __str__(self):
        return '%s' %self.name

    def get_absolute_url(self):
        return '/show/%s/' % self.key()
    
class SearchCondition(db.Model):
    knowledge_area = db.StringProperty()
#    con2 = db.StringProperty()

    def get_absolute_url(self):
        return '/expert/%s/' % self.key()    