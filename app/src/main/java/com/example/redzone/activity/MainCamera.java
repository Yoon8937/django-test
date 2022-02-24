package com.example.redzone.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.redzone.R;
import com.example.redzone.networkAPI.CameraApi;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainCamera extends  AppCompatActivity {
    File filePath = null;
    Button bt_take_image;
    ImageView imageView;
    private Bitmap bitmap;
    private int IMG_REQUEST = 21;
    int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_main);

        take_an_image();
    }

    String currentPhotoPath;
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void take_an_image() {
        setContentView(R.layout.camera_main);

        //Assign Variable
        imageView = findViewById(R.id.imageview);
        bt_take_image = findViewById(R.id.bt_take_image);
        //Request For Camera permission 카메라 허가 요청
        if (ContextCompat.checkSelfPermission(MainCamera.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainCamera.this, new String[]{Manifest.permission.CAMERA}, 100);
        }

//        Uri realUri = null; //원본 이미지 저정할 변수

        bt_take_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open camera 카메라 열기
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                System.out.println("사진 찍었다?");
                if ( intent.resolveActivity(getPackageManager()) != null){
                    File photoFile = null;
                    try{
                        photoFile = createImageFile();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(photoFile != null){
                        Uri photoURI = FileProvider.getUriForFile(MainCamera.this, "com.example.redzone.fileprovider", photoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("This is onActivityResult");
        System.out.println(requestCode);
        System.out.println(resultCode);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK ) {
            System.out.println("들어왔어!");
            //System.out.println(Build.VERSION.SDK_INT); 28

            File file = new File(currentPhotoPath);

            Bitmap bitmap = null;
            if (android.os.Build.VERSION.SDK_INT >= 28) {
                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), Uri.fromFile(file));
                try {
                    bitmap = ImageDecoder.decodeBitmap(source);
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            upload_an_image(bitmap);
        }

    }

    public void upload_an_image(Bitmap captureImage) {
        new AlertDialog.Builder(MainCamera.this)
                .setMessage("촬영된 이미지를 서버로 전송합니다. \n전송하시겠습니까?")
                .setPositiveButton("보내기", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "신고가 접수되었습니다.", Toast.LENGTH_SHORT).show();
                        uploadphoto(captureImage);
                        // gps 합쳐서 전송
                        finish();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }

    private void uploadphoto(Bitmap captureImage) {

        /////////////////////////////////////////
        Intent splashintent = getIntent();
        Integer id = splashintent.getIntExtra("id", -1);
        float lat = splashintent.getFloatExtra("lat", (float) -1.0);
        float lng = splashintent.getFloatExtra("lng", (float) -1.0);

        System.out.print("Camera got ");
        System.out.println(id);

        System.out.print("위도: ");

        System.out.print(lat);
        System.out.print(", 경도: ");
        System.out.println(lng);
        /////////////////////////////////////////
        TimeZone tz;
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");//.format(new Date());
        tz = TimeZone.getTimeZone("Asia/Seoul");
        dateFormat.setTimeZone(tz);
        Date date = new Date();
        String imageFileName = "JPEG_" + dateFormat.format(date) + "_";

        File imageFile = new File(saveBitmapToJpg(captureImage, imageFileName));
        System.out.println("이미지 경로:" + imageFile.toString());

        Retrofit retrofit = new Retrofit.Builder().baseUrl(CameraApi.DJANGO_SITE).addConverterFactory(GsonConverterFactory.create()).build();
        CameraApi api = retrofit.create(CameraApi.class);

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/data"), imageFile);
        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("model_pic", imageFile.getName(), requestBody);

        RequestBody idBody = RequestBody.create(MediaType.parse("text/palin"), String.valueOf(id));
        RequestBody latBody = RequestBody.create(MediaType.parse("text/palin"), String.valueOf(lat));
        RequestBody lngBody = RequestBody.create(MediaType.parse("text/palin"), String.valueOf(lng));

        HashMap<String, RequestBody> requestMap = new HashMap<>();
        requestMap.put("userid", idBody);
        requestMap.put("lat", latBody);
        requestMap.put("lng", lngBody);

        Call<RequestBody> call = api.uploadImage(multipartBody, requestMap);

        call.enqueue(new Callback<RequestBody>() {
            @Override
            public void onResponse(Call<RequestBody> call, Response<RequestBody> response) {
                Log.d("good", "good");
                System.out.println(requestBody);
            }

            @Override
            public void onFailure(Call<RequestBody> call, Throwable t) {
                Log.d("fail", ""+t.getMessage());

            }
        });
    }

    public String saveBitmapToJpg(Bitmap bitmap, String name) {
        File storage = getCacheDir();
        String fileName = name + ".jpg";
        File imgFile = new File(storage, fileName);

        try {

            imgFile.createNewFile();
            FileOutputStream out = new FileOutputStream(imgFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
        } catch (FileNotFoundException e) {
            Log.e("saveBitmapToJpg", "FileNotFoundException: " + e.getMessage());
        } catch (IOException e) {
            Log.e("saveBitmapToJpg", "IOException: " + e.getMessage());
        }
        Log.d("imgPath", getCacheDir() + "/" + fileName);
        return getCacheDir() + "/" + fileName;

    }
}