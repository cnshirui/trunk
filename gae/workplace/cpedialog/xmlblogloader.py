from google.appengine.api import urlfetch
from xml.etree import ElementTree

WEATHER_URL = 'http://xml.weather.yahoo.com/forecastrss?p=%s'
WEATHER_NS = 'http://xml.weather.yahoo.com/ns/rss/1.0'


def parse( url ) :
   result = urlfetch.fetch(url)
   if result.status_code == 200:
           parser = ElementTree.XMLTreeBuilder()
           parser.feed(result.content)
           return parser.close()


def weather_for_zip(zip_code):
    url = WEATHER_URL % zip_code
    rss = parse(url)
    forecasts = []
    for element in rss.findall('channel/item/{%s}forecast' %WEATHER_NS):
        forecasts.append({
            'date': element.get('date'),
            'low': element.get('low'),
            'high': element.get('high'),
            'condition': element.get('text')
        })
    ycondition = rss.find('channel/item/{%s}condition' % WEATHER_NS)
    return {
        'current_condition': ycondition.get('text'),
        'current_temp': ycondition.get('temp'),
        'forecasts': forecasts,
        'title': rss.findtext('channel/title')
    }


#print 'Content-Type: text/plain'
#print ''
#print weather_for_zip('94089')



def get_blog_list(xmlfile):
    root = ElementTree.parse(xmlfile).getroot()
    bloglist = []
    for element in root.findall('weblog'):
        bloglist.append({
            'id': element.findtext('id'),
            'title': element.findtext('title'),
            'text': element.findtext('text'),
            'date': element.findtext('date')
        })
    return bloglist

def get_blog_reaction_list(xmlfile):
    root = ElementTree.parse(xmlfile).getroot()
    reactionlist = []
    for element in root.findall('weblog_reactions'):
        reactionlist.append({
            'id': element.findtext('weblog'),
            'user': element.findtext('user'),
            'text': element.findtext('text'),
            'ip': element.findtext('ip'),
            'date': element.findtext('date')
        })
    return reactionlist

