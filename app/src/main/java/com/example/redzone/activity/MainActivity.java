package com.example.redzone.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.content.Intent;

import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.example.redzone.R;
import com.example.redzone.networkAPI.ServiceApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private EditText mUsername;
    private EditText mPassword2;
    private  Button mLoginBtn;
    private ServiceApi service;
    private ProgressBar mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsername = (EditText) findViewById(R.id.user);
        mPassword2 = (EditText) findViewById(R.id.password2);
        mProgressView = (ProgressBar) findViewById(R.id.register_progress_main);


        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServiceApi.DJANGO_SITE)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        service = retrofit.create(ServiceApi.class);


        Button toregister = (Button) findViewById(R.id.toRegisterBtn);

        toregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainRegister.class);
                startActivity(intent);
            }
        });


        mLoginBtn = (Button) findViewById(R.id.LoginBtn);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    private void attemptLogin(){
        mUsername.setError(null);
        mPassword2.setError(null);

        String username = mUsername.getText().toString();
        String password2 = mPassword2.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (username.isEmpty()) {
            mUsername.setError("이름을 입력해주세요.");
            focusView = mUsername;
            cancel = true;
        }
        if (password2.isEmpty()){
            mPassword2.setError("비밀번호를 입력해주세요.");
            focusView = mPassword2;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            startLogin(username, password2);
            showProgress(true);
        }
    }

    private void startLogin(String username, String password){
        Call<ResponseBody> call = service.addLog(username, password);  // no error

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Gson gson = new Gson();
                String obj = null;
                try {
                    obj = gson.toJson(response.body().string());

                    JsonPrimitive object = gson.fromJson(obj, JsonPrimitive.class);
                    System.out.println("primitive:" + object.getAsString());

                    JsonObject jsonObject = gson.fromJson(object.getAsString(), JsonObject.class);

                    int code = Integer.parseInt(String.valueOf(jsonObject.get("hello")));

                    if (code == -1){
                        showProgress(false);
                        Toast.makeText(MainActivity.this, "아이디 또는 비밀번호를 잘못 입력하셨습니다.", Toast.LENGTH_SHORT).show();
                    }else {
                        showProgress(false);
                        Toast.makeText(MainActivity.this, "환영합니다 " + username + "님.", Toast.LENGTH_SHORT).show();
                        Log.d("Success","Successssssssssssssssssss");
                        Intent intent = new Intent(getApplicationContext(), MainResult.class);
                        intent.putExtra("id", code);
                        startActivity(intent);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Wifi 또는 네트워크를 확인하세요..", Toast.LENGTH_SHORT).show();
                Log.e("네트워크를 확인하세요.", t.getMessage());
            }
        });

    }

    private void showProgress(boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE );
    }

}