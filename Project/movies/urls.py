from django.urls import path
from . import views

app_name = 'movies'

urlpatterns = [
    path('', views.index, name='index'),#URL 별칭
    path('<int:movie_id>/', views.detail, name='detail'),
    path('movie/create/', views.movie_create, name='movie_create'),
    path('movie/modify/<int:movie_id>/', views.movie_modify, name='movie_modify'),
    path('movie/delete/<int:movie_id>/', views.movie_delete, name='movie_delete'),
]
