from django import forms
from movies.models import Movie_model


class MovieForm(forms.ModelForm):
    class Meta:
        model = Movie_model  # 사용할 모델
        fields = ['movie_subject', 'movie_content'] 
