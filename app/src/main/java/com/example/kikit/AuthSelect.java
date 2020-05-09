package com.example.kikit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AuthSelect extends AppCompatActivity implements View.OnClickListener {
    Button login,register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_select);
        login=findViewById(R.id.login_btn);
        register=findViewById(R.id.register_btn);

        register.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.login_btn: {
                Intent intent = new Intent(AuthSelect.this, Login.class);
                startActivity(intent);
                break;
            }
            case R.id.register_btn: {
                Intent intent = new Intent(AuthSelect.this, Register.class);
                startActivity(intent);
                break;
            }
        }
    }
}
