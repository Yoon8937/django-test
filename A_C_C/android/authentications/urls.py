from django.urls import path
from .views import login_view, register_user


urlpatterns = [
	path('register/', register_user, name='register'),
	path('login/', login_view, name='login'),
 ]
