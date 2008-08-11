from google.appengine.ext import webapp
import util
import string


register = webapp.template.create_template_register()


@register.filter
def gravatar(email):
        return util.getGravatarUrl(email)
