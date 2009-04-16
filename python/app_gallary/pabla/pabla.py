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

"""Pabla, a simple picture sharing application running on App Engine.

Specific App Engine features demonstrated include:
 * the Image API, which is used for creating thumbnails of uploaded pictures
 * the Datastore API, which is used to store both the metadata for and
   the actual content of pictures
 * the ListProperty type, which is used for efficient tagging support, and
 * support for file uploads using the WebOb request object

We use the webapp.py WSGI framework and the Django templating
language, both of which are documented in the App Engine docs
(http://appengine.google.com/docs/).
"""

__author__ = 'Fred Wulff'


import cgi
import os

from google.appengine.api import images
from google.appengine.api import users
from google.appengine.ext import db
from google.appengine.ext import webapp
from google.appengine.ext.webapp import template

import wsgiref.handlers


# Makes the tags defined by templatetags/basetags.py usable
# by templates rendered in this file
template.register_template_library('templatetags.basetags')


class Album(db.Model):
  """An album is an organizational unit for pictures.

  Properties:
    name: sanitized, user entered name for the album
    creator: Google Account of the person who created the album
    created_date: DateTime the album was created
  """

  name = db.StringProperty()
  creator = db.UserProperty()
  created_date = db.DateTimeProperty(auto_now_add=True)


class Picture(db.Model):
  """Storage for a picture and its associated metadata.

  Properties:
    submitter: Google Account of the person who submitted the picture
    submitted_date: DateTime the picture was submitted
    title: sanitized, user entered title for the picture
    caption: sanitized, user entered caption for the picture
    album: reference to album the picture is in
    tags: a StringListProperty of tags for the picture
    data: data for the original picture, converted into png format
    thumbnail_data: png format data for the thumbnail for this picture
  """

  submitter = db.UserProperty()
  submitted_date = db.DateTimeProperty(auto_now_add=True)
  title = db.StringProperty()
  caption = db.StringProperty(multiline=True)
  album = db.ReferenceProperty(Album, collection_name='pictures')
  tags = db.StringListProperty()
  data = db.BlobProperty()
  thumbnail_data = db.BlobProperty()


class PablaBaseHandler(webapp.RequestHandler):
  """Base Pabla RequestHandlers with some convenience functions."""

  def template_path(self, filename):
    """Returns the full path for a template from its path relative to here."""
    return os.path.join(os.path.dirname(__file__), filename)

  def render_to_response(self, filename, template_args):
    """Renders a Django template and sends it to the client.

    Args:
      filename: template path (relative to this file)
      template_args: argument dict for the template
    """
    template_args.setdefault('current_uri', self.request.uri)
    self.response.out.write(
        template.render(self.template_path(filename), template_args)
    )


class PablaAlbumIndex(PablaBaseHandler):
  """Handler for listing albums."""

  def get(self):
    """Lists all available albums."""
    albums = Album.all().order('-created_date')
    self.render_to_response('index.html', {
        'albums': albums,
      })


class PablaAlbumCreate(PablaBaseHandler):
  """Handler for creating a new Album via form."""

  def get(self):
    """Displays the album creation form."""
    self.render_to_response('new.html', {})

  def post(self):
    """Processes an album creation request."""
    Album(name=cgi.escape(self.request.get('albumname')),
          creator=users.get_current_user()).put()
    self.redirect('/')

PICTURES_PER_ROW = 5

class PablaAlbumView(PablaBaseHandler):
  """Handler for viewing the pictures in a particular album."""

  def get(self, album_key):
    """Displays a single album.

    Note that in this and later handlers, the args come
    from a capturing group in the WSGIApplication specification.
    See the webapp framework docs for more info.

    Args:
      album_key: the datastore key for the Album to view.
    """
    album = db.get(album_key)

    pics = []
    num_results = 0

    for picture in album.pictures:
      if num_results % PICTURES_PER_ROW == 0:
        current_row = []
        pics.append(current_row)
      current_row.append(picture)
      num_results += 1

    self.render_to_response('album.html', {
        'num_results': num_results,
        'album_key': album.key(),
        'pics': pics,
        'album_name': album.name
      })


