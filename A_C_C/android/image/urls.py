from django.urls import path
from . import views

app_name = 'image'

urlpatterns = [
    path('image/', views.PostCreateAPIView.as_view(), name='postcreate'),
]
