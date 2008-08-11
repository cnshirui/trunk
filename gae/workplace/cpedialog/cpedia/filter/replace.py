import re
from google.appengine.ext import webapp


register = webapp.template.create_template_register()


@register.filter
def replace ( string, args ):
        search  = args[0]
        replace = args[1]
        return re.sub( search, replace, string )

@register.filter
def email_username ( email ):
        return email.split("@")[0]
