from django.db import models


class Movie_model(models.Model):
    movie_subject = models.CharField(max_length=200)
    movie_content = models.TextField()
    create_date = models.DateTimeField()

    def __str__(self):
        return self.movie_subject
