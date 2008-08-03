from django import newforms as forms
import models
from google.appengine.ext.db import djangoforms

class ExpertForm(djangoforms.ModelForm):
    class Meta:    
        model = models.Expert

class SearchForm(djangoforms.ModelForm):
    class Meta:    
        model = models.SearchCondition