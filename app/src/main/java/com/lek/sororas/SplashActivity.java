package com.lek.sororas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.lek.sororas.Models.User;
import com.lek.sororas.Utils.CurrentUser;
import com.lek.sororas.Utils.FirebaseHelper;


public class SplashActivity extends BasicActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //String user = getCurrentUser();


        if (checkLogin()) {

            getCurrentUser();


        } else {

//            Intent intent = new Intent(this, LoginActivity.class);
//            startActivity(intent);
//            finish();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

        }

    }

    public boolean checkLogin(){

        return (mAuth.getCurrentUser() != null);

    }

    public void getCurrentUser(){


        DocumentReference docRef = db.collection("users").document(mAuth.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("getUser", "DocumentSnapshot data: " + document.getData());

                        //User user = document.toObject(User.class);
                        CurrentUser.setUser(document.toObject(User.class));

                        FirebaseHelper.setPhotoUriCurrentUser(mAuth);

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();

                        //updateNavigationView();
                        Log.d("getUser", "DocumentSnapshot data: " + document.getData());

                    } else {

//
//                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                        startActivity(intent);
//                        finish();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();

                        Log.d("getUser", "No such document");
                    }
                } else {
                    Log.d("getUser", "get failed with ", task.getException());
                }
            }
        });



    }

    @Override
    public void onResume() {
        super.onResume();
    }

//    public String getCurrentUser(){
//
//        SharedPreferences sharedPref = getSharedPreferences("filename", Context.MODE_PRIVATE);
//
//        return (sharedPref.getString(getString(R.string.user),"null"));
//    }


}