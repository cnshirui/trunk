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

"""Map CGI argument 'commands' to Image class method calls."""

import re

from google.appengine.api import images


# VALID_TRANSFORMS lists valid parameters and simple regexes to test them.
# The regexes only do very basic sanity checking, not full API validation.
# The match groups are later used to pass the results on to the functions.
VALID_TRANSFORMS = {
    u'crop': re.compile(','.join(['([01]\.[0-9]+)'] * 4)),
    u'horizontal_flip': re.compile('.*$'),
    u'im_feeling_lucky': re.compile('.*$'),
    u'resize': re.compile('([0-9]+),([0-9]+)$'),
    u'rotate': re.compile('(-?[0-9]+0)$'),
    u'vertical_flip': re.compile('.*$')
    }

# _CONVERSION_INFO lists functions to convert the string captured by the
# regex into some other format which the API may require.
_CONVERSION_INFO = {
    u'crop': float,
    u'resize': int,
    u'rotate': int
}


class ImageTransformer(object):
  """ImageTransformer: A wrapper for google.appengine.api.images.

  In a sense, this is an inverse of google.appengine.api.images: it is
  intialized with image data to which multiple transforms are applied. This is
  initialized with a serialized list of transforms with can then be run on
  multiple images.
  """
  
  def __init__(self, params, format):
    """Load and transform image data in a specified manner.

    Args:
       params: UnicodeMultiDict of possible tranformations.
               Typically request.params from a web request.
               Unknown and invalid items will be mostly ignored.
       format: images.JPEG or images.PNG
    """

    self.format = format
    self.transforms = []
    self.unknown_params = []
    self.invalid_params = []
    for (param, value) in params.iteritems():
      if param in VALID_TRANSFORMS:
        if VALID_TRANSFORMS[param].match(value):
          self.transforms.append((param, value))
        else:
          self.invalid_params.append(param)
      else:
        self.unknown_params.append(param)

  def transform_image(self, image_data):
    """Applies this object's list of transforms to an image.
    
    Args:
      image_data: binary image data in JPEG, PNG, or another format which the
                  images.image class can interpret.
    
    Returns:
      Image data transformed using the transforms and formatting specified at
      ImageTransformer initialization time.
                  
    Raises:
      Any of the exceptions which images.Image.execute_transforms can raise.
      
      images.BadRequestError when there is something wrong with the request
        specifications.
      images.NotImageError when the image data given is not an image.
      images.BadImageError when the image data given is corrupt.
      images.LargeImageError when the image data given is too large to process.
      images.TransformtionError when something errors during image manipulation.
      images.Error when something unknown, but bad, happens.
    """
    
    image = images.Image(image_data)
    seen_transforms = set()
    for (transform, param) in self.transforms:
      if transform in seen_transforms:
        # we need to execture all transforms before continuing.
        image_data = image.execute_transforms(self.format)
        image = images.Image(image_data)
        seen_transforms.clear()

      args = VALID_TRANSFORMS[transform].match(param).groups()
      if transform in _CONVERSION_INFO:
        args = [_CONVERSION_INFO[transform](arg) for arg in args]
      getattr(image, transform)(*args)
      seen_transforms.add(transform)

    if seen_transforms:
      image_data = image.execute_transforms(self.format)
    return image_data
