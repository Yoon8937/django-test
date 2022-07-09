from django.contrib import admin
from .models import Movie_model

class Movie_modelAdmin(admin.ModelAdmin):
    search_fields = ['movie_subject']



admin.site.register(Movie_model, Movie_modelAdmin)

# Register your models here.
