package com.example.kikit;

import android.content.Intent;
import android.os.Bundle;
import android.text.LoginFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

public class StoryDisplay extends AppCompatActivity {
        TextView title,desc,date,host,home;
        DatabaseReference reff,fer;
        String TAG="StoryDisplay";
        FirebaseDatabase firebaseDatabase;
        Button join;
        Story_model story_model;
        String storyKey;
    Display_Story display_story;
        FirebaseAuth mAuth;
    String Display_Title,Display_desc,Display_date,Display_host;
    String UserID;

    profileBuild profileBuild;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_display);
        Intent i=getIntent();

        Display_Title=i.getStringExtra("title");
        Display_desc=i.getStringExtra("desc");
        Display_date=i.getStringExtra("date");
        Display_host=i.getStringExtra("userName");
        Intent uidIntent=getIntent();
        UserID=uidIntent.getStringExtra("uid");
        Log.w(TAG,"UserID"+UserID);
        profileBuild=new profileBuild();
        storyKey=i.getStringExtra("StoryKey");
        Log.w(TAG,"Story_Key=>"+storyKey);
        join=findViewById(R.id.join);
        mAuth=FirebaseAuth.getInstance();
        title=findViewById(R.id.title);
        desc=findViewById(R.id.story_desc);
        date=findViewById(R.id.story_date);
        host=findViewById(R.id.story_host);
        home=findViewById(R.id.home);
        Display_host=mAuth.getInstance().getCurrentUser().getDisplayName();
        firebaseDatabase=FirebaseDatabase.getInstance();
        reff=firebaseDatabase.getReference().child("Story");
        story_model=new Story_model();

        display_story=new Display_Story();
        Log.w(TAG,"REFF=>"+reff);
        title.setText(Display_Title);
        desc.setText(Display_desc);
        host.setText(Display_host);
        date.setText(Display_date);


        host.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StoryDisplay.this,UserProfile.class);
                startActivity(intent);
            }
        });
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    reff.child(storyKey).child("coming").push().setValue(mAuth.getCurrentUser().getUid());
                    Intent intent=new Intent(StoryDisplay.this,MapsActivity.class);
                    startActivity(intent);
                }catch (Exception e){
                    Log.w(TAG,"ERROR=>"+e);
                }
            }
        });



        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StoryDisplay.this,Homepage.class);
                startActivity(intent);
            }
        });


    }
    public void fetch_story() {
        Log.w(TAG, "Fetch FUnction");


    }

    public static class Display_Story {
        public String story_Name;
        public String story_desc;
        public String story_date;
        public String story_host;

        public String getStory_category() {
            return story_category;
        }

        public void setStory_category(String story_category) {
            this.story_category = story_category;
        }

        public String story_category;


        public Display_Story(String story_Name, String story_desc, String story_date, String story_host,String story_category) {
            this.story_Name = story_Name;
            this.story_desc = story_desc;
            this.story_date = story_date;
            this.story_host = story_host;
            this.story_category=story_category;
        }

        public Display_Story() {
        }

        public String getStory_Name() {
            return story_Name;
        }

        public void setStory_Name(String story_Name) {
            this.story_Name = story_Name;
        }

        public String getStory_desc() {
            return story_desc;
        }

        public void setStory_desc(String story_desc) {
            this.story_desc = story_desc;
        }

        public String getStory_date() {
            return story_date;
        }

        public void setStory_date(String story_date) {
            this.story_date = story_date;
        }

        public String getStory_host() {
            return story_host;
        }

        public void setStory_host(String story_host) {
            this.story_host = story_host;
        }
    }
}