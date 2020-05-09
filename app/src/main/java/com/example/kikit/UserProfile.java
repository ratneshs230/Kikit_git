package com.example.kikit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;

public class UserProfile extends AppCompatActivity {

    FirebaseUser user;
    ImageView profile_pic;

    String TAG="USER_PROFILE";
    String uid;
    TextView username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
try {
    Intent i = getIntent();
    uid = i.getStringExtra("uid");
    username=findViewById(R.id.user);
    user= FirebaseAuth.getInstance().getCurrentUser();

    profile_pic=findViewById(R.id.profile_picture);
    display_profile(uid);

    if(user!=null){
        username.setText(user.getDisplayName());

            if(user.getPhotoUrl()!=null){
                Glide.with(this)
                .load(user.getPhotoUrl())
                .into(profile_pic);
            }
    }



}catch (Exception e){
    e.printStackTrace();
}
    }

    private void display_profile(String uid) {
        if(uid!=null){

        }

    }
}
