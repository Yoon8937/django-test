# -*- encoding: utf-8 -*-


from calendar import month
import queue
from time import time
from django import template
from django.contrib.auth.decorators import login_required
from django.http import HttpResponse, HttpResponseRedirect
from django.template import loader
from django.urls import reverse
import folium
from .models import Post, Result, Post2
from django.shortcuts import render
# from image import Post



# @login_required(login_url="/login/")
# def ui_maps(request):
#     map23 = folium.Map(location=[37,126], zoom_start=7)
#     maps = map23._repr_html_()
#     # table_log = Post.objects.all()
#     # context = {'table_log': table_log}

#     return render(request, "home/ui-maps.html", {'maps':maps})

@login_required(login_url="/login/")
def gallery(request):
    gal = Post.objects.all()
    context = {'gal': gal}

    return render(request, "home/ui-icons.html", context)


@login_required(login_url="/login/")
def ui_tables(request):
    table_log = Post.objects.all()
    context = {'table_log': table_log}

    return render(request, "home/ui-tables.html", context)


@login_required(login_url="/login/")
def index(request):
    chart = Post2.objects.all()
    

    #월별 카운트
    jan = chart.filter(time__year = '2022', time__month='01').count()
    feb = chart.filter(time__year = '2022', time__month='02').count()
    mar = chart.filter(time__year = '2022', time__month='03').count()
    apr = chart.filter(time__year = '2022', time__month='04').count()
    may = chart.filter(time__year = '2022', time__month='05').count()
    jun = chart.filter(time__year = '2022', time__month='06').count()
    jul = chart.filter(time__year = '2022', time__month='07').count()
    aug = chart.filter(time__year = '2022', time__month='08').count()
    sep = chart.filter(time__year = '2022', time__month='09').count()
    oct = chart.filter(time__year = '2022', time__month='10').count()
    nov = chart.filter(time__year = '2022', time__month='11').count()
    dec = chart.filter(time__year = '2022', time__month='12').count()

    monthly_chart = [jan, feb, mar, apr, may, jun, jul, aug, sep, oct, nov, dec]
    
    #월별 불법 접수량

    query1 = "select * from result r inner join image_post ip on r.recept_num = ip.id where time >= '2022-01-01' and time < '2022-02-01' and judge=1"
    RS1 = Result.objects.raw(query1)

    query2 = "select * from result r inner join image_post ip on r.recept_num = ip.id where time >= '2022-02-01' and time < '2022-03-01' and judge=1"
    RS2 = Result.objects.raw(query2)
    
    query3 = "select * from result r inner join image_post ip on r.recept_num = ip.id where time >= '2022-03-01' and time < '2022-04-01' and judge=1"
    RS3 = Result.objects.raw(query3)

    query4 = "select * from result r inner join image_post ip on r.recept_num = ip.id where time >= '2022-04-01' and time < '2022-05-01' and judge=1"
    RS4 = Result.objects.raw(query4)

    query5 = "select * from result r inner join image_post ip on r.recept_num = ip.id where time >= '2022-05-01' and time < '2022-06-01' and judge=1"
    RS5 = Result.objects.raw(query5)

    query6 = "select * from result r inner join image_post ip on r.recept_num = ip.id where time >= '2022-06-01' and time < '2022-07-01' and judge=1"
    RS6 = Result.objects.raw(query6)

    query7 = "select * from result r inner join image_post ip on r.recept_num = ip.id where time >= '2022-07-01' and time < '2022-08-01' and judge=1"
    RS7 = Result.objects.raw(query7)

    query8 = "select * from result r inner join image_post ip on r.recept_num = ip.id where time >= '2022-08-01' and time < '2022-09-01' and judge=1"
    RS8 = Result.objects.raw(query8)

    query9 = "select * from result r inner join image_post ip on r.recept_num = ip.id where time >= '2022-09-01' and time < '2022-10-01' and judge=1"
    RS9 = Result.objects.raw(query9)

    query10 = "select * from result r inner join image_post ip on r.recept_num = ip.id where time >= '2022-10-01' and time < '2022-11-01' and judge=1"
    RS10 = Result.objects.raw(query10)

    query11 = "select * from result r inner join image_post ip on r.recept_num = ip.id where time >= '2022-11-01' and time < '2022-12-01' and judge=1"
    RS11 = Result.objects.raw(query11)

    query12 = "select * from result r inner join image_post ip on r.recept_num = ip.id where time >= '2022-12-01' and time < '2022-12-31' and judge=1"
    RS12 = Result.objects.raw(query12)

    monthly_illegal = [len(list(RS1)), len(list(RS2)), len(list(RS3)), len(list(RS4)), len(list(RS5)), len(list(RS6)), len(list(RS7)), len(list(RS8)), len(list(RS9)), len(list(RS10)), len(list(RS11)), len(list(RS12))]
    monthly_legal = [jan-len(list(RS1)), feb-len(list(RS2)) ,mar-len(list(RS3)) ,
    apr-len(list(RS4)) ,may-len(list(RS5))  ,jun-len(list(RS6)) ,jul-len(list(RS7)) ,aug-len(list(RS8)) ,sep-len(list(RS9)) ,oct-len(list(RS10)), nov-len(list(RS11)), dec-len(list(RS12))]
    context = {
        'segment': 'index',
        'monthly_chart': monthly_chart,
        'monthly_illegal': monthly_illegal,
        'monthly_legal' : monthly_legal
    }

    html_template = loader.get_template('home/index.html')
    return HttpResponse(html_template.render(context, request))


@login_required(login_url="/login/")
def pages(request):
    context = {}
    # All resource paths end in .html.
    # Pick out the html file name from the url. And load that template.

    try:

        load_template = request.path.split('/')[-1]

        if load_template == 'admin':
            return HttpResponseRedirect(reverse('admin:index'))
        context['segment'] = load_template

        html_template = loader.get_template('home/' + load_template)
        return HttpResponse(html_template.render(context, request))

    except template.TemplateDoesNotExist:

        html_template = loader.get_template('home/page-404.html')
        return HttpResponse(html_template.render(context, request))

    except:
        html_template = loader.get_template('home/page-500.html')
        return HttpResponse(html_template.render(context, request))

