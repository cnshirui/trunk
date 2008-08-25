from google.appengine.ext.db import djangoforms

import models

class ExpertForm(djangoforms.ModelForm):
    class Meta:    
        model = models.Expert

class SearchForm(djangoforms.ModelForm):
    class Meta:    
        model = models.SearchCondition