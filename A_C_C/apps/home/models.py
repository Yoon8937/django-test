from pyexpat import model
from django.db import models
from django.contrib.auth.models import User



class Post2(models.Model):

    user = models.IntegerField(db_column='userid')
    lat = models.FloatField(db_column='lat', max_length=50)
    lng = models.FloatField(db_column='lng', max_length=50)
    time = models.DateTimeField(db_column='time')
    model_pic = models.CharField(db_column='model_pic', max_length=200)
    #type = models.ForeignKey(User, models.DO_NOTHING, db_column='draft_user')
    #job = models.DateTimeField(blank=True, null=True)
    #age = models.DateTimeField(blank=True, null=True)

    class Meta:
        db_table = 'image_post'
        managed = False

    def __str__(self):
        return "제목 : " + self.name + ", 유형 : " + self.type




class Post(models.Model):
    
    user = models.IntegerField(db_column='userid')
    lat = models.FloatField(db_column='lat', max_length=50)
    lng = models.FloatField(db_column='lng', max_length=50)
    time = models.DateTimeField(db_column='time')
    model_pic = models.CharField(db_column='model_pic', max_length=200)
    plate_num = models.CharField(db_column='plate_num', max_length=200)
    #name = models.TextField()
    #type = models.ForeignKey(User, models.DO_NOTHING, db_column='draft_user')
    #job = models.DateTimeField(blank=True, null=True)
    #age = models.DateTimeField(blank=True, null=True)

    class Meta:
        db_table = 'image_picpost'
        managed = False

    def __str__(self):
        return "제목 : " + self.name + ", 유형 : " + self.type



class Result(models.Model):

    recepts_num = models.IntegerField(db_column='recept_num')
    judge = models.IntegerField(db_column='judge')
#    car_num = models.FloatField(db_column='car_num')
    
    class Meta:
        db_table = 'result'
        managed = False

    def __str__(self):
        return "제목 : "  + self.name + ", 유형 : " + self.type
		

class Results(models.Model):
    recepts_num = models.IntegerField(db_column='recept_num')
    judge = models.IntegerField(db_column='judge')
    time = models.DateTimeField(auto_now_add=True)

#    car_num = models.FloatField(db_column='car_num')
