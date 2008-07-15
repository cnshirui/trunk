from google.appengine.ext import db

class Poll(db.Model):
    question = db.StringProperty()
    created_on = db.DateTimeProperty(auto_now_add = 1)
    created_by = db.UserProperty()

    def __str__(self):
        return '%s' %self.question

    def get_absolute_url(self):
        return '/poll/%s/' % self.key()


class Choice(db.Model):
    poll = db.ReferenceProperty(Poll)
    choice = db.StringProperty()
    votes = db.IntegerProperty(default = 0)

