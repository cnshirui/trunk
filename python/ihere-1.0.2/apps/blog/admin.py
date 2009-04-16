from django.contrib import admin
from models import *
admin.site.register(Tag)
admin.site.register(Category)
admin.site.register(Comment)
admin.site.register(Menu)

class PostAdmin(admin.ModelAdmin):    
    list_display = ('date','title', )
    fields = ('title','content','date','author','authorEmail', 'category','slug','lastCommentedDate','visitcount','lastModifiedDate','lastModifiedBy','entryType','isPublished',)

admin.site.register(Post, PostAdmin)

class LinkAdmin(admin.ModelAdmin):    
    list_display = ('description', )

admin.site.register(Link,LinkAdmin)