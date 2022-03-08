from django.views import View
from django.http import HttpResponse, JsonResponse
from .serializers import CreateSerializer
from rest_framework.generics import (CreateAPIView)
from rest_framework.response import Response
from rest_framework import status
from .models import PicPost
from .open import plate_detection
import cv2
import numpy as np
import sys
import json
from django.core.serializers import serialize
from PIL import Image


class PostCreateAPIView(CreateAPIView):
	def post(self, request, format=None):
		serializer_class = CreateSerializer(data=request.data)
		if serializer_class.is_valid():
			serializer_class.save()
			car_url = serializer_class.data['model_pic'].split(".com/")[1]
			result = plate_detection(car_url)
			print("Result: ", result )

			return Response(serializer_class.data, status=status.HTTP_201_CREATED)
		return Response(serializer_class.errors, status=status.HTTP_400_BAD_REQUEST)
	

