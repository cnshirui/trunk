#!/usr/bin/python2.5
#
# Copyright 2008 Google Inc. All Rights Reserved.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

"""Custom template tags for base.html of the Pabla demo app."""

__author__ = 'Fred Wulff'

from google.appengine.api import users
from google.appengine.ext.webapp import template


# Get the template Library
register = template.create_template_register()


@register.inclusion_tag('user_link.html')
def render_user_link(current_uri):
  """Renders a link that the user can use to log into or out of Google account.

  This is a custom tag since it's included in the header of virtually every
  page served by Pabla, and we don't want to force views to pass the user in
  the template context every time.

  Args:
    current_uri: the uri of the page currently being rendered

  Returns:
    A link that allows the user to log in or out.
  """
  user = users.get_current_user()

  # Note: Here we return a dict that serves at the context for the
  # template indicated in the method decorator.
  if user is None:
    url = users.create_login_url(current_uri)
    return {'authenticated': False,
            'url': url}
  else:
    url = users.create_logout_url(current_uri)
    return {'authenticated': True,
            'username': user.nickname(),
            'url': url}
