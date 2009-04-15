import os
import md5
import re
import urllib
import datetime
import math

import simplejson

from elementtree.ElementTree import *

from google.appengine.api import urlfetch
from google.appengine.api import users
from google.appengine.ext import webapp
from google.appengine.ext.webapp import template
from google.appengine.ext import db
import wsgiref.handlers

MONTHS = ['JAN', 'FEB', 'MAR', 'APR', 'MAY', 'JUN', 'JUL', 'AUG', 'SEP', 'OCT', 'NOV', 'DEC']

class MainPage(webapp.RequestHandler):
  def get(self):

    user = users.GetCurrentUser()
    login = users.CreateLoginURL(self.request.uri)
    logout = users.CreateLogoutURL(self.request.uri)

    template_file_name = 'mainpage.html'
    template_values = {'login': login, 'logout': logout, 'user': user}

    path = os.path.join(os.path.dirname(__file__), template_file_name)
    self.response.out.write(template.render(path, template_values))

class ReviewPage(webapp.RequestHandler):
  def get(self):
    user = users.GetCurrentUser()
    login = users.CreateLoginURL(self.request.uri)
    logout = users.CreateLogoutURL(self.request.uri)

    template_file_name = 'reviewpage.html'
    template_values = {'login': login, 'logout': logout, 'user': user}

    path = os.path.join(os.path.dirname(__file__), template_file_name)
    self.response.out.write(template.render(path, template_values))


class SubmitReview(webapp.RequestHandler):
  def post(self):
    json = self.request.get('json')

    json = simplejson.loads(json)

    movie_review = MovieReview()
    movie_review.comment = json['reviewComment']
    movie_review.title = json['title']
    movie_review.movie = getMovie(movie_review.title)
    movie_review.author = users.GetCurrentUser()

    movie_review.put() 

class Movie(db.Model):
  title = db.StringProperty()
  picture = db.BlobProperty(default=None)
  full_content = db.TextProperty(default=None)
  summary = db.TextProperty(default=None)
  date = db.DateTimeProperty(auto_now_add=True)
  release_date = db.DateTimeProperty(default=None)
  rating_average = db.FloatProperty(default=0.0)
  rating_count = db.IntegerProperty(default=0)

class MovieReview(db.Model):
  title = db.StringProperty()
  movie = db.Reference(Movie)
  author = db.UserProperty()
  comment = db.TextProperty()
  date = db.DateTimeProperty(auto_now_add=True)

class GetImage(webapp.RequestHandler):
  
  def get(self):

    title = self.request.get('title')

    movie = getMovie(title)

    if (movie and movie.picture):
      self.response.headers['Content-Type'] = 'image/jpg'
      self.response.out.write(movie.picture)
    else:
     self.redirect('/static/noimage.jpg')

class SetRating(webapp.RequestHandler):
  def post(self):
    title =self.request.get('title')
    rating = float(self.request.get('rating'))
    movie = getMovie(title)
    movie.rating_average = (movie.rating_average * movie.rating_count +
      rating) / (movie.rating_count + 1)
    movie.rating_count = movie.rating_count + 1
    movie.put()

class GetReviewsJson(webapp.RequestHandler):
  def get(self):

    title = self.request.get('title')
    movie = getMovie(title)
    json_obj = {}

    json_obj['movie'] = {}
    json_obj['reviews'] = []

    json_obj['movie']['title'] = movie.title
    json_obj['movie']['summary'] = movie.summary
    json_obj['movie']['release_date'] = movie.release_date.ctime() 
    json_obj['movie']['pic'] = 'image?' + urllib.urlencode({'title': movie.title})
    json_obj['movie']['rating_average'] = movie.rating_average
    json_obj['movie']['rating_count'] = movie.rating_count

    results = db.GqlQuery("SELECT * FROM MovieReview WHERE title = :1 ORDER BY date ASC", 
        title)
    
    for result in results:
      review = {}
      review['title'] = result.title
      review['author'] = result.author.nickname()
      review['comment'] = result.comment
      review['date'] = result.date.ctime()

      json_obj['reviews'].append(review)

    json_str = simplejson.dumps(json_obj)

    self.response.headers['Content-Type'] = 'text/javascript'
    self.response.out.write(json_str)

