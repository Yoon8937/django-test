package com.example.redzone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.redzone.R;

public class MainResult extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_result_main);

        Intent userid = getIntent();
        String useridinfo = userid.getStringExtra("id");
        String username = userid.getStringExtra("username");
        TextView textView = (TextView)findViewById(R.id.usernameinfo);
        Log.d(this.getClass().getName(), (String)textView.getText());
        textView.setText(username+"님.");


        Button ReportBtn = (Button) findViewById(R.id.ReportBtn);
        ReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent reportintent = getIntent();
                Integer id = reportintent.getIntExtra("id", -1);

                Intent intent = new Intent(getApplicationContext(), MainSplash.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

    }

}