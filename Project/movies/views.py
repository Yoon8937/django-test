from django.shortcuts import render, get_object_or_404, redirect
from .models import Movie_model
from django.utils import timezone
from .forms import MovieForm
from django.contrib import messages




def index(request):
    movie_list = Movie_model.objects.order_by('-create_date')
    context = {'movie_list': movie_list}
    return render(request, 'movies/movie_list.html', context)


def detail(request, movie_id):
    movie = get_object_or_404(Movie_model, pk=movie_id)
    context = {'movie': movie}
    return render(request, 'movies/movie_detail.html', context)


def movie_create(request):
    if request.method == 'POST':
        form = MovieForm(request.POST)
        if form.is_valid():
            movie = form.save(commit=False)
            movie.create_date = timezone.now()
            movie.save()
            return redirect('movies:index')
    else:
        form = MovieForm()
    context = {'form': form}
    return render(request, 'movies/movie_form.html', context)


def movie_modify(request, movie_id):
    movie = get_object_or_404(Movie_model, pk=movie_id)
    if request.method == "POST":
        form = MovieForm(request.POST, instance=movie)
        if form.is_valid():
            movie = form.save(commit=False)
            movie.modify_date = timezone.now()  # 수정일시 저장
            movie.save()
            return redirect('movies:detail', movie_id=movie.id)
    else:
        form = MovieForm(instance=movie)
    context = {'form': form}
    return render(request, 'movies/movie_form.html', context)


def movie_delete(request, movie_id):
    movie = get_object_or_404(Movie_model, pk=movie_id)
    movie.delete()
    return redirect('movies:index')