class GetMoviesJson(webapp.RequestHandler):
  def get(self):
    sortby = self.request.get('sortby')
    page = self.request.get('page')
    
    if (not sortby):
      sortby = 'release_date'

    if (not page):
      page = 1
    else:
      page = int(page)

    results = db.GqlQuery("SELECT * FROM Movie ORDER BY %s ASC" % sortby)

    item_per_page = 16

    start_index = (page - 1) * item_per_page

    total_items = results.count()

    results = results.fetch(item_per_page, start_index)

    json_obj = {}

    json_obj['total'] = total_items

    json_obj['movies'] = []

    for result in results:
      movie = {}
      movie['title'] = result.title
      #movie['summary'] = result.summary
      movie['release_date'] = result.release_date.ctime()
      movie['pic'] = 'image?' + urllib.urlencode({'title': result.title})
      

      json_obj['movies'].append(movie)

    json_str = simplejson.dumps(json_obj)

    self.response.headers['Content-Type'] = 'text/javascript'
    self.response.out.write(json_str)


def getMovie(title):
  result = db.GqlQuery("SELECT * FROM Movie WHERE title = :1 LIMIT 1",
    title).fetch(1)
  
  if (len(result) > 0):
    return result[0]
  else:
    return None

class DeleteAll(webapp.RequestHandler):
  def get(self):
    results = db.GqlQuery("SELECT * FROM Movie")
    for result in results:
      result.delete()
    results = db.GqlQuery("SELECT * FROM MovieReview")
    for result in results:
      result.delete()
      

class Build(webapp.RequestHandler):

  def get(self):
      
    build_only = self.request.get('buildonly')

    if (not build_only):
      build_only = False
    else:
      build_only = True

    rss1 = 'http://movies.go.com/xml/rss/intheaters.xml'    
    rss2 = 'http://movies.go.com/xml/rss/upcoming.xml'    
    
    movies = self.getMoviesFromRss(rss1, build_only)

    new_movies = movies['new']
    update_movies = movies['update']

    movies = self.getMoviesFromRss(rss2, build_only)

    new_movies.extend(movies['new'])
    update_movies.extend(movies['update'])

    self.response.headers['Content-Type'] = 'text/html'
    self.response.out.write('added %d new movie(s)<br>' % (len(new_movies),))
    self.response.out.write(str(new_movies) + '<br><br>')
    self.response.out.write('updated %d existing  movie(s)<br>' % (len(update_movies),))
    self.response.out.write(str(update_movies) + '<br>')
  
  def getMoviesFromRss(self, url, build_only=False):

    rss = urlfetch.Fetch(url)
          
    tree = ElementTree(fromstring(rss.content))
    
    img_re = re.compile('<img src="([a-zA-Z0-9/:._\-]+)" ')
    content_re = re.compile('&#0151; \n(.+)')

    release_re = re.compile('Release:</strong> ([a-zA-Z]{3,4})\. ([0-9]{1,2}), ([0-9]{4})')

    new_movies = []
    update_movies = []

    for item in tree.findall('.//item'):
      title = item.find('title').text.strip()

      is_new = True

      movie = getMovie(title)

      if (not movie):
        movie = Movie()
      else:
        is_new = False
        if (build_only):
          continue

      description = item.find('description').text.strip()
      summary = (content_re.search(description)).groups()[0]

      release_match = (release_re.search(description))

      imageMatch = img_re.search(description)

      movie.title = title
      movie.full_content = description
      movie.summary = db.Text(summary)
      
      if (release_match):
        release_month = MONTHS.index(release_match.groups()[0].upper()) + 1
        release_date = int(release_match.groups()[1])
        release_year = int(release_match.groups()[2])

        movie.release_date = datetime.datetime(release_year, release_month, release_date)

      if (imageMatch):          
        picture_url = imageMatch.groups()[0]
        movie.picture = db.Blob(urlfetch.Fetch(picture_url).content)
      
      movie.put()
      
      if (is_new):
        new_movies.append(title)
      else:
        update_movies.append(title)

    movies = {}
    movies['new'] = new_movies
    movies['update'] = update_movies

    return movies

class Test(webapp.RequestHandler):
  def get(self):
    title = self.request.get('title')
    movie = getMovie(title)
    if (not movie):
      title = 'None'
    else:
      title = movie.title
    self.response.headers['Content-Type'] = 'text/html'
    self.response.out.write('Movie title: %s' % title)
    self.response.out.write('<br/>')


apps_binding = []

apps_binding.append(('/', MainPage))
apps_binding.append(('/test', Test))
apps_binding.append(('/review', ReviewPage))
apps_binding.append(('/reviews', GetReviewsJson))
apps_binding.append(('/movies', GetMoviesJson))
apps_binding.append(('/image', GetImage))
apps_binding.append(('/submitreview', SubmitReview))
apps_binding.append(('/deleteall', DeleteAll))
apps_binding.append(('/build', Build))
apps_binding.append(('/rating', SetRating))

application = webapp.WSGIApplication(apps_binding, debug=True)
wsgiref.handlers.CGIHandler().run(application)
