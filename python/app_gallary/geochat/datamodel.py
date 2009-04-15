
# Copyright 2008 Google Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
# 
#     http://www.apache.org/licenses/LICENSE-2.0
# 
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

from google.appengine.ext import db

"""Database models used in the Geochat application.

  ChatEvent: Stores user chat events.
  MoveEvent: Stores user movement events.
  Settings: Stores various user settings not stored in the actual users API.
"""

class ChatEvent(db.Model):
  timestamp = db.DateTimeProperty(auto_now_add = True)
  user = db.UserProperty()
  contents = db.StringProperty()
  latitude = db.FloatProperty()
  longitude = db.FloatProperty()
  zoom = db.IntegerProperty()


class Settings(db.Model):
  user = db.UserProperty()
  default_location = db.StringProperty(default = 'Mountain View, CA')
  default_zoom = db.IntegerProperty(default = 13)


