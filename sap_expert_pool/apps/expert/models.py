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
        return '/expert/%s/' % self.key()
    
    #def search(con1, cond2):
    #    return Expert.gql("WHERE city = :city1 or city = :city2", city1=con1, city2=con2)  
    
class SearchCondition(db.Model):
    con1 = db.StringProperty()
    con2 = db.StringProperty()

    def get_absolute_url(self):
        return '/expert/%s/' % self.key()    