class PablaUploadImage(PablaBaseHandler):
  """Handler for uploading images."""

  def get(self, album_key):
    """Display the image upload form.

    Args:
      album_key: datastore key for the album to upload the image to
    """
    album = db.get(album_key)
    self.render_to_response('upload.html', {
        'album_key': album.key(),
        'album_name': album.name
      })

  def post(self, album_key):
    """Process the image upload form.

    We also generate the thumbnail for the picture at this point.

    Args:
      album_key: datastore key for the album to add the image to
    """
    album = db.get(album_key)
    if album is None:
      self.error(400)
      self.response.out.write('Couldn\'t find specified album')

    title = cgi.escape(self.request.get('title'))
    caption = cgi.escape(self.request.get('caption'))
    tags = cgi.escape(self.request.get('tags')).split(',')
    tags = [tag.strip() for tag in tags]
    # Get the actual data for the picture
    img_data = self.request.POST.get('picfile').file.read()

    try:
      img = images.Image(img_data)
      # Basically, we just want to make sure it's a PNG
      # since we don't have a good way to determine image type
      # through the API, but the API throws an exception
      # if you don't do any transforms, so go ahead and use im_feeling_lucky.
      img.im_feeling_lucky()
      png_data = img.execute_transforms(images.PNG)

      img.resize(60, 100)
      thumbnail_data = img.execute_transforms(images.PNG)

      Picture(submitter=users.get_current_user(),
              title=title,
              caption=caption,
              album=album,
              tags=tags,
              data=png_data,
              thumbnail_data=thumbnail_data).put()

      self.redirect('/album/%s' % album.key())
    except images.BadImageError:
      self.error(400)
      self.response.out.write(
          'Sorry, we had a problem processing the image provided.')
    except images.NotImageError:
      self.error(400)
      self.response.out.write(
          'Sorry, we don\'t recognize that image format.'
          'We can process JPEG, GIF, PNG, BMP, TIFF, and ICO files.')
    except images.LargeImageError:
      self.error(400)
      self.response.out.write(
          'Sorry, the image provided was too large for us to process.')

class PablaShowImage(PablaBaseHandler):
  """Handler for viewing a single image.

  Note that this doesn't actually serve the picture, only the page
  containing it. That happens in PablaServeImage.
  """

  def get(self, pic_key):
    """Renders the page for a single picture.

    Args:
      pic_key: key for the Picture model for the picture to display
    """

    pic = db.get(pic_key)
    self.render_to_response('show_image.html', {
        'pic': pic,
        'image_key': pic.key(),
    })


class PablaServeImage(webapp.RequestHandler):
  """Handler for dynamically serving an image from the datastore.

  Very simple - it just pulls the appropriate data out of the datastore
  and serves it.
  """

  def get(self, display_type, pic_key):
    """Dynamically serves a PNG image from the datastore.

    Args:
      type: a string describing the type of image to serve (image or thumbnail)
      pic_key: the key for a Picture model that holds the image
    """
    image = db.get(pic_key)

    if display_type == 'image':
      self.response.headers['Content-Type'] = 'image/png'
      self.response.out.write(image.data)
    elif display_type == 'thumbnail':
      self.response.headers['Content-Type'] = 'image/png'
      self.response.out.write(image.thumbnail_data)
    else:
      self.error(500)
      self.response.out.write(
          'Couldn\'t determine what type of image to serve.')

class PablaSearch(PablaBaseHandler):
  """Handler for searching pictures by tag."""

  def get(self):
    """Displays the tag search box and possibly a list of results."""
    query = cgi.escape(self.request.get('q'))
    pics = []
    if query:
      # ListProperty magically does want we want: search for the occurrence
      # of the term in any of the tags.
      pics = Picture.all().filter('tags =', query)
    else:
      query = ''
    self.render_to_response('search.html', {
        'query': query,
        'pics': pics,
      })


def main():
  url_map = [('/', PablaAlbumIndex),
             ('/new', PablaAlbumCreate),
             ('/album/([-\w]+)', PablaAlbumView),
             ('/upload/([-\w]+)', PablaUploadImage),
             ('/show_image/([-\w]+)', PablaShowImage),
             ('/(thumbnail|image)/([-\w]+)', PablaServeImage),
             ('/search', PablaSearch)]
  application = webapp.WSGIApplication(url_map,
                                       debug=True)
  wsgiref.handlers.CGIHandler().run(application)

if __name__ == '__main__':
  main()
