
import datamodel
from google.appengine.ext import db

"""Helper functions for managing user settings.

  get(): Returns the settings for a specified user.
  new(): Initializes and inserts a new user.
"""


def get(user):
  """Returns the settings for a specified user.
  
  Args:
    user: The user object to look up.
    
  Returns:
    Either a datamodel.Settings instance, if found. Returns None otherwise.
  """
  query = db.Query(datamodel.Settings)
  query.filter('user =', user)
  results = query.fetch(1)
  if len(results) == 0:
    return None
  else:
    return results[0]
  
def new(user, default_location = ''):
  """Initializes and inserts a new user.
  
  Args:
    user: The user object to create settings for.
    default_location: Optionally specifies the user's starting location as a
      string.
  """
  user_settings = datamodel.Settings()
  user_settings.user = user
  if default_location:
    user_settings.default_location = str(default_location)
  user_settings.put()
  return user_settings