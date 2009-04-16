from django import newforms as forms
import models
from google.appengine.ext.db import djangoforms

class PollForm(djangoforms.ModelForm):
    class Meta:
        model = models.Poll
        exclude = ['created_by']

class ChoiceForm(forms.Form):
    choice = forms.CharField(max_length = 100)

    def __init__(self, poll=None, *args, **kwargs):
        self.poll = poll
        super(ChoiceForm, self).__init__(*args, **kwargs)

    def save(self):
        choice = models.Choice(poll = self.poll, choice = self.clean_data['choice'])
        choice.put()
