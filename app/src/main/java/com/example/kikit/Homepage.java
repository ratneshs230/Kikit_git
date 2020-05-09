package com.example.kikit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Homepage extends AppCompatActivity implements View.OnClickListener {
    ImageButton dineouts, trips, events, sports;
    private RecyclerView story_Recycler;
    private LinearLayoutManager linearLayoutManager;
    Story_model story_model;
    StoryDisplay mstory_display;
    private GridLayoutManager gridLayoutManager ;
    private FirebaseRecyclerAdapter adapter;
    private FirebaseAuth mAuth;
    ImageButton add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        add = findViewById(R.id.add);
        dineouts = findViewById(R.id.dineouts);
        trips = findViewById(R.id.trips);
        events = findViewById(R.id.events);
        sports = findViewById(R.id.sports);


        dineouts.setOnClickListener(this);
        trips.setOnClickListener(this);
        sports.setOnClickListener(this);

        events.setOnClickListener(this);
        add.setOnClickListener(this);
        mstory_display=new StoryDisplay();
        story_Recycler = findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        story_Recycler.setLayoutManager(linearLayoutManager);
        story_Recycler.setHasFixedSize(true);
        fetch();
    }

    private void fetch() {

        Query query = FirebaseDatabase.getInstance().getReference().child("Story");
        FirebaseRecyclerOptions<Story_model> options = new FirebaseRecyclerOptions.Builder<Story_model>()
                .setQuery(query, Story_model.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Story_model, ViewHolder>(options) {

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
                return new ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, final int position, @NonNull final Story_model model) {

                holder.setEventname(model.getStory_Name());
                holder.setEventhost(model.getStory_host());
                holder.setEventdesc(model.getStory_desc());
                holder.setEventImg(model.getStory_image())
                ;
                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(Homepage.this,StoryDisplay.class);
                        intent.putExtra("StoryKey",model.getStory_key());


                        intent.putExtra("title", model.getStory_Name());
                        intent.putExtra("desc", model.getStory_desc());
                        intent.putExtra("date", model.getStory_date());
                        intent.putExtra("username",model.getStory_host());
                        intent.putExtra("uid", model.getUID());

                        startActivity(intent);
                    }
                });

            }
        };
        story_Recycler.setAdapter(adapter);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout root;
        public TextView eventname, eventdesc, eventhost;
        public ImageView event_img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root);
            eventdesc = itemView.findViewById(R.id.evetdesc);
            eventhost = itemView.findViewById(R.id.eventhost);
            eventname = itemView.findViewById(R.id.eventName);
            event_img=itemView.findViewById(R.id.event_img);

        }

        public LinearLayout getRoot() {
            return root;
        }

        public void setRoot(LinearLayout root) {
            this.root = root;
        }


        public void setEventname(String string) {
            eventname.setText(string);
        }


        public void setEventdesc(String string) {
            eventdesc.setText(string);
        }


        public void setEventhost(String string) {
            eventdesc.setText(string);
        }
        public void setEventImg(Uri image) {

            event_img.setImageURI(image);
        }


    }
    private void getStoryImage(){

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
    public void fetch_category_wise(String category) {
        story_model = new Story_model();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Query query = database.child("Story").orderByChild("story_category").equalTo(category);

        FirebaseRecyclerOptions<Story_model> options = new FirebaseRecyclerOptions.Builder<Story_model>()
                .setQuery(query, Story_model.class)
                .build();
        FirebaseRecyclerAdapter<Story_model, ViewHolder> cat_adapter = new FirebaseRecyclerAdapter<Story_model, ViewHolder>(options) {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
                return new ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, final int position, @NonNull Story_model model) {
                holder.setEventname(model.getStory_Name());
                holder.setEventhost(model.getStory_host());
                holder.setEventdesc(model.getStory_desc());
                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(Homepage.this, "Touched" + position, Toast.LENGTH_LONG).show();
                    }
                });


            }


        };

        story_Recycler.setAdapter(cat_adapter);
        cat_adapter.startListening();



    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add: {
                Intent intent = new Intent(Homepage.this, HostActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.dineouts: {
                fetch_category_wise("Dineouts");


                break;
            }
            case R.id.events: {
                fetch_category_wise("Events");
                break;
            }
            case R.id.trips: {
                fetch_category_wise("Trips");
                break;
            }
            case R.id.sports: {
                fetch_category_wise("Sports");

                break;
            }
        }
    }


}