package com.example.kikit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseUser user;
    String uid;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progress);
        int SPLASH_SCREEN_TIME_OUT=2000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);

                Intent intent=new Intent(MainActivity.this,AuthSelect.class);
                startActivity(intent);
                finish();

            }
        }, SPLASH_SCREEN_TIME_OUT);
    }


    @Override
    protected void onStart() {
        super.onStart();

        user=mAuth.getCurrentUser();
        if(user!=null)
        {
        uid=user.getUid();
        Intent intent=new Intent(MainActivity.this,Homepage.class);
        intent.putExtra("UID",uid);
        startActivity(intent);
        }

    }
}
