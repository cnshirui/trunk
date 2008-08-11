__author__ = 'Ping Chen'

blog = {
    "title": "Shi Rui's Blog",
    "author": "Rui Shi",
    "email": "shiruide@gmail.com",   # This must be the email address of a registered administrator for the application due to mail api restrictions.
    "description": "Shi Rui's Blog powered by Google AppEngine.",
    "root_url": "http://www.whohar.com",
    "cache_time": 0,      # You can override this default for each page through a handler's call to view.ViewPage(cache_time=...)
}

album ={
    "username":["cpedia","yeli.piao","pegina8"]
}

delicious = {
    "username":"cnshirui"    #del.icio.us user name.
}

google_docs = {
    "username":"shiruide@gmail.com"
}

google_calendar = {
    "username":"shiruide@gmail.com"
}

logo_images = ["logo1.gif","logo2.gif"]  #make sure the images located in the folder img/logo.

# Set to true if we want to have our webapp print stack traces, etc
_DEBUG = True

#entity num of per page.
_NUM_PER_PAGE = 8
