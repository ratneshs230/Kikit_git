package com.example.kikit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Register extends AppCompatActivity implements View.OnClickListener {
        EditText name, email,password;
        Button register_btn;
        FirebaseAuth mAuth;
        FirebaseDatabase db;
        DatabaseReference reff;
    String mailId, pass,Name,pushKey;
    ImageView profilePic;
    User_model user_model;
    String TAG="LOGIN_ACTIVITY";
    StorageReference storageReference;
    Uri imageUri;
    Uri photoUrl;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try{if(requestCode==1000){
            if(resultCode== Activity.RESULT_OK){
                imageUri=data.getData();
                profilePic.setImageURI(imageUri);


            }
        }}
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register);
try {
    name = findViewById(R.id.name);
    email = findViewById(R.id.email);
    password = findViewById(R.id.pass);
    profilePic = findViewById(R.id.profile_pic);
    register_btn = findViewById(R.id.register_btn);

    mAuth = FirebaseAuth.getInstance();
    user_model = new User_model();
    db = FirebaseDatabase.getInstance();
    reff = db.getReference().child("User");
    FirebaseUser user=mAuth.getCurrentUser();
    storageReference = FirebaseStorage.getInstance().getReference();
    register_btn.setOnClickListener(this);

    if(user.getPhotoUrl()!=null){
        Glide.with(this)
                .load(user.getPhotoUrl())
                .into(profilePic);
    }
    profilePic.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(openGallery, 1000);
        }
    });
}catch (Exception e){
    e.printStackTrace();
}
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.register_btn:{

                    mailId = email.getText().toString();
                    pass = password.getText().toString();
                    Name=name.getText().toString();
                    email_Register(mailId, pass,Name,imageUri);
                    register_btn.setEnabled(false);

                }
            }
        }

        private void email_Register(final String mailId, final String pass, final String Name, final Uri image) {


            mAuth.createUserWithEmailAndPassword(mailId,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Log.d(TAG,"User Creation Successful");

                       try {
                           FirebaseUser user = mAuth.getCurrentUser();
                           UserProfileChangeRequest profileChangeRequest=new UserProfileChangeRequest.Builder()
                                   .setDisplayName(Name).setPhotoUri(image).build();


                           user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                              Log.w(TAG,"Profile Updated Successfully");
                               }
                           });
                           String uid = user.getUid();
                           user_data_save(Name,mailId,pass,uid,image);

                           //user_data_save(Name,mailId,pass,uid);
                           Intent intent = new Intent(Register.this, Homepage.class);
                           startActivity(intent);
                       }
                       catch (Exception e){
                           Log.w(TAG,"Exception in user=>"+e);
                       }

                    }
                    else{

                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(Register.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();                }
                }
            });

        }
/*private void getDownloadUrl(StorageReference storageReference){
        storageReference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        setUserProfileImage(uri);
                    }
                });
}*/
private void setUserProfileImage(Uri uri){
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest request=new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();
        user.updateProfile(request).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(Register.this,"Image Updated.",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Register.this, "Profile image failed to download", Toast.LENGTH_SHORT).show();
            }
        });
}
    private void user_data_save(String name, String email, final String password, String uid, Uri image) {
        final StorageReference fileref=storageReference.child("profile Images").child(uid+"jpeg");
        final String Stringpath;

        fileref.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.w(TAG,"image Uploaded Successfully");
                Task<Uri> downloadUri=taskSnapshot.getStorage().getDownloadUrl();
                if(downloadUri.isSuccessful()){
                    Uri path=downloadUri.getResult();
                    setUserProfileImage(path);
                    user_model.setPhoto_url(path);
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Register.this,"Error Uploading File",Toast.LENGTH_LONG).show();
            }
        });
/*
        fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {


                photoUrl=uri;
                Log.w(TAG,"PHOTOURI"+photoUrl);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG,"Error in Downloading image");
            }
        });*/
        user_model.setName(name);
        user_model.setEmail(mailId);
        user_model.setPassword(password);
        user_model.setUid(uid);
        user_model.setUser_key(pushKey);

        reff.child(uid).push().setValue(user_model);
        Log.w(TAG,"PUSH2Key=>"+reff.push().getKey());

    }
}
