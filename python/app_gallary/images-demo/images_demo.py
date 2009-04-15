#!/usr/bin/python2.5
#
# Copyright 2008 Google Inc.
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


"""Demo for the Google App Engine images API.

Provides a simple way to apply transforms to canned images.

This file contains all request handlers.
"""

import os
import urllib
import wsgiref.handlers

from google.appengine.api import images
from google.appengine.ext import webapp
from google.appengine.ext.webapp import template

import image_transformer


class TemplatedPage(webapp.RequestHandler):
  """Base class for templatized handlers."""
  
  def write_template(self, values):
    """Write out the template with the same name as the class name."""
    path = os.path.join(os.path.dirname(__file__), 'templates',
                        self.__class__.__name__ + '.html')
    self.response.out.write(template.render(path, values))


class MainPage(TemplatedPage):
  """Request handler for main index page."""

  def get(self):
    """Handle GET request."""
    imagelist = os.listdir(os.path.join(os.path.dirname(__file__), 'samples'))
    
    values = {
        'imagelist': imagelist,
        'title': 'App Engine Image Demo'
    }

    self.write_template(values)


class ImagePage(TemplatedPage):
  """Request handler for image information display page."""
  
  def get(self):
    """Handle GET request."""
    image = self.request.get('image')
    if not image:
      # TODO: More friendly error.
      self.error(400)
      return

    # Transformer is used here only to validate parameters.
    transformer = image_transformer.ImageTransformer(self.request.params,
                                                     images.JPEG)
    transformer.unknown_params.remove('image')
    # format is not really a transform.
    while 'format' in transformer.unknown_params:
      transformer.unknown_params.remove('format')

    transform_args = urllib.urlencode(transformer.transforms).replace('%2C',',')
    if (self.request.get('format') == 'png'
        or self.request.get('format') == 'jpeg'):
      transform_args += '&format=%s' % (self.request.get('format'))
      
    documentation = {}
    for (method, _) in image_transformer.VALID_TRANSFORMS.iteritems():
      documentation[method] = getattr(images.Image, method).__doc__
    
    values = {
        'documentation': documentation,
        'image': image,
        'title': 'App Engine Image Demo',
        'transformer': transformer,
        'transform_args': transform_args,
    }

    self.write_template(values)


class SamplePage(TemplatedPage):
  """Request handler for image transform sample page."""
  
  def get(self):
    """Handle GET request."""
    image = self.request.get('image')
    if not image:
      # TODO: More friendly error.
      self.error(400)
      return

    transform_list = [
        'resize=200,200',
        'im_feeling_lucky&resize=200,200',
        'rotate=90&resize=200,200',
        'rotate=180&resize=200,200',
        'rotate=360&resize=200,200',
        'resize=50,50',
        'resize=50,50&resize=200,200',
        'resize=200,200&crop=0.0,0.0,0.5,0.5',
        'crop=0.5,0.0,1.0,0.5&resize=200,200',
        'horizontal_flip&resize=200,200',
        'vertical_flip&resize=200,200',
    ]
    
    values = {
        'image': image,
        'title': 'App Engine Image Demo',
        'transform_list': transform_list,
    }
    self.write_template(values)


class ViewSource(TemplatedPage):
  """Page handler for view source page."""
  
  def get(self):
    """Handle GET request.
    
    An extremely simple implementation of view source using a template.
    """
    files = ['images_demo.py', 'image_transformer.py']
    file_contents = []
    for filename in files:
      f = open(filename, 'r')
      file_contents += [{'name': filename,
                         'contents': f.read()}]
      f.close()

    values = {
        'filecontents': file_contents,
        'title': 'App Engine Image Demo Source Code',
    }
    self.write_template(values)


class ImageData(webapp.RequestHandler):
  """Request handler for image binary data output."""

  def load_local_sample_image(self, filename):
    """Load a sample image from a (local to this script) filename."""
    path = os.path.join(os.path.dirname(__file__), 'samples', filename)
    f = open(path, 'rb')
    data = f.read()
    f.close()
    return data

  def get(self):
    """Handle image data GET."""
    image = self.request.get('image')
    if not image:
      # TODO: More friendly error.
      self.error(400)
      return
      
    format = images.JPEG
    if self.request.get('format') == 'png':
      format = images.PNG

    data = self.load_local_sample_image(image)

    transformer = image_transformer.ImageTransformer(self.request.params,
                                                     format)
    data = transformer.transform_image(data)

    if format == images.PNG:
      self.response.headers['Content-Type'] = 'image/png'
    else:
      self.response.headers['Content-Type'] = 'image/jpeg'
    
    self.response.out.write(data)


def main():
  application = webapp.WSGIApplication([('/', MainPage),
                                        ('/tryit', ImagePage),
                                        ('/imagedata', ImageData),
                                        ('/sample', SamplePage),
                                        ('/viewsource', ViewSource),
                                       ],
                                       debug=True)
  wsgiref.handlers.CGIHandler().run(application)

if __name__ == '__main__':
  main()
