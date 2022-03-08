from rest_framework import serializers
from rest_framework.serializers import (ModelSerializer)
from .models import PicPost

class CreateSerializer(ModelSerializer):

    class Meta:
        model = PicPost
        fields = ['model_pic', 'userid', 'lat', 'lng', 'plate_num']

