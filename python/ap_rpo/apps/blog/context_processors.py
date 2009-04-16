def twitter(request):
    from django.conf import settings
    return {'TWITTER_ID': settings.TWITTER_ID or 21535364,'TWITTER_USERNAME':settings.TWITTER_USERNAME or 'iHereBlog'}
