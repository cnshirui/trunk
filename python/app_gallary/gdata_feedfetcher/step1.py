# Copyright (C) 2008 Google Inc.
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


__author__ = 'api.jscudder (Jeffrey Scudder)'


import wsgiref.handlers
from google.appengine.ext import webapp
import gdata.service


# Change the value of HOST_NAME to the name given to point to your app.
HOST_NAME = 'gdata-feedfetcher.appspot.com'


class Fetcher(webapp.RequestHandler):
  def get(self):
     # Instantiate an instance of the GDataService()
     client = gdata.service.GDataService()
       
     authsub_next_url = ('http://%s/step1'
         '?token_scope=http://docs.google.com/feeds/' % HOST_NAME)
    
     # Generate the AuthSub URL and write a page that includes the link
     self.response.out.write("""<html><body>
         <a href="%s">Request token for the Google Documents Scope</a>
         </body></html>""" % client.GenerateAuthSubURL(authsub_next_url,
             'http://docs.google.com/feeds/', secure=False, session=True))


def main():
  application = webapp.WSGIApplication([('/.*', Fetcher),], debug=True)
  wsgiref.handlers.CGIHandler().run(application)

if __name__ == '__main__':
  main()
