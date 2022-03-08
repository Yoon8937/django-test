from django.shortcuts import render
from django.contrib.auth import authenticate, login
from .forms import LoginForm, SignUpForm
from django.http import HttpResponse, JsonResponse
from django.contrib.auth.models import User


def login_view(request):
    form = LoginForm(request.POST or None)

    msg = None

    if request.method == "POST":
        if form.is_valid():
            username = form.cleaned_data.get("username")
            password = form.cleaned_data.get("password")
            user = authenticate(username=username, password=password)
            if user is not None:
                login(request, user)
                id = User.objects.get(username = request.user.get_username()).id
                print(id)

                return JsonResponse({'hello': id})
            else:
                return JsonResponse({'hello': -1})
        else:
            pass
#           return HttpResponse(status=301)

def register_user(request):

    if request.method == "POST":
        form = SignUpForm(request.POST)
        if form.is_valid():
            form.save()
            username = form.cleaned_data.get("username")
            raw_password = form.cleaned_data.get("password1")
            user = authenticate(username=username, password=raw_password)

            return HttpResponse(status=200)
        else:
            pass
            return HttpResponse(status=301)
    else:
        form = SignUpForm()
