from django.db import models

# plt_num = plate_detection(
class PicPost(models.Model):
    model_pic = models.ImageField(upload_to='image/%Y/%m/%d', max_length=1000)
    time = models.DateTimeField(auto_now_add=True)
    userid = models.IntegerField();
    lat = models.FloatField(null=True)
    lng = models.FloatField(null=True)
    plate_num = models.CharField(null=True, max_length=200)
